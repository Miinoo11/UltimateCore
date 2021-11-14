package dev.miinoo.ucore.version;

/**
 * @author DotClass
 *
 */

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface UIField<E> extends UIElement {

	E get(int x, int y);

	UIItem getItem(int x, int y);

	UIField set(int x, int y, E element);

	interface OnClickListener<E> {

		boolean onClick(Player player, UIField<E> field, E element, InventoryClickEvent event);

	}

}