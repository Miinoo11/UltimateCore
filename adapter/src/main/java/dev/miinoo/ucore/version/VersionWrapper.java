package dev.miinoo.ucore.version;

import org.bukkit.entity.Player;

public interface VersionWrapper {

    Object createAnvilUI(Player player);

    void sendTabListHeaderFooter(Player player, String header, String footer);

    Object addGlow(boolean bool);

    Object addGlow();
}
