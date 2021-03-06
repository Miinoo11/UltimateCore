package dev.miinoo.ucore.menu;

/**
 * @author DotClass
 *
 */
import dev.miinoo.ucore.menu.manager.UIManager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface UI extends InventoryHolder {

	void open();

	void onClick(InventoryClickEvent event);

	void onClose(InventoryCloseEvent event);
	
	Inventory getInventory();

}