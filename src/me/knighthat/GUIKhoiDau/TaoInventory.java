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

	public Main plugin() {
		return Main.instance;
	}

	private static boolean phienBanMoi = Arrays.stream(Material.values()).map(Material::name)
			.collect(Collectors.toList()).contains("PLAYER_HEAD");

	public static void taoGUIs(Player player, FileConfiguration config) {
		Inventory inv = Bukkit.createInventory(null, PhanLoai.checkInteger(config.getInt("So_Hang"), 6) * 9,
				PlaceholderAPI.setPlaceholders(player, LinhTinh
						.chatMau(config.contains("Tieu_De") ? config.getString("Tieu_De") : "&c&lTIÊU ĐỀ TRỐNG!")));
		if (config.contains("O_Trong")) {
			String[] oTrong = config.getString("O_Trong").toUpperCase().split(" ");
			ItemStack vatLieu = PhanLoai.phanLoaiItems(oTrong[0]);
			if (!vatLieu.getType().equals(Material.AIR)) {
				ItemMeta fillMeta = vatLieu.getItemMeta();
				fillMeta.setDisplayName(" ");
				vatLieu.setItemMeta(fillMeta);
				if (oTrong.length > 1 && oTrong[1].matches("-?\\d+"))
					vatLieu.setAmount(PhanLoai.checkInteger(Integer.parseInt(oTrong[1]), vatLieu.getMaxStackSize()));
			}
			for (int y = 0; y < inv.getSize(); y++)
				inv.setItem(y, vatLieu);
		}
		config.getConfigurationSection("Vat_Pham").getKeys(false).forEach(muc -> {
			String duongDan = "Vat_Pham." + muc + ".";
			ItemStack vatLieu = PhanLoai.phanLoaiItems(config.getString(duongDan + "Vat_Lieu"));
			if (config.getString(duongDan + "Vat_Lieu").toUpperCase().equals("PLAYER_HEAD")) {
				vatLieu = new ItemStack(Material.getMaterial(phienBanMoi ? "PLAYER_HEAD" : "SKULL_ITEM"), 1, (short) 3);
				if (config.contains(duongDan + "Chu_So_Huu"))
					if (config.contains(duongDan + "Chu_So_Huu")) {
						SkullMeta sMeta = (SkullMeta) vatLieu.getItemMeta();
						sMeta.setOwner(
								PlaceholderAPI.setPlaceholders(player, config.getString(duongDan + "Chu_So_Huu")));
						vatLieu.setItemMeta(sMeta);
					}
			}
			if (!vatLieu.getType().equals(Material.AIR)) {
				ItemMeta meta = vatLieu.getItemMeta();
				meta.setLore(config.getStringList(duongDan + "Chu_Thich").stream()
						.map(string -> LinhTinh.papi(player, string)).collect(Collectors.toList()));
				meta.setDisplayName(LinhTinh.papi(player,
						config.contains(duongDan + "Ten_Hien_Thi") ? config.getString(duongDan + "Ten_Hien_Thi")
								: "&4&lTên bị trống!"));
				vatLieu.setAmount(PhanLoai.checkInteger(config.getInt(duongDan + "So_Luong"), vatLieu.getAmount()));
				if (config.contains(duongDan + "Thuoc_Tinh"))
					for (String i : config.getStringList(duongDan + "Thuoc_Tinh"))
						if (Arrays.stream(ItemFlag.values()).map(ItemFlag::name).collect(Collectors.toList())
								.contains(i.toUpperCase()))
							meta.addItemFlags(ItemFlag.valueOf(i.toUpperCase()));
				vatLieu.setItemMeta(meta);
				if (config.contains(duongDan + "Cuong_Hoa"))
					for (String i : config.getStringList(duongDan + "Cuong_Hoa"))
						if (PhanLoai.phanLoaiEnchantments(i.split(" ")[0]) != null) {
							Enchantment cuongHoa = PhanLoai.phanLoaiEnchantments(i.split(" ")[0]);
							int capDo = 1;
							if (i.split(" ").length > 1 && (i.split(" ")[1].matches("-?\\d+")))
								capDo = PhanLoai.checkInteger(Integer.parseInt(i.split(" ")[1]),
										cuongHoa.getMaxLevel());
							vatLieu.addUnsafeEnchantment(cuongHoa, capDo);
						}
			}
			inv.setItem(PhanLoai.checkInteger(config.getInt(duongDan + "Vi_Tri") - 1, inv.getSize()), vatLieu);
		});
		player.openInventory(inv);
	}

	/**
	 * @param e
	 */
	@EventHandler
	public void chonItem(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if (e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR))
			for (String i : CauLenh.tepTin.keySet()) {
				FileConfiguration config = CauLenh.tepTin.get(i);
				if (e.getInventory().getName().equals(LinhTinh.chatMau(config.getString("Tieu_De")))) {
					e.setCancelled(true);
					config.getConfigurationSection("Vat_Pham").getKeys(false).forEach(muc -> {
						String duongDan = "Vat_Pham." + muc + ".";
						if (e.getCurrentItem().getItemMeta().getDisplayName() != null
								&& e.getCurrentItem().getItemMeta().getDisplayName()
										.equals(LinhTinh.papi(player, config.getString(duongDan + "Ten_Hien_Thi")))) {
							if (config.contains(duongDan + "Quyen_Han"))
								if (!player.hasPermission(config.getString(duongDan + "Quyen_Han"))) {
									player.sendMessage(
											LinhTinh.chatMau(CaiDat.lay().getString("Khong_Quyen.Vat_Pham")));
									return;
								}
							if (config.contains(duongDan + "Chi_Phi")) {
								List<ItemStack> vatPham = new ArrayList<ItemStack>();
								Double chiPhi = 0.0;
								int soLuong = 1;
								for (String y : config.getStringList(duongDan + "Chi_Phi"))
									if (y.startsWith("TIEN:")) {
										chiPhi += Double.parseDouble(y.replace("TIEN: ", "").replace("TIEN:", ""));
									} else if (!PhanLoai.phanLoaiItems(y.split(" ")[0]).getType()
											.equals(Material.AIR)) {
										ItemStack vP = PhanLoai.phanLoaiItems(y.split(" ")[0]);
										if (y.split(" ").length > 1 && y.split(" ")[1].matches("-?\\d+")
												&& Integer.parseInt(y.split(" ")[1]) > 0)
											soLuong = Integer.parseInt(y.split(" ")[1]);
										vP.setAmount(soLuong);
										vatPham.add(vP);
									}
								for (ItemStack item : vatPham)
									if (!player.getInventory().containsAtLeast(item, item.getAmount())) {
										player.sendMessage(LinhTinh.chatMau(CaiDat.lay().getString("Thieu_Vat_Pham")
												.replace("%so_luong%", String.valueOf(item.getAmount()))
												.replace("%vat_pham%", item.getType().toString())));
										return;
									} else {
										int conLai = item.getAmount();
										for (int y = 0; y < player.getInventory().getSize(); y++) {
											ItemStack conThieu = player.getInventory().getItem(y);
											if (conThieu != null && conThieu.getType().equals(item.getType()))
												if (conThieu.getAmount() >= conLai) {
													conThieu.setAmount(conThieu.getAmount() - conLai);
													player.getInventory().setItem(y, conThieu);
													break;
												} else {
													conLai -= conThieu.getAmount();
													player.getInventory().setItem(y, null);
												}
										}
										player.updateInventory();
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
											if (CauLenh.tenTepTin.containsKey(cauLenh[1].trim()))
												taoGUIs(player, CauLenh.tenTepTin.get(cauLenh[1].trim()));
											break;
										case "TIN_NHAN":
											player.sendMessage(LinhTinh.papi(player, cauLenh[1].trim()));
											break;
										case "THONG_BAO":
											for (Player p : Main.instance.getServer().getOnlinePlayers())
												p.sendMessage(LinhTinh.papi(player, cauLenh[1].trim()));
											break;
										case "PLAYER":
											player.sendMessage(LinhTinh.papi(player, cauLenh[1].trim()));
											break;
										default:
											Main.instance.getServer().dispatchCommand(
													Main.instance.getServer().getConsoleSender(),
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
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin(), new Runnable() {
				public void run() {
					for (FileConfiguration config : CauLenh.tepTin.values()) {
						if (config.getBoolean("Menu_Chinh"))
							taoGUIs(player, config);
					}
				}
			}, 20L);
	}
}
