package de.zillolp.ffa.stats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;

import de.zillolp.ffa.config.tools.ConfigTools;
import de.zillolp.ffa.config.tools.LanguageTools;
import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.utils.ConfigManager;
import de.zillolp.ffa.utils.ConfigUtil;

public class MySQL {
	private ConfigManager manager;
	private String PREFIX = LanguageTools.getPREFIX();
	private boolean english = ConfigTools.getEnglish();
	private ConfigUtil configutil;
	private String HOST;
	private String PORT;
	private String DATABASE;
	private String USER;
	private String PASSWORD;

	public Connection con;
	public Boolean connected;

	public MySQL() {
		manager = new ConfigManager();
		configutil = manager.getNewConfig("mysql.yml", new String[] { "FFA" });
	}

	public void load() {
		configutil.setDefault("Host", "localhost", "MySQL data");
		configutil.setDefault("Port", "3306");
		configutil.setDefault("Database", "FFA");
		configutil.setDefault("User", "root");
		configutil.setDefault("Password", "123+");
		HOST = configutil.getString("Host");
		PORT = configutil.getString("Port");
		DATABASE = configutil.getString("Database");
		USER = configutil.getString("User");
		PASSWORD = configutil.getString("Password");
		connect();
	}

	public void connect() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE, USER, PASSWORD);
			update("CREATE TABLE IF NOT EXISTS ffa_players(UUID varchar(64), NAME varchar(64), KILLS long, DEATHS long);");
			if (english) {
				Bukkit.getConsoleSender().sendMessage(PREFIX + "§aThe connection with MySQL has been established!");
			} else {
				Bukkit.getConsoleSender().sendMessage(PREFIX + "§aDie Verbindung mit MySQL wurde hergestellt!");
			}
			connected = true;
		} catch (SQLException e) {
			if (english) {
				Bukkit.getConsoleSender()
						.sendMessage(PREFIX + "§cThe connection to MySQL failed! §4Error: " + e.getMessage());
			} else {
				Bukkit.getConsoleSender().sendMessage(
						PREFIX + "§cDie Verbindung mit MySQL ist fehlgeschlagen! §4Fehler: " + e.getMessage());
			}
			connected = false;
			Main.getInstance().disabled = false;
		}
	}

	public void close() {
		try {
			if (con != null) {
				con.close();
				if (english) {
					Bukkit.getConsoleSender().sendMessage(PREFIX + "§cThe connection to MySQL was terminated!");
				} else {
					Bukkit.getConsoleSender().sendMessage(PREFIX + "§cDie Verbindung mit MySQL wurde beendet!");
				}
				connected = false;
			}
		} catch (SQLException e) {
			if (english) {
				Bukkit.getConsoleSender().sendMessage(
						PREFIX + "§cThe connection to MySQL could not be terminated! §4Error: " + e.getMessage());
			} else {
				Bukkit.getConsoleSender().sendMessage(
						PREFIX + "§cDie Verbindung mit MySQL konnte nicht beendet werden! §4Fehler: " + e.getMessage());
			}
			connected = false;
			Main.getInstance().disabled = false;
		}
	}

	public void update(String qre) {
		if (!(Main.getInstance().disabled)) {
			CompletableFuture.runAsync(() -> {
				if (con != null) {
					try {
						PreparedStatement st = con.prepareStatement(qre);
						st.executeUpdate();
						st.close();
					} catch (SQLException e) {
						connect();
						System.err.print(e);
					}
				}
			});
		} else {
			if (con != null) {
				try {
					PreparedStatement st = con.prepareStatement(qre);
					st.executeUpdate();
					st.close();
				} catch (SQLException e) {
					connect();
					System.err.print(e);
				}
			}
		}
	}

	public ResultSet query(String qre) {
		if (con != null) {
			ResultSet rs = null;
			try {
				PreparedStatement st = con.prepareStatement(qre);
				rs = st.executeQuery();
			} catch (SQLException e) {
				connect();
				System.err.print(e);
			}
			return rs;
		}
		return null;
	}
}
