package me.knighthat.GUIKhoiDau;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

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

	public static TextComponent hoverAndClick(String modified, @Nullable String color, @Nullable String hoverEvent,
			@Nullable String clickEvent) {
		TextComponent doanKyTu = new TextComponent(modified);
		if (!color.equals(null))
			doanKyTu.setColor(net.md_5.bungee.api.ChatColor.valueOf(color.toUpperCase()));
		if (!hoverEvent.equals(null))
			doanKyTu.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder(hoverEvent).color(net.md_5.bungee.api.ChatColor.AQUA).italic(true).create()));
		if (!clickEvent.equals(null))
			doanKyTu.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickEvent));
		return doanKyTu;
	}
}
