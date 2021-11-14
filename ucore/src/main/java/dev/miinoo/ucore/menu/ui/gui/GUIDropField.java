package dev.miinoo.ucore.menu.ui.gui;

/**
 * @author DotClass
 *
 */

import dev.miinoo.ucore.menu.ui.Dimension;
import dev.miinoo.ucore.menu.ui.ui.UIDropElement;
import dev.miinoo.ucore.menu.ui.ui.UIItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GUIDropField extends GUIElement implements UIDropElement {

	private Dimension dimension;
	private UIItem[][] items;
	private Consumer<List<ItemStack>> action;
	private DropFilter filter;

	public GUIDropField(int width, int height, List<ItemStack> items, Consumer<List<ItemStack>> action) {
		dimension = new Dimension(width, height);
		this.items = new UIItem[height][width];
		if (items != null) {
			for (int i = 0; i < height; i++) {
				UIItem[] u = new UIItem[width];
				for (int j = 0; j < width; j++) {
					int index = i * width + j;
					if (index >= items.size()) {
						break;
					}
					u[j] = new GUIDroppedItem(items.get(index));
				}
				this.items[i] = u;
			}
		}
		this.action = action;
	}

	public GUIDropField(int width, int height, Consumer<List<ItemStack>> action) {
		dimension = new Dimension(width, height);
		this.action = action;
	}

	@Override
	public Dimension getSize() {
		return dimension;
	}

	@Override
	public UIDropElement setFilter(DropFilter filter) {
		this.filter = filter;
		return this;
	}

	@Override
	public boolean onClick(Player player, int x, int y, InventoryClickEvent event) {
		if ((event.getAction() == InventoryAction.PICKUP_ALL || event.getAction() == InventoryAction.PICKUP_HALF
				|| event.getAction() == InventoryAction.PICKUP_ONE || event.getAction() == InventoryAction.PICKUP_SOME)
				|| (filter == null || filter.accept(event.getCursor()))) {
			event.setCancelled(false);
		}
		return false;
	}

	@Override
	public void call(Player player, ItemStack[][] items) {
		if (action != null) {
			List<ItemStack> list = new ArrayList<>();
			for (ItemStack[] stacks : items) {
				for (ItemStack item : stacks) {
					list.add(item);
				}
			}
			action.accept(list);
		}
	}

	@Override
	public void onClose(Player player, ItemStack[][] items) {
		if (action != null) {
			List<ItemStack> list = new ArrayList<>();
			for (ItemStack[] stacks : items) {
				for (ItemStack item : stacks) {
					list.add(item);
				}
			}
			action.accept(list);
		}
	}

	@Override
	public UIItem[][] getItems() {
		return items;
	}

}
