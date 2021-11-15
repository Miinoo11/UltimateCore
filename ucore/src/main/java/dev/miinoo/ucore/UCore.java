package dev.miinoo.ucore;

import dev.miinoo.ucore.menu.manager.UIs;
import dev.miinoo.ucore.menu.ui.manager.EmptyLockManager;
import dev.miinoo.ucore.sidebar.SidebarAPI;
import dev.miinoo.ucore.version.VersionMatcher;
import dev.miinoo.ucore.version.VersionWrapper;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class UCore {

    private static Plugin plugin;
    private static VersionWrapper WRAPPER = new VersionMatcher().match();
    private static PlaceholderAPIPlugin placeholderAPI;
    private static SidebarAPI sidebarAPI;

    public static Plugin getPlugin() {
        return plugin;
    }
    public static VersionWrapper getWRAPPER() {
        return WRAPPER;
    }
    public static PlaceholderAPIPlugin getPlaceholderAPI() {
        return placeholderAPI;
    }
    public static SidebarAPI getSidebarAPI() {
        return sidebarAPI;
    }

    public UCore(Plugin plugin) {
        this.plugin = plugin;
        sidebarAPI = new SidebarAPI(this.plugin);

        UIs.load(this.plugin, UIs.getUIManager(), new EmptyLockManager());
        registerPlaceholders();
    }

    public void disable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.closeInventory();
        }
    }


    private void registerPlaceholders() {
        if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            try {
                placeholderAPI = (PlaceholderAPIPlugin) plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI");
            } catch (ClassCastException | NullPointerException ignored) {
            }
            if (placeholderAPI != null) {
                plugin.getLogger().info("Hooked PlaceholderAPI v" + placeholderAPI.getDescription().getVersion());

            }
        } else {
            placeholderAPI = null;
        }
    }
}
