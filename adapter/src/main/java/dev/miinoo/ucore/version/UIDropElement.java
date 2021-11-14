package dev.miinoo.ucore.version;

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
