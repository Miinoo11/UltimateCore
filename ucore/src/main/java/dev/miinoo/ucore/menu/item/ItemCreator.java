package dev.miinoo.ucore.menu.item;

/**
 * @author DotClass
 *
 */

import com.cryptomorin.xseries.XEnchantment;
import dev.miinoo.ucore.UCore;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

class ItemCreator implements ItemBuilder {

	private final ItemStack item;
	private final ItemMeta meta;

	ItemCreator(ItemStack item) {
		this.item = item;
		meta = item.getItemMeta();
	}

	public ItemBuilder setType(Material type) {
		item.setType(type);
		return this;
	}

	public ItemBuilder setData(MaterialData data) {
		item.setData(data);
		return this;
	}

	public ItemBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}

	public ItemBuilder setDurability(short durability) {
		item.setDurability(durability);
		return this;
	}

	public ItemBuilder setDisplayName(String name) {
		meta.setDisplayName(name);
		return this;
	}

	public ItemBuilder setLore(List<String> lore) {
		meta.setLore(lore);
		return this;
	}

	public ItemBuilder setLore(String... lore) {
		setLore(Arrays.asList(lore));
		return this;
	}

	@Override
	public ItemBuilder addLore(List<String> lore) {
		List<String> metaLore = meta.getLore();
		if (metaLore != null) {
			metaLore.addAll(lore);
			meta.setLore(metaLore);
		}
		return this;
	}

	@Override
	public ItemBuilder addLore(String... lore) {
		addLore(Arrays.asList(lore));
		return this;
	}

	public ItemBuilder setUnbreakable(boolean unbreakable) {
		//meta.setUnbreakable(unbreakable);
		return this;
	}

	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		meta.addEnchant(enchantment, level, true);
		return this;
	}

	public ItemBuilder addXEnchantments(Map<XEnchantment, Integer> enchantments) {
		for(XEnchantment enchantment : enchantments.keySet()) {
			meta.addEnchant(enchantment.parseEnchantment(), enchantments.get(enchantment), true);
		}
		return this;
	}

	public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
		for(Enchantment enchantment : enchantments.keySet()) {
			meta.addEnchant(XEnchantment.matchXEnchantment(enchantment).parseEnchantment(), enchantments.get(enchantment), true);
		}
		return this;
	}

	public ItemBuilder removeEnchantment(Enchantment enchantment) {
		meta.removeEnchant(enchantment);
		return this;
	}

	public ItemBuilder addItemFlags(ItemFlag... flags) {
		meta.addItemFlags(flags);
		return this;
	}

	public ItemBuilder removeItemFlags(ItemFlag... flags) {
		meta.removeItemFlags(flags);
		return this;
	}

	public ItemBuilder addGlow(boolean bool) {
		UCore.getWRAPPER().addGlow(bool);
		return this;
	}

	public ItemBuilder addGlow() {
		UCore.getWRAPPER().addGlow();
		return this;
	}

	@Override
	public ItemBuilder If(boolean condition, Consumer<ItemBuilder> action) {
		if (condition) {
			action.accept(this);
		}
		return this;
	}

	@Override
	public ItemBuilder If(boolean condition, Consumer<ItemBuilder> action, Consumer<ItemBuilder> elseAction) {
		if (condition) {
			action.accept(this);
		} else {
			elseAction.accept(this);
		}
		return this;
	}

	@Override
	@Deprecated
	public ItemBuilder If(boolean condition) {
		return condition ? this : new EmptyItemBuilder(this);
	}

	@Override
	public ItemStack getItem() {
		item.setItemMeta(meta);
		return item;
	}

}