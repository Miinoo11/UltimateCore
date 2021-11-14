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

import java.util.function.Consumer;

public class GUIDropItem extends GUIElement implements UIDropElement {

	private ItemStack item;
	private Consumer<ItemStack> action;
	private DropFilter filter;

	public GUIDropItem(ItemStack item, Consumer<ItemStack> action) {
		this.item = item;
		this.action = action;
	}

	public GUIDropItem(Consumer<ItemStack> action) {
		this.action = action;
	}

	@Override
	public Dimension getSize() {
		return new Dimension(1, 1);
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
	public void onClose(Player player, ItemStack[][] items) {
		if (action != null) {
			action.accept(items[0][0]);
		}
	}

	@Override
	public void call(Player player, ItemStack[][] items) {
		if (action != null) {
			action.accept(items[0][0]);
		}
	}

	@Override
	public UIItem[][] getItems() {
		return new UIItem[][] { new UIItem[] { new GUIDroppedItem(item) } };
	}

}
