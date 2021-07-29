package de.zillolp.ffa.profiles;

import org.bukkit.inventory.Inventory;

public class InventoryProfil {
	private Inventory Arenainv;

	public Inventory getArenainv() {
		return Arenainv;
	}

	public Inventory setArenainv(Inventory arenainv) {
		Arenainv = arenainv;
		return Arenainv;
	}

	public void reloadInventorys() {
		setArenainv(null);
	}
}
