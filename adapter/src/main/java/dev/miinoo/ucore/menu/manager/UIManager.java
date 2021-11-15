package dev.miinoo.ucore.menu.manager;

import dev.miinoo.ucore.menu.UI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public interface UIManager {

	void open(Player player, UI ui);
	
	UI getCurrent(Player player);
	
	void handle(Player player, InventoryClickEvent event);
	
	void handle(Player player, InventoryDragEvent event);
	
	void handle(Player player, InventoryCloseEvent event);
}