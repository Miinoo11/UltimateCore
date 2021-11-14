package dev.miinoo.ucore.menu.ui.gui;

import dev.miinoo.ucore.menu.ui.Dimension;
import dev.miinoo.ucore.menu.ui.ui.UIField;
import dev.miinoo.ucore.menu.ui.ui.UIItem;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Function;

/**
 * @author DotClass
 */

public class GUIField<E> extends GUIElement implements UIField<E> {

	private Dimension dimension;
	private Function<E, UIItem> converter;
	private Object[][] elements;
	private UIItem[][] items;
	private UIField.OnClickListener<E> listener;

	public GUIField(int width, int height, Function<E, UIItem> converter) {
		Validate.notNull(converter);
		dimension = new Dimension(width, height);
		this.converter = converter;
		elements = new Object[height][width];
		items = new UIItem[height][width];
	}

	@Override
	public Dimension getSize() {
		return dimension;
	}

	public GUIField<E> setOnClickListener(UIField.OnClickListener<E> listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public boolean onClick(Player player, int x, int y, InventoryClickEvent event) {
		boolean rebuild = false;
		E element = get(x, y);
		if (listener != null) {
			rebuild = listener.onClick(player, this, element, event);
		}
		UIItem item = getItem(x, y);
		if (!rebuild && item != null) {
			rebuild = item.onClick(player, 0, 0, event);
		}
		if (rebuild) {
			items[y][x] = converter.apply(element);
		}
		return rebuild;
	}

	@Override
	public UIItem[][] getItems() {
		return items;
	}

	@Override
	public E get(int x, int y) {
		return (E) elements[y][x];
	}

	@Override
	public UIItem getItem(int x, int y) {
		return items[y][x];
	}

	@Override
	public GUIField set(int x, int y, E element) {
		elements[y][x] = element;
		items[y][x] = converter.apply(element);
		return this;
	}

	public GUIField<E> setElement(int x, int y, E element) {
		set(x, y, element);
		return this;
	}

	public GUIField<E> fill(int x1, int y1, int x2, int y2, E element) {
		for (int x = x1; x < x2; x++) {
			for (int y = y1; y < y2; y++) {
				set(x, y, element);
			}
		}
		return this;
	}

	public GUIField<E> fill(int x1, int x2, E element) {
		return fill(x1, 0, x2, dimension.getHeight(), element);
	}

	public GUIField<E> fill(E element) {
		return fill(0, 0, dimension.getWidth(), dimension.getHeight(), element);
	}

}