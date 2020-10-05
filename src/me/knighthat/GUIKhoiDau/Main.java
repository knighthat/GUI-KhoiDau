package me.knighthat.GUIKhoiDau;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	CaiDat caiDat = new CaiDat(this);
	Map<String, FileConfiguration> tepTin = new HashMap<String, FileConfiguration>();
	Map<String, FileConfiguration> tenTepTin = new HashMap<String, FileConfiguration>();

	@Override
	public void onEnable() {
		new CaiDat(this);
		napGUIs();
		getCommand("guikhoidau").setExecutor(new CauLenh(this));
		getCommand("guikhoidau").setTabCompleter(new CauLenh(this));
		getServer().getPluginManager().registerEvents(new TaoInventory(this), this);
		if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
			getServer().getConsoleSender()
					.sendMessage(LinhTinh.chatMau("[GUIKhoiDau] &bĐã liên kết với plugin PlaceholderAPI"));
		} else
			getServer().getConsoleSender().sendMessage(
					LinhTinh.chatMau("[GUIKhoiDau] &cKhông tìm thấy plugin PlaceHolderAPI! Liên kết thất bại."));
		if (tienTe()) {
			getServer().getConsoleSender().sendMessage(LinhTinh.chatMau("[GUIKhoiDau] &eĐã liên kết với plugin Vault"));
		} else
			getServer().getConsoleSender()
					.sendMessage(LinhTinh.chatMau("[GUIKhoiDau] &cKhông tìm thấy plugin Vault! Liên kết thất bại."));
		getServer().getConsoleSender().sendMessage(LinhTinh.chatMau("[GUIKhoiDau] &aHoàn tất quá trình khởi động!"));
	}

	public void napGUIs() {
		if (new File(getDataFolder().getAbsolutePath() + File.separator + "menus").list().length < 1) {
			String[] menuMau = { "example.yml", "MenuChinh.yml", "VuKhi.yml", "Giap.yml", "VoDung.yml" };
			for (String i : menuMau)
				saveResource("menus/" + i, false);
		}
		for (String a : new File(getDataFolder().getAbsolutePath() + File.separator + "menus").list()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "menus/" + a));
			if (config.contains("Cau_Lenh")) {
				tepTin.put(config.getString("Cau_Lenh"), config);
				tenTepTin.put(a, config);
			}
		}
	}

	private Economy econ = null;

	public Economy getEconomy() {
		return econ;
	}

	public boolean tienTe() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			getServer().getConsoleSender().sendMessage(
					LinhTinh.chatMau("[GUIKhoiDau] &cKhông tìm thấy plugin quản lý tiền tệ (VD: Essentials)!"));
			getServer().getConsoleSender().sendMessage(
					LinhTinh.chatMau("[GUIKhoiDau] &cPlugin vẫn sẽ hoạt động nhưng không phát huy tối đa công dụng."));
			return true;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

}
