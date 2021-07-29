package de.zillolp.ffa.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.zillolp.ffa.utils.StringUtil;

public class AnvilEventOld implements Listener {

	@EventHandler
	public void on(InventoryClickEvent e) {
		Inventory inv = e.getClickedInventory();
		if (inv != null && e.getSlot() == 2 && e.getAction() != InventoryAction.NOTHING) {
			if (inv.getType() == InventoryType.ANVIL) {
				ItemStack craftItem = inv.getItem(2);
				if (craftItem != null && craftItem.getType() != Material.AIR) {
					ItemMeta itemmeta = craftItem.getItemMeta();
					itemmeta.setDisplayName(StringUtil.replaceDefaults(itemmeta.getDisplayName()));
					craftItem.setItemMeta(itemmeta);
				}
			}
		}
	}
}
