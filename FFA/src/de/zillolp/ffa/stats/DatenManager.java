package de.zillolp.ffa.stats;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import de.zillolp.ffa.config.tools.ConfigTools;

public class DatenManager {
	private final static String table = "ffa_players";
	private static boolean status;
	private static boolean connected;
	private static MySQL mysql;
	private static SQLite sqlite;

	public DatenManager() {
		status = ConfigTools.getMySQL();
		if (status) {
			mysql = new MySQL();
			mysql.load();
			connected = mysql.connected;
		} else {
			connected = true;
			sqlite = new SQLite();
			sqlite.connect();
		}
	}

	public static boolean getStatus() {
		return status;
	}

	public static boolean getConnected() {
		return connected;
	}

	public static MySQL getMysql() {
		return mysql;
	}

	public static SQLite getSqlite() {
		return sqlite;
	}

	public static void createPlayer(Player p) {
		String uuid = p.getUniqueId().toString();
		if (!(inPlayers(uuid))) {
			String query = "INSERT INTO " + table + "(UUID, NAME, KILLS, DEATHS) VALUES ('" + uuid + "', '"
					+ p.getName() + "', '0', '0');";
			if (status) {
				mysql.update(query);
			} else {
				sqlite.update(query);
			}
		} else {
			checkName(p);
		}
	}

	private static void checkName(Player p) {
		String uuid = p.getUniqueId().toString();
		String name = p.getName();
		if (!(getName(uuid).equalsIgnoreCase(name))) {
			setName(uuid, name);
		}
	}

	private static boolean inPlayers(String uuid) {
		String query = "SELECT * FROM " + table + " WHERE UUID= '" + uuid + "'";
		try {
			ResultSet rs;
			if (status) {
				rs = mysql.query(query);
			} else {
				rs = sqlite.query(query);
			}
			if (rs.next()) {
				return rs.getString("UUID") != null;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static String getName(String uuid) {
		String i = "";
		String query = "SELECT * FROM " + table + " WHERE UUID= '" + uuid + "'";
		try {
			ResultSet rs;
			if (status) {
				rs = mysql.query(query);
			} else {
				rs = sqlite.query(query);
			}
			if (rs.next() && rs.getString("NAME") == null) {
				return i;
			}
			i = rs.getString("NAME");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	public static Long getKills(String uuid) {
		Long i = 0L;
		String query = "SELECT * FROM " + table + " WHERE UUID= '" + uuid + "'";
		try {
			ResultSet rs;
			if (status) {
				rs = mysql.query(query);
			} else {
				rs = sqlite.query(query);
			}
			if (rs.next() && Long.valueOf(rs.getLong("KILLS")) == null) {
				return i;
			}
			i = rs.getLong("KILLS");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	public static Long getDeaths(String uuid) {
		Long i = 0L;
		String query = "SELECT * FROM " + table + " WHERE UUID= '" + uuid + "'";
		try {
			ResultSet rs;
			if (status) {
				rs = mysql.query(query);
			} else {
				rs = sqlite.query(query);
			}
			if (rs.next() && Long.valueOf(rs.getLong("DEATHS")) == null) {
				return i;
			}
			i = rs.getLong("DEATHS");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	public static void setName(String uuid, String name) {
		String query = "UPDATE " + table + " SET NAME= '" + name + "' WHERE UUID= '" + uuid + "';";
		if (status) {
			mysql.update(query);
		} else {
			sqlite.update(query);
		}
	}

	public static void setKills(String uuid, Long kills) {
		String query = "UPDATE " + table + " SET KILLS= '" + kills + "' WHERE UUID= '" + uuid + "';";
		if (status) {
			mysql.update(query);
		} else {
			sqlite.update(query);
		}
	}

	public static void setDeaths(String uuid, Long deaths) {
		String query = "UPDATE " + table + " SET DEATHS= '" + deaths + "' WHERE UUID= '" + uuid + "';";
		if (status) {
			mysql.update(query);
		} else {
			sqlite.update(query);
		}
	}

	public static ResultSet OrderBy(String type, int limit) {
		String query = "SELECT UUID FROM " + table + " ORDER BY " + type.toUpperCase() + " DESC LIMIT " + limit;
		if (status) {
			return mysql.query(query);
		} else {
			return sqlite.query(query);
		}
	}

	public static ResultSet OrderBy(String type) {
		String query = "SELECT UUID FROM " + table + " ORDER BY " + type.toUpperCase() + " DESC";
		if (status) {
			return mysql.query(query);
		} else {
			return sqlite.query(query);
		}
	}
}
