package dev.miinoo.ucore.menu.ui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author DotClass
 *
 */
public interface LockManager {

	ItemStack lock(ItemStack item, Inventory inventory);
	
	ItemStack unlock(ItemStack item);
	
	boolean verify(ItemStack item, Inventory inventory);
	
}