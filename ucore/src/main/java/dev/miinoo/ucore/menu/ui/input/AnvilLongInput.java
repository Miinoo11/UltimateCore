package dev.miinoo.ucore.menu.ui.input;

/**
 * @author DotClass
 *
 */

import com.cryptomorin.xseries.XMaterial;
import dev.miinoo.ucore.UCore;
import dev.miinoo.ucore.menu.item.Items;
import dev.miinoo.ucore.menu.ui.gui.GUIItem;
import dev.miinoo.ucore.menu.ui.ui.AnvilUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class AnvilLongInput extends GUIInput<Long> {

	AnvilLongInput() {
		super("anvilLong");
	}

	@Override
	void get(Player player, Long value, Consumer<Long> input) {
		AnvilUI gui = UCore.getWRAPPER().createAnvilUI(player);
		gui.setItem(0, new GUIItem(Items.createItem(XMaterial.PAPER.parseMaterial())
				.setDisplayName(value == null ? "Zahl eingeben" : value.toString()).getItem()));
		gui.setItem(2, new GUIItem(Items.createItem(XMaterial.BARRIER.parseMaterial()).setDisplayName("§4❌").getItem())
				.setOnClickListener((player2, item2, event) -> {
					ItemStack item = event.getClickedInventory().getItem(2);
					if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
							&& !item.getItemMeta().getDisplayName().trim().isEmpty()) {
						try{
							long amount = Long.valueOf(item.getItemMeta().getDisplayName());
							player.closeInventory();
							input.accept(amount);
						} catch(NumberFormatException exception) {
							player.sendMessage("§cDu musst eine gültige Zahl angeben");
						}
					}
					return false;
				}));
		gui.open();
	}

}