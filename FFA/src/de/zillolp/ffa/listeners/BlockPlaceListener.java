package de.zillolp.ffa.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.LanguageTools;
import de.zillolp.ffa.config.tools.LocationTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.profiles.PlayerProfil;
import de.zillolp.ffa.xclasses.XMaterial;

public class BlockPlaceListener implements Listener {
	private static ArrayList<Location> blocks = new ArrayList<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void on(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
		if (playerprofil != null) {
			String PREFIX = LanguageTools.getPREFIX();
			if (!(playerprofil.getBuildmode()) && playerprofil.getJoined()) {
				if (playerprofil.getIngame()) {
					String arena = Main.getInstance().getArenaManager().active_arena;
					LocationTools locationtools = new LocationTools(arena);
					if (locationtools.getBuild()) {
						Block block = e.getBlock();
						if (e.getBlockReplacedState().getType() == XMaterial.AIR.parseMaterial()) {
							Location loc = block.getLocation();
							int protection = ConfigTools.getBuildingProtection();
							if (!(GameChangeListener.checkOnSpawn(loc, locationtools, protection))) {
								blocks.add(loc);
								replaceBlock(block, ConfigTools.getReplaceType(), ConfigTools.getReplaceTime(), false);
								replaceBlock(block, XMaterial.AIR, ConfigTools.getAirTime(), true);
								if (ConfigTools.getInfiniteblocks()) {
									ItemStack item = e.getItemInHand();
									if (item.getAmount() - 1 <= 0) {
										p.getInventory().setItemInHand(item);
									}
								}
							} else {
								p.sendMessage(PREFIX + LanguageTools.getBUILD_PROTECTION());
								e.setCancelled(true);
							}
						} else {
							e.setCancelled(true);
						}
					} else {
						e.setCancelled(true);
					}
				} else {
					e.setCancelled(true);
				}
			}
		}
	}

	public static void replaceAll() {
		for (int i = 0; i < blocks.size(); i++) {
			Location loc = blocks.get(i);
			loc.getBlock().setType(XMaterial.AIR.parseMaterial());
		}
		blocks.clear();
	}

	private void replaceBlock(Block block, XMaterial material, int delay, boolean delete) {
		Location loc = block.getLocation();
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

			@Override
			public void run() {
				if (blocks.contains(loc)) {
					block.setType(material.parseMaterial());
					if (delete) {
						blocks.remove(loc);
					}
				}
			}
		}, delay);
	}
}
