package de.zillolp.ffa.profiles;

import org.bukkit.inventory.Inventory;

public class InventoryProfil {
	private Inventory arenaInventory;

	public Inventory getArenaInventory() {
		return arenaInventory;
	}

	public Inventory setArenaInventory(Inventory arenainv) {
		arenaInventory = arenainv;
		return arenaInventory;
	}

	public void reloadInventorys() {
		setArenaInventory(null);
	}
	
}
