package me.knighthat.GUIKhoiDau;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CaiDat {

	private static FileConfiguration config;
	private static File tep;

	public static void khoiDong() {
		if (tep == null)
			tep = new File(Main.instance.getDataFolder(), "caidat.yml");
		if (!tep.exists())
			Main.instance.saveResource("caidat.yml", false);
	}

	public static void taiLai() {
		khoiDong();
		config = YamlConfiguration.loadConfiguration(tep);
		InputStream duLieu = Main.instance.getResource("caidat.yml");
		if (duLieu != null)
			config.setDefaults(
					YamlConfiguration.loadConfiguration(new InputStreamReader(duLieu, Charset.forName("UTF8"))));
	}

	public static FileConfiguration lay() {
		if (config == null)
			taiLai();
		return config;
	}

	public void kiemTra() {
		if (lay().getString("Phien-Ban").equals(Main.instance.getDescription().getVersion())) {
			tep.renameTo(new File(Main.instance.getDataFolder(), "catdat.yml.old"));
			tep.delete();
			Main.instance.saveResource("catdat.yml", false);
			Main.instance.getServer().getConsoleSender().sendMessage(LinhTinh
					.chatMau("[GUIKhoiDau] &dPhát hiện phiên bản cũ của caidat.yml - Tiến hành thay thế tệp mới..."));
		}
	}
}
