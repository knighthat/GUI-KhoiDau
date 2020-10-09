package me.knighthat.GUIKhoiDau;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static Main instance;

	@Override
	public void onEnable() {
		instance = this;
		CaiDat.khoiDong();
		CauLenh.napGUIs();
		getCommand("guikhoidau").setExecutor(new CauLenh());
		getCommand("guikhoidau").setTabCompleter(new CauLenh());
		getCommand("check").setExecutor(new CauLenh());
		getServer().getPluginManager().registerEvents(new TaoInventory(), this);
		if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
			getServer().getConsoleSender()
					.sendMessage(LinhTinh.chatMau("[GUIKhoiDau] &bĐã liên kết với plugin PlaceholderAPI"));
		} else
			getServer().getConsoleSender().sendMessage(
					LinhTinh.chatMau("[GUIKhoiDau] &cKhông tìm thấy plugin PlaceHolderAPI! Liên kết thất bại."));
		if (Hooks.vaultPlugin()) {
			getServer().getConsoleSender().sendMessage(LinhTinh.chatMau("[GUIKhoiDau] &eĐã liên kết với plugin Vault"));
		} else
			getServer().getConsoleSender()
					.sendMessage(LinhTinh.chatMau("[GUIKhoiDau] &cKhông tìm thấy plugin Vault! Liên kết thất bại."));
		getServer().getConsoleSender().sendMessage(LinhTinh.chatMau("[GUIKhoiDau] &aHoàn tất quá trình khởi động!"));
	}
}
