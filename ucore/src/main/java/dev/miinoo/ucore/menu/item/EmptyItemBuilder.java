package dev.miinoo.ucore.menu.item;

/**
 * @author DotClass
 *
 */

import com.cryptomorin.xseries.XEnchantment;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

final class EmptyItemBuilder implements ItemBuilder {

	private final ItemBuilder builder;
	
	EmptyItemBuilder(ItemBuilder builder) {
		this.builder = builder;
	}
	
	@Override
	public ItemBuilder setType(Material type) {
		return builder;
	}

	@Override
	public ItemBuilder setData(MaterialData data) {
		return builder;
	}

	@Override
	public ItemBuilder setAmount(int amount) {
		return builder;
	}

	@Override
	public ItemBuilder setDurability(short durability) {
		return builder;
	}

	@Override
	public ItemBuilder setDisplayName(String name) {
		return builder;
	}

	@Override
	public ItemBuilder setLore(List<String> lore) {
		return builder;
	}

	@Override
	public ItemBuilder setLore(String... lore) {
		return builder;
	}

	@Override
	public ItemBuilder addLore(List<String> lore) {
		return builder;
	}

	@Override
	public ItemBuilder addLore(String... lore) {
		return builder;
	}

	@Override
	public ItemBuilder setUnbreakable(boolean unbreakable) {
		return builder;
	}

	@Override
	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		return builder;
	}

	@Override
	public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
		return builder;
	}

	@Override
	public ItemBuilder addXEnchantments(Map<XEnchantment, Integer> enchantments) {
		return builder;
	}

	@Override
	public ItemBuilder removeEnchantment(Enchantment enchantment) {
		return builder;
	}

	@Override
	public ItemBuilder addItemFlags(ItemFlag... flags) {
		return builder;
	}

	@Override
	public ItemBuilder removeItemFlags(ItemFlag... flags) {
		return builder;
	}

	@Override
	public ItemBuilder addGlow() {
		return builder;
	}

	@Override
	public ItemBuilder addGlow(boolean bool) {
		return builder;
	}

	@Override
	public ItemBuilder If(boolean condition, Consumer<ItemBuilder> action) {
		return this;
	}

	@Override
	public ItemBuilder If(boolean condition, Consumer<ItemBuilder> action, Consumer<ItemBuilder> elseAction) {
		return this;
	}
	
	@Override
	public ItemBuilder If(boolean condition) {
		return this;
	}

	@Override
	public ItemStack getItem() {
		return builder.getItem();
	}

}
