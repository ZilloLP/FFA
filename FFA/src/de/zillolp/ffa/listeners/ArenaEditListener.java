package de.zillolp.ffa.listeners;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.KitTools;
import de.zillolp.ffa.config.tools.LanguageTools;
import de.zillolp.ffa.config.tools.LocationTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.profiles.InventoryProfil;
import de.zillolp.ffa.profiles.PlayerProfil;
import de.zillolp.ffa.xclasses.XMaterial;

public class ArenaEditListener implements Listener {
	private HashMap<Player, InventoryProfil> invprofiles = Main.getInstance().invprofiles;
	private HashMap<Player, PlayerProfil> playerprofiles = Main.getInstance().playerprofiles;

	@EventHandler
	public void on(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerProfil playerprofil = playerprofiles.get(p);
		InventoryProfil invprofil = invprofiles.get(p);
		if (playerprofil != null) {
			Inventory clickedinv = e.getClickedInventory();
			ItemStack currentitem = e.getCurrentItem();
			if (clickedinv != null && currentitem != null && currentitem.getType() != Material.AIR) {
				String viewtitle = e.getView().getTitle();
				if (viewtitle != null && viewtitle.equalsIgnoreCase(Main.getInstance().getInventorySetter().TITLE)) {
					e.setCancelled(true);
					ClickType clicktype = e.getClick();
					String itemname = currentitem.getItemMeta().getDisplayName();
					Material itemtype = currentitem.getType();
					String PREFIX = LanguageTools.getPREFIX();
					String arena = playerprofil.getArena();
					boolean english = ConfigTools.getEnglish();
					if (itemtype == XMaterial.IRON_SWORD.parseMaterial()
							&& itemname.equalsIgnoreCase(Main.getInstance().getInventorySetter().SET_KIT)) {
						PlayerInventory player_inv = p.getInventory();
						KitTools kittools = new KitTools(arena, player_inv.getContents(),
								player_inv.getArmorContents());
						if (clicktype == ClickType.LEFT) {
							kittools.saveKit();
							Main.getInstance().getArenaManager().checkArenas();
							if (english) {
								p.sendMessage(PREFIX + "§7You set the §ekit §7for the arena §b" + arena + "§7.");
							} else {
								p.sendMessage(
										PREFIX + "§7Du hast das §eKit §7für die Arena §b" + arena + " §7gesetzt.");
							}
							Main.getInstance().getInventorySetter().setArenainv(p, invprofil.getArenainv());
						} else if (clicktype == ClickType.RIGHT) {
							p.closeInventory();
							kittools.loadKit(p);
							if (english) {
								p.sendMessage(
										PREFIX + "§7You have loaded the §ekit §7for the arena §b" + arena + "§7.");
							} else {
								p.sendMessage(
										PREFIX + "§7Du hast das §eKit §7für die Arena §b" + arena + " §7geladen.");
							}
						}
					} else if (itemtype == XMaterial.BEACON.parseMaterial()) {
						LocationTools locationtools = new LocationTools(arena);
						if (itemname.equalsIgnoreCase(Main.getInstance().getInventorySetter().TEAMS_ALLOWED)) {
							locationtools.setTeams(false);
							Main.getInstance().getArenaManager().checkArenas();
							if (english) {
								p.sendMessage(
										PREFIX + "§7Teams are now §cnot allowed §7for the arena §b" + arena + "§7.");
							} else {
								p.sendMessage(PREFIX + "§7Teams sind für die Arena §b" + arena + " §7nun §cverboten.");
							}
						} else if (itemname.equalsIgnoreCase(Main.getInstance().getInventorySetter().TEAMS_NOT_ALLOWED)) {
							locationtools.setTeams(true);
							Main.getInstance().getArenaManager().checkArenas();
							if (english) {
								p.sendMessage(PREFIX + "§7Teams are now §aallowed §7for the arena §b" + arena + "§7.");
							} else {
								p.sendMessage(PREFIX + "§7Teams sind für die Arena §b" + arena + " §7nun §aerlaubt.");
							}
						}
						Main.getInstance().getInventorySetter().setArenainv(p, invprofil.getArenainv());
					} else if (itemtype == XMaterial.SANDSTONE.parseMaterial()) {
						LocationTools locationtools = new LocationTools(arena);
						if (itemname.equalsIgnoreCase(Main.getInstance().getInventorySetter().BLOCKS_ACTIVATED)) {
							locationtools.setBuild(false);
							if (english) {
								p.sendMessage(
										PREFIX + "§7Placing blocks is §cdeactivated §7in the arena §b" + arena + "§7.");
							} else {
								p.sendMessage(
										PREFIX + "§7Blöcke Platzieren ist in der Arena §b" + arena + " §cdeaktiviert.");
							}
						} else if (itemname.equalsIgnoreCase(Main.getInstance().getInventorySetter().BLOCKS_DEACTIVATED)) {
							locationtools.setBuild(true);
							if (english) {
								p.sendMessage(
										PREFIX + "§7Placing blocks is §aactivated §7in the arena §b" + arena + "§7.");
							} else {
								p.sendMessage(
										PREFIX + "§7Blöcke Platzieren ist in der Arena §b" + arena + " §aaktiviert.");
							}
						}
						Main.getInstance().getInventorySetter().setArenainv(p, invprofil.getArenainv());
					} else if (itemtype == XMaterial.OAK_SIGN.parseMaterial()
							&& itemname.equalsIgnoreCase(Main.getInstance().getInventorySetter().SET_SIGN)) {
						playerprofil.setSetsign(true);
						if (english) {
							p.sendMessage(PREFIX + "§7You set the §eSpawn §7for the arena §b" + arena + "§7.");
						} else {
							p.sendMessage(PREFIX + "§7Mache Rechtsklick auf ein §6Schild§7.");
						}
					} else if (itemtype == XMaterial.PLAYER_HEAD.parseMaterial()
							&& itemname.equalsIgnoreCase(Main.getInstance().getInventorySetter().SET_UPPERCORNER)) {
						LocationTools locationtools = new LocationTools(arena, p.getLocation());
						locationtools.saveLocation("Uppercorner");
						Main.getInstance().getArenaManager().checkArenas();
						if (english) {
							p.sendMessage(PREFIX + "§7You set the §eUpper Corner §7for the arena §b" + arena + "§7.");
						} else {
							p.sendMessage(
									PREFIX + "§7Du hast die §eObere Ecke §7für die Arena §b" + arena + " §7gesetzt.");
						}
						Main.getInstance().getInventorySetter().setArenainv(p, invprofil.getArenainv());
					} else if (itemtype == XMaterial.MAGMA_CREAM.parseMaterial()
							&& itemname.equalsIgnoreCase(Main.getInstance().getInventorySetter().SET_SPAWN)) {
						LocationTools locationtools = new LocationTools(arena, p.getLocation());
						locationtools.saveLocation("Spawn");
						Main.getInstance().getArenaManager().checkArenas();
						if (english) {
							p.sendMessage(PREFIX + "§7You set the §eSpawn §7for the arena §b" + arena + "§7.");
						} else {
							p.sendMessage(PREFIX + "§7Du hast den §eSpawn §7für die Arena §b" + arena + " §7gesetzt.");
						}
						Main.getInstance().getInventorySetter().setArenainv(p, invprofil.getArenainv());
					} else if (itemtype == XMaterial.PLAYER_HEAD.parseMaterial()
							&& itemname.equalsIgnoreCase(Main.getInstance().getInventorySetter().SET_BUTTOMCORNER)) {
						LocationTools locationtools = new LocationTools(arena, p.getLocation());
						locationtools.saveLocation("Bottomcorner");
						Main.getInstance().getArenaManager().checkArenas();
						if (english) {
							p.sendMessage(PREFIX + "§7You set the §eButtom Corner §7for the arena §b" + arena + "§7.");
						} else {
							p.sendMessage(
									PREFIX + "§7Du hast die §eUntere Ecke §7für die Arena §b" + arena + " §7gesetzt.");
						}
						Main.getInstance().getInventorySetter().setArenainv(p, invprofil.getArenainv());
					}
				}
			}
		}
	}
}
