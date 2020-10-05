package me.knighthat.GUIKhoiDau;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CaiDat {

	private Main plugin;

	public CaiDat(Main plugin) {
		this.plugin = plugin;
		khoiDong();
	}

	private FileConfiguration config;
	private File tep;

	public void khoiDong() {
		if (tep == null)
			tep = new File(plugin.getDataFolder(), "caidat.yml");
		if (!tep.exists())
			plugin.saveResource("caidat.yml", false);
	}

	public void taiLai() {
		if (tep == null)
			tep = new File(plugin.getDataFolder(), "caidat.yml");
		config = YamlConfiguration.loadConfiguration(tep);
		InputStream duLieu = plugin.getResource("caidat.yml");
		if (duLieu != null)
			config.setDefaults(
					YamlConfiguration.loadConfiguration(new InputStreamReader(duLieu, Charset.forName("UTF8"))));
	}

	public FileConfiguration lay() {
		if (config == null)
			taiLai();
		return config;
	}

	public void luu() {
		if (tep == null || config == null)
			return;

		try {
			config.save(tep);
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage(LinhTinh.chatMau("&cKhông thể lưu" + tep));
		}
	}

	public void kiemTra() {
		if (lay().getString("Phien-Ban").equals(plugin.getDescription().getVersion())) {
			tep.renameTo(new File(plugin.getDataFolder(), "catdat.yml.old"));
			tep.delete();
			plugin.saveResource("catdat.yml", false);
		}
	}
}
