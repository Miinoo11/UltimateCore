package dev.miinoo.ucore.menu.ui.gui;

import dev.miinoo.ucore.UCore;
import dev.miinoo.ucore.menu.Dimension;
import dev.miinoo.ucore.menu.UI;
import dev.miinoo.ucore.menu.UIElement;
import dev.miinoo.ucore.menu.UIItem;
import dev.miinoo.ucore.menu.manager.UIs;
import dev.miinoo.ucore.menu.ui.Permissions;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GUI implements UI {
	
	protected final Player player;
	private String title;
	private Inventory inventory;
	protected int size;
	private final Map<Integer, UIElement> elements = new LinkedHashMap<>();
	protected String permission;

	public GUI(Player player, String title, int size) {
		this.player = player;
		this.title = title;
		inventory = Bukkit.createInventory(this, this.size = size, title);
	}

	protected void setSize(int size) {
		inventory = Bukkit.createInventory(this, this.size = size, title);
	}

	@Override
	public final void open() {
		if (Permissions.isPermitted(player, permission)) {
			build();
			UIs.getUIManager().open(player, this);
			player.openInventory(inventory);
			player.updateInventory();
		} else {
			player.sendMessage(Permissions.notPermitted);
		}
	}

	public final void addElement(int slot, UIElement element) {
		Validate.notNull(element);
		Validate.isTrue(slot >= 0 && slot < inventory.getSize());
		elements.put(slot, element);
	}

	protected void build() {
		for (Entry<Integer, UIElement> entry : elements.entrySet()) {
			build(entry.getKey(), entry.getValue());
		}
	}

	public void build(int slot, UIElement element) {
		if (element.isEnabled(player)) {
			Dimension dimension = element.getSize();
			int x = slot % 9;
			int y = slot / 9;
			UIItem[][] uiitems = element.getItems();
			if (uiitems == null) {
				uiitems = new UIItem[dimension.getHeight()][dimension.getWidth()];
			}
			if (uiitems.length > dimension.getHeight()) {
				throw new IndexOutOfBoundsException();
			}
			for (int i = 0; i < uiitems.length; i++) {
				UIItem[] items = uiitems[i];
				if (items.length > dimension.getWidth()) {
					throw new IndexOutOfBoundsException();
				}
				for (int j = 0; j < items.length; j++) {
					UIItem uiitem = items[j];
					ItemStack item = null;
					if (uiitem != null) {
						if (!uiitem.isEnabled(player)) {
							continue;
						}
						item = uiitem.lock(inventory);
					}
					inventory.setItem((y + i) * 9 + x + j, item);
				}
			}
		}
	}

	protected void build(int slot, UIElement element, int xMin, int yMin, int xMax, int yMax) {
		if (element.isEnabled(player)) {
			Dimension dimension = (Dimension) element.getSize();
			int x = slot % 9;
			int y = slot / 9;
			UIItem[][] uiitems = element.getItems();
			if (uiitems == null) {
				uiitems = new UIItem[dimension.getHeight()][dimension.getWidth()];
			}
			if (uiitems.length > dimension.getHeight()) {
				throw new IndexOutOfBoundsException();
			}
			for (int i = 0; i < uiitems.length; i++) {
				if (y + i >= yMin && y + i <= yMax) {
					UIItem[] items = uiitems[i];
					if (items.length > dimension.getWidth()) {
						throw new IndexOutOfBoundsException();
					}
					for (int j = 0; j < items.length; j++) {
						if (x + j >= xMin && x + j <= xMax) {
							UIItem uiitem = items[j];
							ItemStack item = null;
							if (uiitem != null) {
								if (!uiitem.isEnabled(player)) {
									continue;
								}
								item = uiitem.lock(inventory);
							}
							inventory.setItem((y + i) * 9 + x + j, item);
						}
					}
				}
			}
		}
	}
	@Override
	public final void onClick(InventoryClickEvent event) {
		if (!Permissions.isPermitted(player, permission)) {
			player.sendMessage(Permissions.notPermitted);
			return;
		}
		boolean rebuild = false;
		int slot = event.getSlot();
		int x = slot % 9;
		int y = slot / 9;
		for (Entry<Integer, UIElement> entry : elements.entrySet()) {
			UIElement element = entry.getValue();
			Dimension dimension = (Dimension) element.getSize();
			int eslot = entry.getKey();
			int ex = eslot % 9;
			int ey = eslot / 9;
			if (x >= ex && x < ex + dimension.getWidth() && y >= ey && y < ey + dimension.getHeight()) {
				if (element.onClick((Player) event.getWhoClicked(), x - ex, y - ey, event)) {
					rebuild = true;
				}
			}
		}
		if (rebuild) {
			build();
		}
	}

	public final void close() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(player.getOpenInventory().getTopInventory().equals(inventory)) {
					player.closeInventory();
				}
			}
		}.runTask(UCore.getPlugin());
	}

	@Override
	public final void onClose(InventoryCloseEvent event) {
		for (Entry<Integer, UIElement> entry : elements.entrySet()) {
			UIElement element = entry.getValue();
			int slot = entry.getKey();
			Dimension size = (Dimension) element.getSize();
			int width = size.getWidth();
			int height = size.getHeight();
			ItemStack[][] items = new ItemStack[height][width];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					items[y][x] = inventory.getItem(y * 9 + slot + x);
				}
			}
			element.onClose(player, items);
		}
		onClose(player);
	}
	
	public final void callAll() {
		for (Entry<Integer, UIElement> entry : elements.entrySet()) {
			call(entry.getKey(), entry.getValue());
		}
	}
	
	public void call(int slot, UIElement element) {
		Dimension size = (Dimension) element.getSize();
		int width = size.getWidth();
		int height = size.getHeight();
		ItemStack[][] items = new ItemStack[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				items[y][x] = inventory.getItem(y * 9 + slot + x);
			}
		}
		element.call(player, items);
	}
	
	protected void onClose(Player player) {
	}

	@Override
	public final Inventory getInventory() {
		return inventory;
	}

	public final void setPermission(String permission) {
		this.permission = permission;
	}

}