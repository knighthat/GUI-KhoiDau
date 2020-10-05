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

	private static Map<String, Byte> items = new HashMap<String, Byte>();

	{
		items.put("DARK_OAK_", (byte) 1);
		items.put("ACACIA_", (byte) 0);
		items.put("JUNGLE_", (byte) 3);
		items.put("BIRCH_", (byte) 2);
		items.put("SPRUCE_", (byte) 1);
		items.put("OAK_", (byte) 0);
		items.put("STONE_BRICK_", (byte) 5);
		items.put("NETHER_BRICK_", (byte) 6);
		items.put("BRICK_", (byte) 4);
	}

	@SuppressWarnings("deprecation")
	public static ItemStack phanLoaiItems(String a) {
		String vatPham = a.toUpperCase();
		byte duLieu = 0;
		if (Material.getMaterial(vatPham) == null) {
			if (vatPham.contains("_SHOVEL"))
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
			if (vatPham.contains("_WALL")) {
				if (vatPham.contains("MOSSY_"))
					duLieu += 1;
				vatPham = "COBBLE_WALL";
			}
			if (vatPham.contains("_SLAB") && !vatPham.contains("_SLAB2")) {
				vatPham = "STEP";
				if (vatPham.contains("BRICK_")) {
					for (String i : items.keySet())
						if (vatPham.contains(i))
							duLieu = items.get(i);
				} else
					duLieu = (byte) (vatPham.contains("SMOOTH_STONE_") ? 0
							: (vatPham.contains("SANDSTONE_") ? 1 : (vatPham.contains("COBBLESTONE_") ? 3 : 7)));
			}
			if (vatPham.contains("GRANITE") || vatPham.contains("DIORITE") || vatPham.contains("ANDESITE")) {
				vatPham = "STONE";
				duLieu = (byte) (vatPham.contains("GRANITE") ? 1 : (vatPham.contains("DIORITE") ? 3 : 5));
				if (vatPham.contains("POLISHED_"))
					duLieu += 1;
			}
			if (vatPham.contains("_LOG") || vatPham.contains("_PLANKS") || vatPham.contains("_SLAB")) {
				for (String i : items.keySet())
					if (vatPham.contains(i))
						duLieu = items.get(i);
				vatPham = (vatPham.contains("_PLANK") ? "WOOD"
						: (vatPham.contains("_SLAB") ? "WOOD_STEP"
								: (vatPham.contains("ACACIA_") || vatPham.contains("DARK_OAK_") ? "LOG_2" : "LOG")));
			}
			for (String i : Arrays.stream(DyeColor.values()).map(DyeColor::name).collect(Collectors.toList())) {
				if (vatPham.replace("LIGHT_GRAY", "SILVER").startsWith(i)) {
					vatPham = vatPham.replace(i.replace("SILVER", "LIGHT_GRAY").concat("_"), "");
					duLieu = DyeColor.valueOf(i).getData();
				}
			}
		}
		if (Material.getMaterial(vatPham) == null)
			return new ItemStack(Material.AIR);
		return new ItemStack(Material.matchMaterial(vatPham), 1, (byte) duLieu);
	}

	public static Enchantment phanLoaiEnchant(String a) {
		if (Enchantment.getByName(a.toUpperCase()) == null)
			switch (a.toUpperCase()) {
			case "POWER":
				a = "ARROW_DAMAGE";
				break;
			case "FLAME":
				a = "ARROW_FIRE";
				break;
			case "INFINITY":
				a = "ARROW_INFINITY";
				break;
			case "PUNCH":
				a = "ARROW_KNOCKBACK";
				break;
			case "SHAPRNESS":
				a = "DAMAGE_ALL";
				break;
			case "BANE_OF_ARTHROPODS":
				a = "DAMAGE_ARTHROPODS";
				break;
			case "SMITE":
				a = "DAMAGE_UNDEAD";
				break;
			case "EFFICIENCY":
				a = "DIG_SPEED";
				break;
			case "FORTUNE":
				a = "LOOT_BONUS_BLOCKS";
				break;
			case "LOOTING":
				a = "LOOT_BONUS_MOBS";
				break;
			case "RESPIRATION":
				a = "OXYGEN";
				break;
			case "PROTECTION":
				a = "PROTECTION_ENVIRONMENTAL";
				break;
			case "BLAST_PROTECTION":
				a = "PROTECTION_EXPLOSIONS";
				break;
			case "FEATHER_FALLING":
				a = "PROTECTION_FALL";
				break;
			case "FIRE_PROTECTION":
				a = "PROTECTION_FIRE";
				break;
			case "PROJECILE_PROTECTION":
				a = "PROTECTION_PROJECTILE";
				break;
			case "AQUA_AFFINITY":
				a = "WATER_WORKER";
				break;
			case "UNBREAKING":
				a = "DURABILITY";
				break;
			}
		return Enchantment.getByName(a.toUpperCase());
	}
}
