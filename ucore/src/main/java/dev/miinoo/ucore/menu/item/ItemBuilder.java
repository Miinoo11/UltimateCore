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

public interface ItemBuilder {

	ItemBuilder setType(Material type);
	
	ItemBuilder setData(MaterialData data);
	
	ItemBuilder setAmount(int amount);
	
	ItemBuilder setDurability(short durability);
	
	ItemBuilder setDisplayName(String name);
	
	ItemBuilder setLore(List<String> lore);
	
	ItemBuilder setLore(String... lore);
	
	ItemBuilder addLore(List<String> lore);
	
	ItemBuilder addLore(String... lore);
	
	ItemBuilder setUnbreakable(boolean unbreakable);
	
	ItemBuilder addEnchantment(Enchantment enchantment, int level);

	ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments);

	ItemBuilder addXEnchantments(Map<XEnchantment, Integer> enchantments);

	ItemBuilder removeEnchantment(Enchantment enchantment);
	
	ItemBuilder addItemFlags(ItemFlag... flags);
	
	ItemBuilder removeItemFlags(ItemFlag... flags);
	
	ItemBuilder addGlow();

	ItemBuilder addGlow(boolean bool);
	
	ItemBuilder If(boolean condition, Consumer<ItemBuilder> action);
	
	ItemBuilder If(boolean condition, Consumer<ItemBuilder> action, Consumer<ItemBuilder> elseAction);
	
	@Deprecated
    ItemBuilder If(boolean condition);
	
	ItemStack getItem();

	
}
