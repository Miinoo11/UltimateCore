package dev.miinoo.ucore.menu.ui.gui;

/**
 * @author DotClass
 *
 */

import dev.miinoo.ucore.menu.ui.Dimension;
import dev.miinoo.ucore.menu.ui.ui.UIItem;
import dev.miinoo.ucore.menu.ui.ui.UIList;
import dev.miinoo.ucore.menu.ui.ui.UIPageable;
import dev.miinoo.ucore.menu.ui.ui.UIScrollBar;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIScrollBar extends GUIElement implements UIScrollBar {

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	private static void checkOrientation(int orientation) {
		switch (orientation) {
		case VERTICAL:
		case HORIZONTAL:
			break;
		default:
			throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
		}
	}

	private static void checkLength(int length) {
		if (length < 2) {
			throw new IllegalArgumentException("length must not be less than 2");
		}
	}

	private int orientation;
	private int length;
	private Dimension dimension;
	private UIPageable list;
	private UIItem[][] items;
	private OnClickListener listener;

	public GUIScrollBar(int orientation, int length, UIPageable list, UIItem last, UIItem next) {
		Validate.notNull(list);
		Validate.notNull(next);
		Validate.notNull(last);
		checkOrientation(orientation);
		checkLength(length);
		this.orientation = orientation;
		this.length = length;
		dimension = new Dimension(orientation == VERTICAL ? 1 : length, orientation == HORIZONTAL ? 1 : length);
		this.list = list;
		items = new UIItem[dimension.getHeight()][dimension.getWidth()];
		if (orientation == HORIZONTAL) {
			items[0][0] = last;
			items[0][length - 1] = next;
		} else {
			items[length - 1][0] = last;
			items[0][0] = next;
		}
	}

	public Dimension getSize() {
		return dimension;
	}

	public GUIScrollBar setOnClickListener(OnClickListener listener) {
		this.listener = listener;
		return this;
	}

	public boolean onClick(Player player, int x, int y, InventoryClickEvent event) {
		boolean rebuild = false;
		if (listener != null) {
			rebuild = listener.onClick(player, event);
		}
		UIItem item = items[y][x];
		if (item != null) {
			if (item.onClick(player, 0, 0, event)) {
				rebuild = true;
			}
		}
		if (orientation == VERTICAL) {
			if (y == 0 && x == 0) {
				return list.nextPage();
			}
			if (y == length - 1 && x == 0) {
				return list.lastPage();
			}
		} else {
			if (y == 0 && x == length - 1) {
				return list.nextPage();
			}
			if (y == 0 && x == 0) {
				return list.lastPage();
			}
		}
		return rebuild;
	}

	public UIItem[][] getItems() {
		return items;
	}

	public boolean isEnabled(Player player) {
		if (list instanceof UIList && ((UIList) list).getLength() <= list.getSize().getSize()) {
			return false;
		}
		return super.isEnabled(player);
	}

}
