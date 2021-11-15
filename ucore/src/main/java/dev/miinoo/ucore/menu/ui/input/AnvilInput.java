package dev.miinoo.ucore.menu.ui.input;

import com.cryptomorin.xseries.XMaterial;
import dev.miinoo.ucore.UCore;
import dev.miinoo.ucore.menu.AnvilUI;
import dev.miinoo.ucore.menu.item.Items;
import dev.miinoo.ucore.menu.ui.gui.GUIItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public final class AnvilInput extends GUIInput<String> {

	AnvilInput() {
		super("anvil");
	}

	@Override
	void get(Player player, String text, Consumer<String> input) {
		//AnvilGUI gui = new AnvilGUI(player);
		AnvilUI gui = (AnvilUI) UCore.getWRAPPER().createAnvilUI(player);
		gui.setItem(0, new GUIItem(Items.createItem(XMaterial.PAPER.parseMaterial())
				.setDisplayName(text == null ? "Input text" : text).getItem()));
		gui.setItem(2, new GUIItem(Items.createItem(XMaterial.BARRIER.parseMaterial()).setDisplayName("§4X").getItem())
				.setOnClickListener((player2, item2, event) -> {
					ItemStack item = event.getClickedInventory().getItem(2);
					if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
							&& !item.getItemMeta().getDisplayName().trim().isEmpty()) {
						player.closeInventory();
						input.accept(item.getItemMeta().getDisplayName());
					}
					return false;
				}));
		gui.open();
	}

}