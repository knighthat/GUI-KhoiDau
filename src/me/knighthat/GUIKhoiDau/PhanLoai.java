package me.knighthat.GUIKhoiDau;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class PhanLoai {

	private static Map<String, Integer> items = new HashMap<String, Integer>();
	private static Map<String, String> enchantments = new HashMap<String, String>();

	public static boolean checkMaterial(String material) {
		return Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList())
				.contains(material.toUpperCase());
	}

	static {
		items.put("DARK_OAK_", 1);
		items.put("ACACIA_", 0);
		items.put("JUNGLE_", 3);
		items.put("BIRCH_", 2);
		items.put("SPRUCE_", 1);
		items.put("OAK_", 0);
		items.put("COBBLESTONE_", 3);
		items.put("STONE_BRICK_", 5);
		items.put("NETHER_BRICK_", 6);
		items.put("BRICK_", 4);
		enchantments.put("POWER", "ARROW_DAMAGE");
		enchantments.put("FLAME", "ARROW_FIRE");
		enchantments.put("INFINITY", "ARROW_INFINITY");
		enchantments.put("PUNCH", "ARROW_KNOCKBACK");
		enchantments.put("SHARPNESS", "DAMAGE_ALL");
		enchantments.put("BANE_OF_ARTHROPODS", "DAMAGE_ARTHROPODS");
		enchantments.put("SMITE", "DAMAGE_UNDEAD");
		enchantments.put("EFFICIENCY", "DIG_SPEED");
		enchantments.put("FORTUNE", "LOOT_BONUS_BLOCKS");
		enchantments.put("LOOTING", "LOOT_BONUS_MOBS");
		enchantments.put("RESPIRATION", "OXYGEN");
		enchantments.put("PROTECTION", "PROTECTION_ENVIRONMENTAL");
		enchantments.put("BLAST_PROTECTION", "PROTECTION_EXPLOSIONS");
		enchantments.put("FEATHER_FALLING", "PROTECTIONG_FALL");
		enchantments.put("FIRE_PROTECTION", "PROTECTION_FIRE");
		enchantments.put("PROJECTILE_PROTECTION", "PROTECTION_PROJECTILE");
		enchantments.put("AQUA_AFFINITY", "WATER_WORKER");
		enchantments.put("UNBREAKING", "DURABILITY");
	}

	public static Integer checkInteger(int chuSo, int gioiHan) {
		if (chuSo >= 0 && chuSo <= gioiHan)
			return chuSo;
		return 1;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack phanLoaiItems(String a) {
		String vatPham = a.toUpperCase();
		int duLieu = 0;
		if (!checkMaterial(vatPham)) {
			if (vatPham.endsWith("_SHOVEL"))
				vatPham = vatPham.replace("_SHOVEL", "_SPADE");
			if (vatPham.startsWith("GOLDEN_"))
				vatPham = vatPham.replace("GOLDEN_", "GOLD_");
			if (vatPham.startsWith("WOODEN_"))
				vatPham = vatPham.replace("WOODEN_", "WOOD_");
			if (vatPham.equals("BEEF"))
				vatPham = "RAW_BEEF";
			if (vatPham.contains("TERRACOTTA"))
				vatPham = vatPham.replace("TERRACOTTA", "STAINED_CLAY");
			if (vatPham.equals("RED_SANDSTONE_SLAB"))
				vatPham = "STONE_SLAB2";
			if (vatPham.endsWith("_WALL")) {
				if (vatPham.contains("MOSSY_"))
					duLieu += 1;
				vatPham = "COBBLE_WALL";
			}
			if (vatPham.endsWith("_SLAB")) {
				if (items.keySet().contains(vatPham)) {
					for (String i : items.keySet())
						if (vatPham.contains(i))
							duLieu = items.get(i);
					vatPham = vatPham.contains("BTICK_") ? "STEP" : "WOOD_STEP";
				} else if (vatPham.contains("STONE_")) {
					duLieu = (byte) (vatPham.contains("SMOOTH_STONE_") ? 0
							: (vatPham.contains("SANDSTONE_") ? 1 : (vatPham.contains("COBBLESTONE_") ? 3 : 7)));
					vatPham = "STEP";
				}
			}
			if (vatPham.endsWith("GRANITE") || vatPham.endsWith("DIORITE") || vatPham.endsWith("ANDESITE")) {
				duLieu = (byte) (vatPham.contains("GRANITE") ? 1 : (vatPham.contains("DIORITE") ? 3 : 5));
				if (vatPham.startsWith("POLISHED_"))
					duLieu += 1;
				vatPham = "STONE";
			}
			if (vatPham.endsWith("_LOG") || vatPham.endsWith("_PLANKS")) {
				for (String i : items.keySet())
					if (vatPham.contains(i))
						duLieu = items.get(i);
				vatPham = (vatPham.endsWith("_PLANK") ? "WOOD"
						: (vatPham.startsWith("ACACIA_") || vatPham.startsWith("DARK_OAK_") ? "LOG_2" : "LOG"));
			}
			for (DyeColor i : DyeColor.values())
				if (vatPham.replace("LIGHT_GRAY", "SILVER").startsWith(i.toString()))
					return new ItemStack(
							Material.getMaterial(
									vatPham.replace(i.toString().replace("SILVER", "LIGHT_GRAY").concat("_"), "")),
							1, (byte) DyeColor.valueOf(i.toString()).getData());
		}
		if (!checkMaterial(vatPham))
			vatPham = "AIR";
		return new ItemStack(Material.getMaterial(vatPham), 1, (byte) duLieu);

	}

	public static Enchantment phanLoaiEnchantments(String a) {
		if (!Arrays.stream(Enchantment.values()).map(enchant -> enchant.getName()).collect(Collectors.toList())
				.contains(a.toUpperCase()))
			for (String i : enchantments.keySet())
				if (a.toUpperCase().equals(i))
					return Enchantment.getByName(enchantments.get(i));
		return Enchantment.getByName(a.toUpperCase());
	}
}
