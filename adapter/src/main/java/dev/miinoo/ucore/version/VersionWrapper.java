package dev.miinoo.ucore.version;

import org.bukkit.entity.Player;

public interface VersionWrapper {

    /**
     * @param player the anvilui is created for
     * @return AnvilUI
     * @see dev.miinoo.ucore.menu.AnvilUI
     */
    Object createAnvilUI(Player player);

    /**
     * @param player the tablist is displayed to
     * @param header string that is displayed at the top of the player tab list
     * @param footer string that is displayed below the player tab list
     */
    void sendTabListHeaderFooter(Player player, String header, String footer);

    /**
     * @param show
     * @return ItemBuilder
     */
    Object addGlow(boolean show);

    /**
     *
     * @return ItemBuilder
     */
    Object addGlow();
}
