package de.zillolp.ffa.profiles;

import org.bukkit.inventory.ItemStack;

public class KitProfil {
	private String arena;
	private ItemStack[] inventory;
	private ItemStack[] armor;

	public KitProfil(String arena, ItemStack[] inv, ItemStack[] armor) {
		this.arena = arena;
		this.inventory = inv;
		this.armor = armor;
	}

	public String getArena() {
		return arena;
	}

	public ItemStack[] getInventory() {
		return inventory;
	}

	public ItemStack[] getArmor() {
		return armor;
	}
}
