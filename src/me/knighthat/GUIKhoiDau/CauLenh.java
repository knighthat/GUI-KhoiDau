package me.knighthat.GUIKhoiDau;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class CauLenh implements CommandExecutor, TabCompleter {

	private static List<String> duKien1 = new ArrayList<String>();
	private static List<String> duKien0 = new ArrayList<String>(Arrays.asList("MoMenu", "TaiLai"));
	public static Map<String, FileConfiguration> tepTin = new HashMap<String, FileConfiguration>();
	public static Map<String, FileConfiguration> tenTepTin = new HashMap<String, FileConfiguration>();

	public static void napGUIs() {
		if (new File(Main.instance.getDataFolder().getAbsolutePath() + File.separator + "menus").list().length < 1)
			for (String i : Arrays.asList("example.yml", "MenuChinh.yml", "VuKhi.yml", "Giap.yml", "VoDung.yml"))
				Main.instance.saveResource("menus/" + i, false);
		for (String a : new File(Main.instance.getDataFolder().getAbsolutePath() + File.separator + "menus").list()) {
			FileConfiguration config = YamlConfiguration
					.loadConfiguration(new File(Main.instance.getDataFolder(), "menus/" + a));
			if (config.contains("Cau_Lenh")) {
				CauLenh.tepTin.put(config.getString("Cau_Lenh"), config);
				CauLenh.tenTepTin.put(a, config);
			}
		}
		CauLenh.duKien1 = CauLenh.tepTin.keySet().stream().collect(Collectors.toList());
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("guikhoidau") || label.equalsIgnoreCase("guikd") || label.equalsIgnoreCase("gkd")) {
			if (args.length == 0 || args.length > 3
					|| (args.length == 1 && !args[0].equalsIgnoreCase("MoMenu") && !args[0].equalsIgnoreCase("TaiLai"))
					|| (args.length > 2 && !args[0].equalsIgnoreCase("MoMenu")))
				sender.sendMessage(LinhTinh.chatMau("&f" + "&9/" + label + " TroGiup"));
			if (args.length == 1 && args[0].equalsIgnoreCase("TroGiup"))
				for (String i : duKien0)
					sender.sendMessage(LinhTinh.chatMau("&l/" + label + " " + i));
			if (args.length == 1 && args[0].equalsIgnoreCase("MoMenu"))
				if (sender instanceof Player) {
					sender.sendMessage(LinhTinh.chatMau("Click vào một trong những menus bên dưới để mở nó:"));
					for (String i : tepTin.keySet())
						if (sender.hasPermission("guikhoidau.momenu." + i))
							((Player) sender).spigot().sendMessage(LinhTinh.hoverAndClick("- " + i, "YELLOW",
									"/guikhoidau MoMenu " + i, "/guikhoidau MoMenu " + i));
				} else
					sender.sendMessage(LinhTinh.chatMau(CaiDat.lay().getString("Khong_Phai_La_Nguoi_Choi")));
			if (args.length == 1 && args[0].equalsIgnoreCase("TaiLai"))
				if (sender.hasPermission("guikhoidau.admin")) {
					CaiDat.taiLai();
					duKien0.clear();
					duKien1.clear();
					tepTin.clear();
					tenTepTin.clear();
					napGUIs();
					sender.sendMessage(LinhTinh.chatMau(CaiDat.lay().getString("Tai_Lai")));
				} else
					sender.sendMessage(LinhTinh.chatMau(CaiDat.lay().getString("Khong_Quyen.Lenh")));
			if (args.length >= 2 && args[0].equalsIgnoreCase("MoMenu")) {
				Player player = (Player) sender;
				if (args.length == 3)
					if (sender.hasPermission("guikhoidau.momenu.nguoikhac")) {
						if (Main.instance.getServer().getPlayer(args[2]).equals(null)) {
							sender.sendMessage(LinhTinh.chatMau(
									CaiDat.lay().getString("Khong_Tim_Thay_Nguoi_Choi").replace("%ten%", args[2])));
						} else
							player = Main.instance.getServer().getPlayer(args[2]);
					} else
						sender.sendMessage(LinhTinh.chatMau(CaiDat.lay().getString("Khong_Quyen.Lenh")));
				if (args.length == 2 && !(sender instanceof Player))
					sender.sendMessage(LinhTinh.chatMau(CaiDat.lay().getString("Khong_Quyen.Lenh")));
				if (tepTin.keySet().contains(args[1]))
					if (sender.hasPermission("guikhoidau.momenu." + args[1])) {
						TaoInventory.taoGUIs(player, tepTin.get(args[1]));
					} else
						sender.sendMessage(LinhTinh.chatMau(CaiDat.lay().getString("Khong_Quyen.Menu")));
				return true;
			}
		}
		if (label.equalsIgnoreCase("check")) {
			for (String i : Arrays.stream(Enchantment.values()).map(enchant -> enchant.getName())
					.collect(Collectors.toList()))
				sender.sendMessage(i);
		}
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> ketQua = new ArrayList<String>();
		if (args.length == 1)
			for (String i : duKien0)
				if (i.toLowerCase().startsWith(args[0].toLowerCase()))
					ketQua.add(i);
		if (args.length == 2 && args[0].equalsIgnoreCase("momenu"))
			for (String i : duKien1)
				if (i.toLowerCase().startsWith(args[1].toLowerCase()))
					ketQua.add(i);
		if (args.length == 3 && args[0].equalsIgnoreCase("momenu"))
			return null;
		return ketQua;
	}

}
