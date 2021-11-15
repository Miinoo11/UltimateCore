package dev.miinoo.ucore.menu.ui.gui;

import dev.miinoo.ucore.menu.Dimension;
import dev.miinoo.ucore.menu.UIItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIDroppedItem extends GUIElement implements UIItem {

	private ItemStack item;
	
	public GUIDroppedItem(ItemStack item) {
		this.item = item;
	}
	
	@Override
	public Dimension getSize() {
		return new Dimension(1, 1);
	}

	@Override
	public boolean onClick(Player player, int x, int y, InventoryClickEvent event) {
		event.setCancelled(false);
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
		return item;
	}

}
