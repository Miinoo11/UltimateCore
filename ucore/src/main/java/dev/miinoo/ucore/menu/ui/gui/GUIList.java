package dev.miinoo.ucore.menu.ui.gui;

/**
 * @author DotClass
 *
 */

import dev.miinoo.ucore.menu.ui.Dimension;
import dev.miinoo.ucore.menu.ui.ui.UIItem;
import dev.miinoo.ucore.menu.ui.ui.UIList;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GUIList<E> extends GUIElement implements UIList<E> {

	private Dimension dimension;
	private Function<E, UIItem> converter;
	private int size;
	private List<E> list;
	private int page;
	private UIList.OnClickListener<E> listener;

	public GUIList(int width, int height, Collection<E> elements, Function<E, UIItem> converter) {
		Validate.notNull(elements);
		Validate.notNull(converter);
		dimension = new Dimension(width, height);
		this.converter = converter;
		size = width * height;
		list = new ArrayList<>(elements);
	}

	public GUIList(int width, int height, Function<E, UIItem> function) {
		this(width, height, new ArrayList<>(), function);
	}

	public Dimension getSize() {
		return dimension;
	}

	public GUIList<E> setOnClickListener(UIList.OnClickListener<E> listener) {
		this.listener = listener;
		return this;
	}

	public boolean onClick(Player player, int x, int y, InventoryClickEvent event) {
		int index = y * dimension.getWidth() + x + page * size;
		E element = getElement(index);
		if (element != null) {
			if (listener != null) {
				return listener.onClick(player, this, index, element, event);
			}
			UIItem item = converter.apply(element);
			if (item != null) {
				return item.onClick(player, 0, 0, event);
			}
		}
		return false;
	}

	public UIItem[][] getItems() {
		List<UIItem> items;
		if (list.size() > size) {
			int from = page * size;
			int to = from + size;
			if (to > list.size()) {
				to = list.size();
			}
			items = list.subList(from, to).stream().map(converter).collect(Collectors.toList());
		} else {
			items = new ArrayList<>(list.stream().map(converter).collect(Collectors.toList()));
		}
		UIItem[][] uiitems = new UIItem[dimension.getHeight()][dimension.getWidth()];
		for (int i = 0; i < dimension.getHeight(); i++) {
			UIItem[] u = new UIItem[dimension.getWidth()];
			for (int j = 0; j < dimension.getWidth(); j++) {
				int index = i * dimension.getWidth() + j;
				if (index >= items.size()) {
					break;
				}
				u[j] = items.get(index);
			}
			uiitems[i] = u;
		}
		return uiitems;
	}
	
	public int getLength() {
		return list.size();
	}
	
	public int getPage() {
		return page;
	}

	public boolean setPage(int page) {
		if (page < 0 || page > list.size() / size - (list.size() % size == 0 ? 1 : 0)) {
			return false;
		}
		this.page = page;
		return true;
	}

	public boolean nextPage() {
		return setPage(page + 1);
	}

	public boolean lastPage() {
		return setPage(page - 1);
	}

	public List<E>[] getPages() {
		List<E>[] pages = new List[list.size() / size + list.size() % size == 0 ? 1 : 0];
		for (int i = 0; i < pages.length; i++) {
			pages[i] = getElements(i);
		}
		return pages;
	}

	public List<E> getElements(int page) {
		if (list.size() > size) {
			int from = page * size;
			int to = from + size;
			if (from > list.size()) {
				from = list.size();
			}
			return list.subList(from, to);
		}
		return new ArrayList<>(list);
	}

	public List<E> getElements() {
		return getElements(page);
	}

	public E getElement(int index) {
		if (index >= 0 && index < list.size()) {
			return list.get(index);
		}
		return null;
	}

	public E getElement(int x, int y) {
		return getElements().get(y * dimension.getWidth() + x + page * size);
	}

	public boolean addElement(E element) {
		return list.add(element);
	}

	public boolean addElements(List<E> elements) {
		return list.addAll(elements);
	}

	public E setElement(int index, E element) {
		return list.set(index, element);
	}

	public void setElements(List<E> elements) {
		list = elements;
	}
	
}
