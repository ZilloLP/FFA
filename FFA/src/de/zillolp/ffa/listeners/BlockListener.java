package de.zillolp.ffa.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.LocationTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.profiles.PlayerProfil;
import de.zillolp.ffa.xclasses.XMaterial;

public class BlockListener implements Listener {
	
	private boolean block_weather = true;
	private final String[] blocked_commands = new String[] { "/weather", "/toggledownfall" };

	@EventHandler
	public void on(BlockBreakEvent e) {
		Player p = e.getPlayer();
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
		if (playerprofil != null) {
			if (!(playerprofil.getBuildmode()) && playerprofil.getJoined()) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void on(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
		if (playerprofil != null) {
			if (!(playerprofil.getBuildmode() || playerprofil.getIngame()) && playerprofil.getJoined()) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void on(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
		if (playerprofil != null) {
			if (!(playerprofil.getBuildmode()) && playerprofil.getJoined()) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void on(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
		if (playerprofil != null) {
			if (!(playerprofil.getBuildmode() || playerprofil.getIngame()) && playerprofil.getJoined()) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void on(EntityDamageByBlockEvent e) {
		LocationTools locationtools = new LocationTools(Main.getInstance().getArenaManager().active_arena);
		Location loc = locationtools.loadLocation("Spawn");
		if (loc != null) {
			if (loc.getWorld().getName().equalsIgnoreCase(e.getEntity().getWorld().getName())) {
				if (e.getCause() != DamageCause.VOID) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void on(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
			if (playerprofil != null) {
				if (!(playerprofil.getBuildmode() || playerprofil.getIngame()) && playerprofil.getJoined()) {
					e.setCancelled(true);
				}
			}
		}
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
			if (playerprofil != null) {
				if (!(playerprofil.getBuildmode() || playerprofil.getIngame()) && playerprofil.getJoined()) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void on(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
			if (playerprofil != null) {
				if (!(playerprofil.getBuildmode() || playerprofil.getIngame()) && playerprofil.getJoined()) {
					e.setCancelled(true);
				}
				if (!(ConfigTools.getFalldamage()) && e.getCause() == DamageCause.FALL) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void on(EntityInteractEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
			if (playerprofil != null) {
				if (!(playerprofil.getBuildmode() || playerprofil.getIngame()) && playerprofil.getJoined()) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void on(PlayerInteractAtEntityEvent e) {
		Player p = e.getPlayer();
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
		if (playerprofil != null) {
			if (!(playerprofil.getBuildmode() || playerprofil.getIngame()) && playerprofil.getJoined()) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void on(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
		if (playerprofil != null) {
			if (!(playerprofil.getBuildmode() || playerprofil.getIngame()) && playerprofil.getJoined()) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void on(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
		if (playerprofil != null) {
			if (!(playerprofil.getBuildmode()) && playerprofil.getJoined()) {
				Block block = e.getClickedBlock();
				if (!(playerprofil.getIngame())) {
					e.setCancelled(true);
				} else if (block != null) {
					Material type = block.getType();
					if (e.getAction().equals(Action.PHYSICAL)) {
						if (type == XMaterial.FARMLAND.parseMaterial()) {
							e.setCancelled(true);
						}
					} else {
						if (ConfigTools.getBlocked_blocks().contains(type)) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void on(FoodLevelChangeEvent e) {
		LocationTools locationtools = new LocationTools(Main.getInstance().getArenaManager().active_arena);
		Location loc = locationtools.loadLocation("Spawn");
		if (loc != null) {
			if (loc.getWorld().getName().equalsIgnoreCase(e.getEntity().getWorld().getName())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void on(ProjectileLaunchEvent e) {
		if (e.getEntity() != null && e.getEntity().getShooter() instanceof Player) {
			Player p = (Player) e.getEntity().getShooter();
			PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
			if (playerprofil != null) {
				if (!(playerprofil.getBuildmode() || playerprofil.getIngame()) && playerprofil.getJoined()) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void on(CreatureSpawnEvent e) {
		LocationTools locationtools = new LocationTools(Main.getInstance().getArenaManager().active_arena);
		Location loc = locationtools.loadLocation("Spawn");
		if (loc != null) {
			if (loc.getWorld().getName().equalsIgnoreCase(e.getLocation().getWorld().getName())) {
				if (!(e.getEntity() instanceof Arrow)) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void on(PlayerBedEnterEvent e) {
		Player p = e.getPlayer();
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
		if (playerprofil != null) {
			if (!(playerprofil.getBuildmode()) && playerprofil.getJoined()) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(p);
		if (playerprofil != null) {
			if (!(playerprofil.getBuildmode() || playerprofil.getIngame()) && playerprofil.getJoined()) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void on(PlayerCommandPreprocessEvent e) {
		String msg = e.getMessage();
		String[] args = msg.split(" ");
		if (Arrays.asList(blocked_commands).contains(args[0].toLowerCase())) {
			block_weather = false;
			Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					block_weather = true;
				}
			}, 5);
		}
	}

	@EventHandler
	public void on(WeatherChangeEvent e) {
		LocationTools locationtools = new LocationTools(Main.getInstance().getArenaManager().active_arena);
		Location loc = locationtools.loadLocation("Spawn");
		if (loc != null) {
			if (loc.getWorld().getName().equalsIgnoreCase(e.getWorld().getName())) {
				if (block_weather) {
					e.setCancelled(true);
				}
			}
		}
	}

}
