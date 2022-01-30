package de.zillolp.ffa.config.tools;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.profiles.KitProfil;
import de.zillolp.ffa.utils.BukkitSerialization;
import de.zillolp.ffa.utils.ConfigUtil;
import de.zillolp.ffa.xclasses.NBTEditor;

public class KitTools {
	private static ConfigUtil configutil = Main.getInstance().getConfigCreation().getManager().getNewConfig("kits.yml");
	private static ArrayList<KitProfil> kits = new ArrayList<>();
	private String root;
	private String arena;
	private ItemStack[] inventory;
	private ItemStack[] armor;

	public KitTools(String arena) {
		this.root = "Kits." + arena;
		this.arena = arena;
	}

	public KitTools(String arena, ItemStack[] inv, ItemStack[] armor) {
		this.root = "Kits." + arena;
		this.arena = arena;
		this.inventory = inv;
		this.armor = armor;
	}

	public void saveKit() {
		KitProfil profil = new KitProfil(arena, inventory, armor);
		kits.remove(profil);
		kits.add(profil);
		configutil.set(root + ".inv", BukkitSerialization.itemStackArrayToBase64(inventory));
		configutil.set(root + ".armor", BukkitSerialization.itemStackArrayToBase64(armor));
	}

	public void loadKit(Player p) {
		try {
			inventory = BukkitSerialization.itemStackArrayFromBase64(configutil.getString(root + ".inv"));
			armor = BukkitSerialization.itemStackArrayFromBase64(configutil.getString(root + ".armor"));
			if (ConfigTools.getUnbreakable()) {
				int index = 0;
				
				for (ItemStack item : inventory) {
					if (item != null && item.getType() != Material.AIR) {
						item = NBTEditor.set(item, (byte) 1, "Unbreakable");
						ItemMeta itemmeta = item.getItemMeta();
						if (itemmeta != null) {
							itemmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
							item.setItemMeta(itemmeta);
						}
						inventory[index] = item;
					}
					index++;
				}
				
				index = 0;
				
				for (ItemStack item : armor) {
					if (item != null && item.getType() != Material.AIR) {
						item = NBTEditor.set(item, (byte) 1, "Unbreakable");
						ItemMeta itemmeta = item.getItemMeta();
						if (itemmeta != null) {
							itemmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
							item.setItemMeta(itemmeta);
						}
						armor[index] = item;
					}
					index++;
				}
				
			}
			p.getInventory().setContents(inventory);
			p.getInventory().setArmorContents(armor);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isKit(String arena) {
		boolean isKit = false;
		ConfigurationSection section = configutil.getConfigurationSection("Kits");
		if (section != null && section.getKeys(false).size() > 0 && section.getKeys(false).contains(arena)) {
			isKit = true;
		}
		return isKit;
	}

	public static void loadKits() {
		ConfigurationSection section = configutil.getConfigurationSection("Kits");
		if (section != null && section.getKeys(false).size() > 0) {
			for (String current : section.getKeys(false)) {
				String arena = current;
				ItemStack[] inv = null;
				ItemStack[] armor = null;
				try {
					inv = BukkitSerialization
							.itemStackArrayFromBase64(configutil.getString("Kits." + current + ".inv"));
					armor = BukkitSerialization
							.itemStackArrayFromBase64(configutil.getString("Kits." + current + ".armor"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				kits.add(new KitProfil(arena, inv, armor));
			}
		}
	}

	public void resetKit() {
		for (String key : configutil.getConfigurationSection(root).getKeys(false)) {
			if (configutil.getString(root + "." + key) != null) {
				configutil.set(root + "." + key, null);
			} else {
				continue;
			}
		}
		configutil.set(root, null);
	}
}
