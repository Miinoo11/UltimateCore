package dev.miinoo.ucore.menu.ui.ui;

/**
 * @author DotClass
 *
 */
import org.bukkit.inventory.ItemStack;

public interface UIDropElement extends UIElement {

	UIDropElement setFilter(DropFilter filter);
	
	@FunctionalInterface
	interface DropFilter {
		
		boolean accept(ItemStack cursor);
		
	}
	
}
