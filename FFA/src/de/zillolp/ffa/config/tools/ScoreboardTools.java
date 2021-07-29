package de.zillolp.ffa.config.tools;

import java.io.File;

import org.bukkit.entity.Player;

import de.zillolp.ffa.utils.ConfigUtil;
import de.zillolp.ffa.utils.StringUtil;

public class ScoreboardTools extends StringUtil {
	private ConfigUtil configutil;
	private Player p;
	private boolean ShowHearts;
	private String Hearts;
	private String Title;
	private String Line;
	private String Line1;
	private String Line2;
	private String Line3;
	private String Line4;
	private String Line5;
	private String Line6;
	private String Line7;
	private String Line8;
	private String Line9;
	private String Line10;
	private String Line11;
	private String Line12;
	private String Actionbar;

	public ScoreboardTools(Player p) {
		this.p = p;
		configutil = new ConfigUtil(new File("plugins/FFA/scoreboard.yml"));
		ShowHearts = configutil.getBoolean("Show Hearts");
		Hearts = configutil.getString("Hearts");
		Title = configutil.getString("Scoreboard.Title");
		Line = configutil.getString("Scoreboard.Lines.1");
		Line1 = configutil.getString("Scoreboard.Lines.2");
		Line2 = configutil.getString("Scoreboard.Lines.3");
		Line3 = configutil.getString("Scoreboard.Lines.4");
		Line4 = configutil.getString("Scoreboard.Lines.5");
		Line5 = configutil.getString("Scoreboard.Lines.6");
		Line6 = configutil.getString("Scoreboard.Lines.7");
		Line7 = configutil.getString("Scoreboard.Lines.8");
		Line8 = configutil.getString("Scoreboard.Lines.9");
		Line9 = configutil.getString("Scoreboard.Lines.10");
		Line10 = configutil.getString("Scoreboard.Lines.11");
		Line11 = configutil.getString("Scoreboard.Lines.12");
		Line12 = configutil.getString("Scoreboard.Lines.13");
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

	public String getLine() {
		return replaceScoreboard(p, Line, "§b");
	}

	public String getLine1() {
		return replaceScoreboard(p, Line1, "§0");
	}

	public String getLine2() {
		return replaceScoreboard(p, Line2, "§9");
	}

	public String getLine3() {
		return replaceScoreboard(p, Line3, "§3");
	}

	public String getLine4() {
		return replaceScoreboard(p, Line4, "§1");
	}

	public String getLine5() {
		return replaceScoreboard(p, Line5, "§8");
	}

	public String getLine6() {
		return replaceScoreboard(p, Line6, "§2");
	}

	public String getLine7() {
		return replaceScoreboard(p, Line7, "§5");
	}

	public String getLine8() {
		return replaceScoreboard(p, Line8, "§4");
	}

	public String getLine9() {
		return replaceScoreboard(p, Line9, "§6");
	}

	public String getLine10() {
		return replaceScoreboard(p, Line10, "§7");
	}

	public String getLine11() {
		return replaceScoreboard(p, Line11, "§n");
	}

	public String getLine12() {
		return replaceScoreboard(p, Line12, "§l");
	}

	public String getActionbar() {
		return replaceActionbar(p, Actionbar);
	}
}
