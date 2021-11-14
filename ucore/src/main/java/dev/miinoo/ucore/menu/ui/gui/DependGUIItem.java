package dev.miinoo.ucore.menu.ui.gui;

/**
 * @author DotClass
 *
 */

import dev.miinoo.ucore.menu.ui.Dimension;
import dev.miinoo.ucore.menu.ui.UIs;
import dev.miinoo.ucore.menu.ui.callback.Callback;
import dev.miinoo.ucore.menu.ui.ui.UIItem;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class DependGUIItem extends GUIElement implements UIItem {

	private Supplier<ItemStack> item;
	private UIItem.OnClickListener listener;

	public DependGUIItem(Supplier<ItemStack> item) {
		Validate.notNull(item);
		this.item = item;
	}

	public DependGUIItem(Supplier<ItemStack> item, Callback action) {
		Validate.notNull(item);
		this.item = item;
		listener = new CallbackOnClickListener(action);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(1, 1);
	}

	public DependGUIItem setOnClickListener(UIItem.OnClickListener listener) {
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
		return item.get();
	}

	@Override
	public ItemStack lock(Inventory inventory) {
		return UIs.getLockManager().lock(item.get(), inventory);
	}


}