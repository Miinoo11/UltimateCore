package dev.miinoo.ucore.version;

/**
 * @author DotClass
 *
 */

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public interface UIList<E> extends UIPageable, UIElement {

	int getLength();
	
	List<E>[] getPages();
	
	List<E> getElements(int page);
	
	List<E> getElements();
	
	E getElement(int index);
	
	E getElement(int x, int y);
	
	boolean addElement(E element);
	
	boolean addElements(List<E> elements);
	
	E setElement(int index, E element);
	
	interface OnClickListener<E> {
		
		boolean onClick(Player player, UIList<E> list, int index, E element, InventoryClickEvent event);
		
	}
	
}
