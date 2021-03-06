package de.zillolp.ffa.config.tools;

import de.zillolp.ffa.main.Main;
import de.zillolp.ffa.utils.ConfigUtil;

public class PermissionTools {
	private ConfigUtil configutil;
	private static String ADMIN_PERMISSION;

	public PermissionTools() {
		configutil = Main.getInstance().getConfigCreation().getManager().getNewConfig("permissions.yml");
		ADMIN_PERMISSION = configutil.getString("ADMIN_PERMISSION");
	}

	public static String getADMIN_PERMISSION() {
		return ADMIN_PERMISSION;
	}
}
