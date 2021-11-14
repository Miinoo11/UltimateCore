package dev.miinoo.ucore.menu.ui.ui;

/**
 * @author DotClass
 *
 */
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface UIItem extends UIElement {

	ItemStack getItem();
	
	ItemStack lock(Inventory inventory); 
	
	interface OnClickListener {
		
		boolean onClick(Player player, UIItem item, InventoryClickEvent event);
		
	}
	
}