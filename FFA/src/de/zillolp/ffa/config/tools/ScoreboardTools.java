package de.zillolp.ffa.config.tools;

import java.util.LinkedList;

import org.bukkit.entity.Player;

import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.utils.ConfigUtil;
import de.zillolp.ffa.utils.StringUtil;

public class ScoreboardTools extends StringUtil {
	private ConfigUtil configutil;
	private Player p;
	private boolean ShowHearts;
	private String Hearts;
	private String Title;

	private LinkedList<String> lines;

	private String Actionbar;

	public ScoreboardTools(Player player) {
		this.p = player;
		lines = new LinkedList<>();

		configutil = Main.getInstance().getConfigCreation().getManager().getNewConfig("scoreboard.yml");
		ShowHearts = configutil.getBoolean("Show Hearts");

		Hearts = configutil.getString("Hearts");
		Title = configutil.getString("Scoreboard.Title");

		for(int i = 1; i < 14; i++) {
			lines.add(configutil.getString("Scoreboard.Lines." + i));
		}

		Actionbar = configutil.getString("Actionbar");
	}

	public boolean getShowHearts() {
		return ShowHearts;
	}

	public String getHearts() {
		return replaceDefaults(Hearts);
	}

	public String getTitle() {
		return replaceScoreboard(p, Title, "§k");
	}

	public String getLine(int i) {

		String[] empty = {"§b", "§0", "§9", "§3", "§1", "§8", "§2", "§5", "§4", "§6", "§7", "§n", "§l"};

		return replaceScoreboard(p, Hearts, empty[i]);

	}

	public String getActionbar() {
		return replaceActionbar(p, Actionbar);
	}
}
