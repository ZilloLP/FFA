package de.zillolp.ffa.placeholderapi;

import org.bukkit.OfflinePlayer;

import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.profiles.PlayerProfil;
import de.zillolp.ffa.utils.StringUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Expansion extends PlaceholderExpansion {

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public String getAuthor() {
		return "ZilloLP";
	}

	@Override
	public String getIdentifier() {
		return "ffa";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String onRequest(OfflinePlayer p, String identifier) {
		PlayerProfil profil = Main.getInstance().playerprofiles.get(p);

		if (profil != null) {
			Long kills = profil.getKills();
			Long deaths = profil.getDeaths();
			Long killstreak = profil.getKillstreak();

			// %ffa_kills%
			if (identifier.equals("kills")) {
				return String.valueOf(kills);
			}

			// %ffa_deaths%
			if (identifier.equals("deaths")) {
				return String.valueOf(deaths);
			}

			// %ffa_killstreak%
			if (identifier.equals("killstreak")) {
				return String.valueOf(killstreak);
			}

			// %ffa_arena%
			if (identifier.equals("arena")) {
				return StringUtil.replaceMap("%map%");
			}

			// %ffa_time%
			if (identifier.equals("time")) {
				return StringUtil.replaceTime("%time%");
			}
			
			// %ffa_teams%
			if (identifier.equals("teams")) {
				return StringUtil.replaceTeams("%teams%");
			}
		} else {
			System.err.println("The player profil is null. Contact the FFA developer.");
		}

		return null;
	}
}
