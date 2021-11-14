package dev.miinoo.ucore.menu.ui;

/**
 * @author DotClass
 */

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class UIs implements Listener {

    public static void load(Plugin plugin, UIManager manager, LockManager lock) {
        plugin.getServer().getPluginManager().registerEvents(new UIs(), plugin);
        UIs.manager = manager;
        UIs.lock = lock;
    }

    private static UIManager manager;
    private static LockManager lock;

    public static UIManager getUIManager() {
        return manager;
    }

    public static LockManager getLockManager() {
        return lock;
    }

    public static void setLockManager(LockManager lock) {
        if (lock != null)
            UIs.lock = lock;
    }

    private UIs() {
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Inventory inventory = event.getView().getTopInventory();
            if (inventory != null && event.getClickedInventory() != null) {
                if (event.getCurrentItem() != null && event.getCurrentItem().getType() != XMaterial.AIR.parseMaterial()) {
                    if (!lock.verify(event.getCurrentItem(), inventory)) {
                        event.setCancelled(true);
                        event.setCurrentItem(new ItemStack(XMaterial.AIR.parseMaterial()));
                        return;
                    }
                }
                //if (inventory == event.getClickedInventory()) {
                manager.handle((Player) event.getWhoClicked(), event);
                //}
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Inventory inventory = event.getView().getTopInventory();
            if (inventory != null) {
                for (int slot : event.getRawSlots()) {
                    if (slot < inventory.getSize()) {
                        manager.handle((Player) event.getWhoClicked(), event);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack() != null
                && event.getItemDrop().getItemStack().getType() != XMaterial.AIR.parseMaterial()) {
            if (!lock.verify(event.getItemDrop().getItemStack(), null)) {
                event.getItemDrop().remove();
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() != XMaterial.AIR.parseMaterial()) {
            if (!lock.verify(event.getItem(), null)) {
                event.setCancelled(true);
                event.getPlayer().setItemInHand(new ItemStack(XMaterial.AIR.parseMaterial()));
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            manager.handle((Player) event.getPlayer(), event);
        }
    }

}