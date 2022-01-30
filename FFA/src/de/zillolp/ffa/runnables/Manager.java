package de.zillolp.ffa.runnables;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.ScoreboardTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.profiles.PlayerProfil;
import de.zillolp.ffa.xclasses.ActionBar;

public class Manager implements Runnable {
	private static int sched;

	@SuppressWarnings("deprecation")
	public Manager() {
		sched = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), this, 0, 20);
	}

	@Override
	public void run() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(all);
			if (playerprofil != null) {
				boolean joined = playerprofil.getJoined();
				Scoreboard scoreboard = all.getScoreboard();
				if (scoreboard != null && scoreboard.getObjective(DisplaySlot.SIDEBAR) != null) {
					if (ConfigTools.getScoreboard() && joined) {
						updateScoreboard(all);
					} else {
						scoreboard.clearSlot(DisplaySlot.SIDEBAR);
						scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
					}
				} else if (ConfigTools.getScoreboard() && joined) {
					setScoreboard(all);
				}
				ScoreboardTools scoreboardtools = playerprofil.getScoreboardtools();
				if (ConfigTools.getActionbar() && joined) {
					ActionBar.sendActionBar(all, scoreboardtools.getActionbar());
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void setScoreboard(Player player) {
		Bukkit.getScheduler().runTask(Main.getInstance(), new Runnable() {

			@Override
			public void run() {
				PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(player);
				if (playerprofil != null) {
					ScoreboardTools scoreboardtools = playerprofil.getScoreboardtools();
					ScoreboardManager scoreboardmanager = Bukkit.getScoreboardManager();
					Scoreboard scoreboard = scoreboardmanager.getNewScoreboard();

					if (scoreboardtools.getShowHearts()) {
					
						Objective ob = scoreboard.registerNewObjective("aab", "bba");
						ob.setDisplaySlot(DisplaySlot.BELOW_NAME);
						ob.setDisplayName(scoreboardtools.getHearts());
						
						for (Player all : Bukkit.getOnlinePlayers()) {
							int hp = (int) all.getHealth() / 2;
							ob.getScore(all).setScore(hp);
						}
						
					} else if (scoreboard.getObjective(DisplaySlot.BELOW_NAME) != null) {
					
						scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
					
					}

					Objective obj = scoreboard.registerNewObjective("aaa", "bbb");
					obj.setDisplaySlot(DisplaySlot.SIDEBAR);
					obj.setDisplayName(scoreboardtools.getTitle());
					
					for (int i = 0; i < 12; i++) {
						String team;
						String prefix = "";
						ChatColor color = ChatColor.AQUA;
						if (i <= 0) {
							team = "line";
						} else {
							team = "line" + i;
						}
						color = getColor(i);
						prefix = scoreboardtools.getLine(i);
						Team line = scoreboard.getTeam(team);
						if (line == null) {
							line = scoreboard.registerNewTeam(team);
						}
						line.setPrefix(prefix);
						line.addEntry(color.toString());
						obj.getScore(color.toString()).setScore(11 - i);
					}
				
					player.setScoreboard(scoreboard);
			
				}
			
			}

			private ChatColor getColor(int i) {
				ChatColor color = ChatColor.AQUA;
				switch (i) {
				case 1:
					color = ChatColor.BLACK;
					break;
				case 2:
					color = ChatColor.BLUE;
					break;
				case 3:
					color = ChatColor.DARK_AQUA;
					break;
				case 4:
					color = ChatColor.DARK_BLUE;
					break;
				case 5:
					color = ChatColor.DARK_GRAY;
					break;
				case 6:
					color = ChatColor.DARK_GREEN;
					break;
				case 7:
					color = ChatColor.DARK_PURPLE;
					break;
				case 8:
					color = ChatColor.DARK_RED;
					break;
				case 9:
					color = ChatColor.GOLD;
					break;
				case 10:
					color = ChatColor.YELLOW;
					break;
				case 11:
					color = ChatColor.GREEN;
					break;
				case 12:
					color = ChatColor.GRAY;
					break;
				}
				return color;
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void updateScoreboard(Player player) {
		PlayerProfil playerprofil = Main.getInstance().playerprofiles.get(player);
		if (playerprofil != null) {
			ScoreboardTools scoreboardtools = playerprofil.getScoreboardtools();
			Scoreboard scoreboard = player.getScoreboard();

			if (scoreboardtools.getShowHearts()) {
				for (Player all : Bukkit.getOnlinePlayers()) {
					int hp = (int) all.getHealth() / 2;
					if (scoreboard.getObjective(DisplaySlot.BELOW_NAME).getScore(all).getScore() != hp) {
						scoreboard.getObjective(DisplaySlot.BELOW_NAME).getScore(all).setScore(hp);
					}
				}
			} else if (scoreboard.getObjective(DisplaySlot.BELOW_NAME) != null) {
				scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
			}

			for (int i = 0; i < 12; i++) {
				String team;
				String prefix = "";
				if (i <= 0) {
					team = "line";
				} else {
					team = "line" + i;
				}
				prefix = scoreboardtools.getLine(i);
				if (!(prefix.equalsIgnoreCase(player.getScoreboard().getTeam(team).getPrefix()))) {
					Team line = scoreboard.getTeam(team);
					if (line == null) {
						line = scoreboard.registerNewTeam(team);
					}
					line.setPrefix(prefix);
				}
			}
		}
	}

	public static void stop() {
		Bukkit.getScheduler().cancelTask(sched);
	}
}
