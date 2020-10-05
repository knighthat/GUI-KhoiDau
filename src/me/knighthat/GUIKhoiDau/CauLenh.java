package me.knighthat.GUIKhoiDau;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CauLenh implements CommandExecutor, TabCompleter {

	private Main plugin;

	public CauLenh(Main plugin) {
		this.plugin = plugin;
	}

	TaoInventory taoGUIs = new TaoInventory(plugin);

	private List<String> duKien0 = new ArrayList<String>();
	private List<String> duKien1 = new ArrayList<String>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("guikhoidau") || label.equalsIgnoreCase("guikd") || label.equalsIgnoreCase("gkd")) {
			if (args.length == 1 && args[0].equalsIgnoreCase("tailai"))
				if (!sender.hasPermission("guikhoidau.admin")) {
					sender.sendMessage(LinhTinh.chatMau(plugin.caiDat.lay().getString("Khong_Quyen.Lenh")));
				} else {
					plugin.caiDat.taiLai();
					duKien0.clear();
					duKien1.clear();
					plugin.tepTin.clear();
					plugin.napGUIs();
					sender.sendMessage(LinhTinh.chatMau(plugin.caiDat.lay().getString("Tai_Lai")));
					return true;
				}
			if (args.length <= 3 && args[0].equalsIgnoreCase("momenu")) {
				Player player = (Player) sender;
				if (args.length == 3) {
					if (!sender.hasPermission("guikhoidau.momenu.nguoikhac"))
						sender.sendMessage(LinhTinh.chatMau(plugin.caiDat.lay().getString("Khong_Quyen.Lenh")));
					if (plugin.getServer().getPlayer(args[2]).equals(null)) {
						sender.sendMessage(LinhTinh.chatMau(
								plugin.caiDat.lay().getString("Khong_Tim_Thay_Nguoi_Choi").replace("%ten%", args[2])));
						return true;
					} else
						player = plugin.getServer().getPlayer(args[2]);
				}
				if (args.length == 2 && !(sender instanceof Player)) {
					sender.sendMessage(LinhTinh.chatMau(plugin.caiDat.lay().getString("Khong_Phai_Nguoi_Choi")));
					return true;
				}
				for (String tepConfig : plugin.tenTepTin.keySet()) {
					FileConfiguration config = plugin.tenTepTin.get(tepConfig);
					if (config.getString("Cau_Lenh").equals(args[1])
							&& !sender.hasPermission("guikhoidau.momenu." + config.getString("Cau_Lenh"))) {
						sender.sendMessage(LinhTinh.chatMau(plugin.caiDat.lay().getString("Khong_Quyen.Menu")));
						return true;
					}
				}
				taoGUIs.taoGUIs(player, plugin.tepTin.get(args[1]));
			}
			for (String i : duKien0)
				sender.sendMessage("/" + label + " " + i);
			return true;
		}
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (duKien0.isEmpty()) {
			duKien0.add("TaiLai");
			duKien0.add("MoMenu");
		}
		if (duKien1.isEmpty())
			for (String i : plugin.tepTin.keySet()) {
				duKien1.add(i);
			}
		List<String> ketQua = new ArrayList<String>();
		if (args.length == 1) {
			for (String i : duKien0)
				if (i.toLowerCase().startsWith(args[0].toLowerCase()))
					ketQua.add(i);
			return ketQua;
		}
		if (args.length == 2) {
			for (String i : duKien1)
				if (i.toLowerCase().startsWith(args[1].toLowerCase()))
					ketQua.add(i);
			return ketQua;
		}
		if (args.length == 3 && args[1].equalsIgnoreCase("momenu"))
			return null;

		return null;
	}

}
