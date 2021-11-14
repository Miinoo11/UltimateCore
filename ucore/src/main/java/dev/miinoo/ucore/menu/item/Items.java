package dev.miinoo.ucore.menu.item;

/**
 * @author DotClass
 *
 */

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class Items {

	public static ItemBuilder createItem(Material type) {
		return new ItemCreator(new ItemStack(type));
	}

	public static ItemBuilder createItem(Material type, int data) {
		return new ItemCreator(new ItemStack(type, 1, (short) data));
	}

	public static ItemBuilder createItem(ItemStack item) {
		return new ItemCreator(item.clone());
	}

	public static ItemBuilder edit(ItemStack item) {
		return new ItemCreator(item);
	}

	public static ItemBuilder createBackArrow() {
		ItemStack item = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial());
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner("MHF_ArrowLeft");
		item.setItemMeta(meta);
		return new ItemCreator(item);
	}

	public static ItemBuilder createSkull(String owner) {
		if (owner.length() > 16) {
			ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
			ItemMeta meta = (SkullMeta) head.getItemMeta();
			SkullUtils.applySkin(meta, owner);
			head.setItemMeta(meta);
			return new ItemCreator(head);
		}
		ItemStack item = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial());
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(owner);
		item.setItemMeta(meta);
		return new ItemCreator(item);
	}

	public static ItemBuilder createHead(String owner) {
		if (owner.length() > 16) {
			return new ItemCreator(getHead(owner));
		}
		ItemStack item = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial());
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(owner);
		item.setItemMeta(meta);
		return new ItemCreator(item);
	}

	public static int addItem(Inventory inventory, Location location, ItemStack item) {
		if (item.getAmount() <= 0)
			return 0;
		int drop = 0;
		for (ItemStack item2 : inventory.addItem(item).values()) {
			location.getWorld().dropItem(location, item2);
			drop++;
		}
		return drop;
	}

	public static int addItem(Player player, ItemStack item) {
		return addItem(player.getInventory(), player.getLocation(), item);
	}

	public static int addItems(Inventory inventory, Location location, Iterable<ItemStack> items) {
		int i = 0;
		for (ItemStack item : items) {
			if (addItem(inventory, location, item) > 0)
				i++;
		}
		return i;
	}

	public static int addItems(Player player, Iterable<ItemStack> items) {
		return addItems(player.getInventory(), player.getLocation(), items);
	}

	public static boolean removeItems(Player player, int amount) {
		if (player.getGameMode() == GameMode.CREATIVE)
			return true;
		ItemStack item = player.getInventory().getItemInHand();
		if (item == null || item.getType() == XMaterial.AIR.parseMaterial())
			return false;
		int i = item.getAmount();
		int a = i - amount;
		if (a > 0)
			item.setAmount(a);
		else if (a == 0)
			player.getInventory().setItemInHand(new ItemStack(XMaterial.AIR.parseMaterial()));
		else
			return false;
		return true;
	}

	public static boolean removeItem(Player player) {
		return removeItems(player, 1);
	}

	public static void remove(Player player) {
		if (player.getGameMode() != GameMode.CREATIVE) {
			player.setItemInHand(new ItemStack(XMaterial.AIR.parseMaterial()));
		}
	}

	public static boolean damage(Player player, ItemStack item, short damage) {
		if (player != null && player.getGameMode() == GameMode.CREATIVE) {
			return true;
		}
		item.setDurability((short) (item.getDurability() + damage));
		if (item.getDurability() > item.getType().getMaxDurability()) {
			return false;
		}
		return true;
	}

	public static boolean damage(ItemStack item, short damage) {
		return damage(null, item, damage);
	}

	public static ItemStack getHead(String value) {
		GameProfile profile = new GameProfile(UUID.randomUUID(), "Steve");
		PropertyMap propertyMap = profile.getProperties();
		if (propertyMap == null) {
			propertyMap = new PropertyMap();
		}
		propertyMap.put("textures", new Property("textures", value));
		ItemStack head = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial());
		ItemMeta headMeta = head.getItemMeta();
		Class<?> headMetaClass = headMeta.getClass();
		try {
			Field field = headMetaClass.getDeclaredField("profile");
			field.setAccessible(true);
			field.set(headMeta, profile);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

		head.setItemMeta(headMeta);
		return head;
	}

	public static boolean equals(ItemStack item1, ItemStack item2) {
		if (item1 == null && item2 == null) {
			return true;
		}
		if (item1 == null || item2 == null) {
			return false;
		}
		return item1.getType() == item2.getType() && item1.hasItemMeta() == item2.hasItemMeta()
				&& (item1.hasItemMeta() ? Bukkit.getItemFactory().equals(item1.getItemMeta(), item2.getItemMeta())
						: true);
	}

	public static int stackItems(Inventory inventory) {
		ItemStack[] contents = inventory.getContents();
		int changed = 0;
		for (int i = 0; i < contents.length; i++) {
			ItemStack item = contents[i];
			if (item != null) {
				int required = 64 - item.getAmount();
				for (int j = i + 1; j < contents.length && required > 0; j++) {
					ItemStack stack = contents[j];
					if (item.isSimilar(stack)) {
						int amount = stack.getAmount();
						if ((amount = stack.getAmount()) > required) {
							stack.setAmount(amount - required);
							item.setAmount(64);
							changed++;
							break;
						}
						required -= amount;
						item.setAmount(64 - required);
						contents[j] = null;
						changed++;
					}
				}
			}
		}
		if (changed != 0) {
			inventory.setContents(contents);
		}
		return changed;
	}

	public static int repairItems(Inventory inventory) {
		int changed = 0;
		for (ItemStack item : inventory.getContents()) {
			if (item != null && item.getDurability() > 0) {
				item.setDurability((short) 0);
				changed++;
			}
		}
		if (inventory instanceof PlayerInventory) {
			for (ItemStack item : ((PlayerInventory) inventory).getArmorContents()) {
				if (item != null && item.getDurability() > 0) {
					item.setDurability((short) 0);
					changed++;
				}
			}
		}
		return changed;
	}

}
