package dev.miinoo.ucore.sidebar;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.ArrayList;
import java.util.List;

public class SidebarAPI {

    private Plugin plugin;
    private static List<Sidebars> sidebars;

    public Plugin getPlugin() {
        return plugin;
    }

    public SidebarAPI(Plugin plugin) {
        sidebars = new ArrayList<>();
    }

    protected static void registerSidebar(Sidebars sidebar) {
        sidebars.add(sidebar);
    }

    protected static void unregisterSidebar(Sidebars sidebar) {
        sidebars.remove(sidebar);
    }

    public Sidebars getSidebar(Player player) {
        if(player == null) throw new SidebarException("Player can not be null!");

        Objective objective = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
        if(objective == null) return null;

        if(!sidebars.isEmpty()) {
            for (Sidebars sidebar : sidebars) {
                if(sidebar.getTitle().equals(objective.getDisplayName())) {
                    return sidebar;
                }
            }
        }
        return null;
    }
}
