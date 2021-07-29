package de.zillolp.ffa.profiles;

import org.bukkit.inventory.ItemStack;

public class KitProfil {
	private String arena;
	private ItemStack[] inv;
	private ItemStack[] armor;

	public KitProfil(String arena, ItemStack[] inv, ItemStack[] armor) {
		this.arena = arena;
		this.inv = inv;
		this.armor = armor;
	}

	public String getArena() {
		return arena;
	}

	public ItemStack[] getInv() {
		return inv;
	}

	public ItemStack[] getArmor() {
		return armor;
	}
}
