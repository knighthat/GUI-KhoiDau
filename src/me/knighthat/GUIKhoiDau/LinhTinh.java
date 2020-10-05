package me.knighthat.GUIKhoiDau;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

public class LinhTinh {

	public static String chatMau(String a) {
		return ChatColor.translateAlternateColorCodes('&', a);
	}

	public static String boMau(String a) {
		return ChatColor.stripColor(a);
	}

	public static String papi(Player player, String a) {
		return PlaceholderAPI.setPlaceholders(player, chatMau(a));
	}

	public static <K, V> K layKey(Map<K, V> map, V value) {
		return map.entrySet().stream().filter(nhapVao -> value.equals(nhapVao.getValue())).map(Map.Entry::getKey)
				.findFirst().get();
	}
}
