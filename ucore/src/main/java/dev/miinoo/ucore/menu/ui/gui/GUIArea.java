package dev.miinoo.ucore.menu.ui.gui;

/**
 * @author DotClass
 *
 */

import dev.miinoo.ucore.menu.Dimension;
import dev.miinoo.ucore.menu.UIField;
import dev.miinoo.ucore.menu.UIItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIArea extends GUIElement implements UIField<UIItem> {

	private Dimension dimension;
	private UIItem[][] items;
	private UIField.OnClickListener<UIItem> listener;

	public GUIArea(int width, int height) {
		dimension = new Dimension(width, height);
		items = new UIItem[height][width];
	}

	@Override
	public Dimension getSize() {
		return dimension;
	}

	public GUIArea setOnClickListener(UIField.OnClickListener<UIItem> listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public boolean onClick(Player player, int x, int y, InventoryClickEvent event) {
		boolean rebuild = false;
		UIItem item = get(x, y);
		if (item != null) {
			if (listener != null) {
				rebuild = listener.onClick(player, this, item, event);
			}
			if (item.onClick(player, x, y, event)) {
				return true;
			}
		}
		return rebuild;
	}

	@Override
	public UIItem[][] getItems() {
		return items;
	}

	@Override
	public UIItem get(int x, int y) {
		return items[y][x];
	}

	@Override
	public UIItem getItem(int x, int y) {
		return items[y][x];
	}

	@Override
	public GUIArea set(int x, int y, UIItem item) {
		items[y][x] = item;
		return this;
	}

	public GUIArea fill(int x1, int y1, int x2, int y2, UIItem item) {
		for (int x = x1; x < x2; x++) {
			for (int y = y1; y < y2; y++) {
				set(x, y, item);
			}
		}
		return this;
	}

	public GUIArea fill(UIItem item) {
		return fill(0, 0, dimension.getWidth(), dimension.getHeight(), item);
	}

}