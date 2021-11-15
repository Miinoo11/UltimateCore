package dev.miinoo.ucore.menu.ui.gui;

import dev.miinoo.ucore.menu.Dimension;
import dev.miinoo.ucore.menu.UIItem;
import dev.miinoo.ucore.menu.manager.UIs;
import dev.miinoo.ucore.menu.ui.callback.Callback;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIItem extends GUIElement implements UIItem {

	private ItemStack item;
	private UIItem.OnClickListener listener;

	public GUIItem(ItemStack item) {
		Validate.notNull(item);
		this.item = item;
	}

	public GUIItem(ItemStack item, Callback action) {
		Validate.notNull(item);
		this.item = item;
		listener = new CallbackOnClickListener(action);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(1, 1);
	}

	public GUIItem setOnClickListener(UIItem.OnClickListener listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public boolean onClick(Player player, int x, int y, InventoryClickEvent event) {
		if (listener != null) {
			return listener.onClick(player, this, event);
		}
		return false;
	}

	@Override
	public UIItem[][] getItems() {
		return new UIItem[][] { new UIItem[] { this } };
	}

	@Override
	public ItemStack getItem() {
		return item;
	}

	@Override
	public ItemStack lock(Inventory inventory) {
		return UIs.getLockManager().lock(item, inventory);
	}

}

class CallbackOnClickListener implements UIItem.OnClickListener {

	private Callback action;

	public CallbackOnClickListener(Callback action) {
		this.action = action;
	}

	@Override
	public boolean onClick(Player player, UIItem item, InventoryClickEvent event) {
		action.call();
		return false;
	}

}
