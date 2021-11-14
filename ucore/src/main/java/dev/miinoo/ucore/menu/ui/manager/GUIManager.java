package dev.miinoo.ucore.menu.ui.manager;

import dev.miinoo.ucore.menu.ui.UIException;
import dev.miinoo.ucore.menu.ui.UIManager;
import dev.miinoo.ucore.menu.ui.ui.UI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class GUIManager implements UIManager {

	private final Map<Player, UI> uis = new HashMap<>();

	@Override
	public void open(Player player, UI ui) {
		uis.put(player, ui);
	}

	@Override
	public UI getCurrent(Player player) {
		return uis.get(player);
	}

	@Override
	public void handle(Player player, InventoryClickEvent event) {
		Inventory inventory = event.getView().getTopInventory();
		UI ui = uis.get(player);
		if (ui != null && ui.getInventory().equals(inventory)) {
			event.setCancelled(true);
			if(event.getClickedInventory().equals(inventory)) {
				try {
					ui.onClick(event);
				} catch (Throwable throwable) {
					event.getWhoClicked().closeInventory();
					throw new UIException(throwable);
				}
			}
		}
	}

	@Override
	public void handle(Player player, InventoryDragEvent event) {
		Inventory inventory = event.getView().getTopInventory();
		UI ui = uis.get(player);
		if (ui != null && ui.getInventory().equals(inventory)) {
			event.setCancelled(true);
			return;
		}
	}

	@Override
	public void handle(Player player, InventoryCloseEvent event) {
		UI ui = uis.get(player);
		if (ui != null && ui.getInventory().equals(event.getView().getTopInventory())) {
			ui.onClose(event);
			event.getInventory().clear();
		}
	}
	
}