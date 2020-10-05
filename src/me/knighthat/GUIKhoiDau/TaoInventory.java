package me.knighthat.GUIKhoiDau;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.clip.placeholderapi.PlaceholderAPI;

public class TaoInventory implements Listener {
	Main plugin;

	public TaoInventory(Main plugin) {
		this.plugin = plugin;
	}

	private boolean phienBanMoi = Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList())
			.contains("PLAYER_HEAD");

	public void taoGUIs(Player player, FileConfiguration config) {
		int soHang = 6;
		if (config.getInt("So_Hang") >= 1 || config.getInt("So_Hang") <= 6)
			soHang = config.getInt("So_Hang");
		String tieuDe = "&c&lTIÊU ĐỀ TRỐNG!";
		if (config.contains("Tieu_De"))
			tieuDe = config.getString("Tieu_De");
		Inventory inv = Bukkit.createInventory(null, soHang * 9,
				PlaceholderAPI.setPlaceholders(player, LinhTinh.chatMau(tieuDe)));
		if (config.contains("O_Trong")) {
			String[] oTrong = config.getString("O_Trong").toUpperCase().split(" ");
			ItemStack vatLieu = PhanLoai.phanLoaiItems(oTrong[0]);
			if (vatLieu.getType() != Material.AIR) {
				int soLuong = 1;
				if (oTrong.length > 1 && oTrong[1].matches("_?\\d+")
						&& Integer.parseInt(oTrong[1]) <= vatLieu.getMaxStackSize() && Integer.parseInt(oTrong[1]) >= 1)
					soLuong = Integer.parseInt(oTrong[1]);
				if (vatLieu.getType() != Material.AIR) {
					ItemMeta fillMeta = vatLieu.getItemMeta();
					fillMeta.setDisplayName(" ");
					vatLieu.setItemMeta(fillMeta);
					vatLieu.setAmount(soLuong);
				}
			}
			for (int y = 0; y < inv.getSize(); y++)
				inv.setItem(y, vatLieu);
		}
		config.getConfigurationSection("Vat_Pham").getKeys(false).forEach(muc -> {
			String duongDan = "Vat_Pham." + muc + ".";
			ItemStack vatLieu = PhanLoai.phanLoaiItems(config.getString(duongDan + "Vat_Lieu").toUpperCase());
			if (config.getString(duongDan + "Vat_Lieu").toUpperCase().equals("PLAYER_HEAD")) {
				vatLieu = new ItemStack(Material.getMaterial(phienBanMoi ? "PLAYER_HEAD" : "SKULL_ITEM"), 1, (short) 3);
				if (config.contains(duongDan + "Chu_So_Huu"))
					if (!config.getString(duongDan + "Chu_So_Huu").equals(null)) {
						SkullMeta sMeta = (SkullMeta) vatLieu.getItemMeta();
						sMeta.setOwner(
								PlaceholderAPI.setPlaceholders(player, config.getString(duongDan + "Chu_So_Huu")));
						vatLieu.setItemMeta(sMeta);
					}
			}
			if (!vatLieu.getType().equals(Material.AIR)) {
				ItemMeta meta = vatLieu.getItemMeta();
				List<String> lore = new ArrayList<String>();
				for (String i : config.getStringList(duongDan + "Chu_Thich"))
					lore.add(PlaceholderAPI.setPlaceholders(player, LinhTinh.chatMau(i)));
				meta.setLore(lore);
				String ten = "&4&lTên bị trống!";
				if (config.contains(duongDan + "Ten_Hien_Thi"))
					ten = PlaceholderAPI.setPlaceholders(player,
							LinhTinh.chatMau(config.getString(duongDan + "Ten_Hien_Thi")));
				meta.setDisplayName(ten);
				int soLuong = config.getInt(duongDan + "So_Luong");
				if (soLuong < 1 && soLuong > vatLieu.getMaxStackSize())
					soLuong = 1;
				vatLieu.setAmount(soLuong);
				if (config.contains(duongDan + "Thuoc_Tinh"))
					for (String i : config.getStringList(duongDan + "Thuoc_Tinh"))
						if (ItemFlag.valueOf(i) != null)
							meta.addItemFlags(ItemFlag.valueOf(i));
				vatLieu.setItemMeta(meta);
				if (config.contains(duongDan + "Cuong_Hoa"))
					for (String i : config.getStringList(duongDan + "Cuong_Hoa"))
						if (PhanLoai.phanLoaiEnchant(i.split(" ")[0]) != null) {
							Enchantment cuongHoa = PhanLoai.phanLoaiEnchant(i.split(" ")[0]);
							int capDo = 1;
							if (i.split(" ").length > 1 && Integer.parseInt(i.split(" ")[1]) > 0
									&& Integer.parseInt(i.split(" ")[1]) <= cuongHoa.getMaxLevel())
								capDo = Integer.parseInt(i.split(" ")[1]);
							vatLieu.addUnsafeEnchantment(cuongHoa, capDo);
						}
			}
			int viTri = config.getInt(duongDan + "Vi_Tri") - 1;
			if (viTri < 0 || viTri > inv.getSize())
				viTri = 0;
			inv.setItem(viTri, vatLieu);
		});
		player.openInventory(inv);
	}

	@EventHandler
	public void chonItem(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR)
			for (String i : plugin.tepTin.keySet()) {
				FileConfiguration config = plugin.tepTin.get(i);
				if (e.getInventory().getName().equals(LinhTinh.chatMau(config.getString("Tieu_De")))) {
					e.setCancelled(true);
					config.getConfigurationSection("Vat_Pham").getKeys(false).forEach(muc -> {
						String duongDan = "Vat_Pham." + muc + ".";
						if (e.getCurrentItem().getItemMeta().getDisplayName()
								.equals(LinhTinh.papi(player, config.getString(duongDan + "Ten_Hien_Thi")))) {
							if (config.contains(duongDan + "Chi_Phi")) {
								List<ItemStack> vatPham = new ArrayList<ItemStack>();
								Double chiPhi = 0.0;
								int soLuong = 1;
								for (String y : config.getStringList(duongDan + "Chi_Phi"))
									if (y.startsWith("TIEN:") && plugin.tienTe()
											&& Double.parseDouble(y.replace("TIEN: ", "").replace("TIEN:", "")) > 0) {
										chiPhi += Double.parseDouble(y.replace("TIEN: ", "").replace("TIEN:", ""));
									} else if (!PhanLoai.phanLoaiItems(y.split(" ")[0]).getType()
											.equals(Material.AIR)) {
										ItemStack vP = PhanLoai.phanLoaiItems(y.split(" ")[0]);
										if (y.split(" ").length > 1 && y.split(" ")[1].matches("_?\\d+")
												&& Integer.parseInt(y.split(" ")[1]) > 0)
											soLuong = Integer.parseInt(y.split(" ")[1]);
										vP.setAmount(soLuong);
										vatPham.add(vP);
									}
								if (chiPhi > 0 && plugin.tienTe() && plugin.getEconomy().getBalance(player) < chiPhi) {
									player.sendMessage(LinhTinh
											.chatMau(plugin.caiDat.lay().getString("Thieu_Tien").replace("%so_tien%",
													String.valueOf(chiPhi - plugin.getEconomy().getBalance(player)))));
									return;
								}
								for (ItemStack item : vatPham)
									if (!player.getInventory().containsAtLeast(item, item.getAmount())) {
										player.sendMessage(
												LinhTinh.chatMau(plugin.caiDat.lay().getString("Thieu_Vat_Pham")
														.replace("%so_luong%", String.valueOf(item.getAmount()))
														.replace("%vat_pham%", item.getType().toString())));
										return;
									}
								if (plugin.tienTe())
									plugin.getEconomy().withdrawPlayer(player, chiPhi);
								for (ItemStack item : vatPham)
									for (int a = 0; a < player.getInventory().getSize(); a++) {
										ItemStack itm = player.getInventory().getItem(a);
										if (itm != null && itm.getType().equals(item.getType())) {
											int amt = itm.getAmount() - item.getAmount();
											itm.setAmount(amt);
											player.getInventory().setItem(a, amt > 0 ? itm : null);
											player.updateInventory();
											break;
										}
									}
							}
							if (config.contains(duongDan + "Hanh_Dong"))
								for (String y : config.getStringList(duongDan + "Hanh_Dong"))
									if (y.equals("DONG_MENU")) {
										player.closeInventory();
									} else {
										String[] cauLenh = y.split(":");
										switch (cauLenh[0].toUpperCase()) {
										case "MO_MENU":
											if (plugin.tenTepTin.containsKey(cauLenh[1].trim()))
												taoGUIs(player, plugin.tenTepTin.get(cauLenh[1].trim()));
											break;
										case "TIN_NHAN":
											player.sendMessage(LinhTinh.papi(player, cauLenh[1].trim()));
											break;
										case "THONG_BAO":
											for (Player p : plugin.getServer().getOnlinePlayers())
												p.sendMessage(LinhTinh.papi(player, cauLenh[1].trim()));
											break;
										case "PLAYER":
											player.sendMessage(LinhTinh.papi(player, cauLenh[1].trim()));
											break;
										default:
											plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
													LinhTinh.papi(player, y.replace("CONSOLE:", "").trim()));
											break;
										}
									}
						}
					});
					break;
				}
			}
	}

	@EventHandler
	public void thamGia(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (!player.hasPermission("guikhoidau.bypass"))
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					for (FileConfiguration config : plugin.tepTin.values()) {
						if (config.getBoolean("Menu_Chinh"))
							taoGUIs(player, config);
					}
				}
			}, 20L);
	}
}
