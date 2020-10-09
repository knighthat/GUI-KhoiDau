package me.knighthat.GUIKhoiDau;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class Hooks {
	private static Economy econ = null;
	private static boolean tienTe = true;

	public static boolean vaultPlugin() {
		if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
			tienTe = false;
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			Bukkit.getServer().getConsoleSender().sendMessage(
					LinhTinh.chatMau("[GUIKhoiDau] &cKhông tìm thấy plugin quản lý tiền tệ (VD: Essentials)!"));
			Bukkit.getServer().getConsoleSender().sendMessage(
					LinhTinh.chatMau("[GUIKhoiDau] &cPlugin vẫn sẽ hoạt động nhưng không phát huy tối đa công dụng."));
			return true;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public static Economy getEconomy() {
		return econ;
	}

	public static boolean tienTe() {
		return tienTe;
	}
}
