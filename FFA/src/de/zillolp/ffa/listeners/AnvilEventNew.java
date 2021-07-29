package de.zillolp.ffa.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.zillolp.ffa.utils.StringUtil;

public class AnvilEventNew implements Listener {

	@EventHandler
	public void on(PrepareAnvilEvent e) {
		AnvilInventory inv = e.getInventory();
		ItemStack result = e.getResult();
		if (result != null && result.hasItemMeta() && inv.getRenameText() != "") {
			ItemMeta resultMeta = result.getItemMeta();
			resultMeta.setDisplayName(StringUtil.replaceDefaults(inv.getRenameText()));
			result.setItemMeta(resultMeta);
		}
	}
}
