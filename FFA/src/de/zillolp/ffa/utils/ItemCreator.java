package de.zillolp.ffa.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.zillolp.ffa.xclasses.XMaterial;

public class ItemCreator {

	public static ItemStack createItem(final ItemStack it) {
		final ItemMeta meta = it.getItemMeta();
		it.setItemMeta(meta);
		return it;
	}

	public static ItemStack createItem(final ItemStack it, final ItemFlag flag) {
		final ItemMeta meta = it.getItemMeta();
		meta.addItemFlags(flag);
		it.setItemMeta(meta);
		return it;
	}

	public static ItemStack createItem(final ItemStack it, final boolean enchantment) {
		final ItemMeta meta = it.getItemMeta();
		meta.addEnchant(Enchantment.DURABILITY, 1, enchantment);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		it.setItemMeta(meta);
		return it;
	}

	public static ItemStack createItem(final ItemStack it, final ArrayList<String> lore) {
		final ItemMeta meta = it.getItemMeta();
		meta.setLore(lore);
		it.setItemMeta(meta);
		return it;
	}

	public static ItemStack createItem(final ItemStack it, final ArrayList<String> lore, final boolean enchantment) {
		final ItemMeta meta = it.getItemMeta();
		meta.setLore(lore);
		meta.addEnchant(Enchantment.DURABILITY, 1, enchantment);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		it.setItemMeta(meta);
		return it;
	}

	public static ItemStack createItem(final ItemStack it, final String display) {
		final ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(display);
		it.setItemMeta(meta);
		return it;
	}

	public static ItemStack createItem(final ItemStack it, final String display, final ItemFlag flag) {
		final ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(display);
		meta.addItemFlags(flag);
		it.setItemMeta(meta);
		return it;
	}

	public static ItemStack createItem(final ItemStack it, final String display, final boolean enchantment) {
		final ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(display);
		meta.addEnchant(Enchantment.DURABILITY, 1, enchantment);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		it.setItemMeta(meta);
		return it;
	}

	public static ItemStack createItem(final ItemStack it, final String display, final ArrayList<String> lore) {
		final ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(display);
		meta.setLore(lore);
		it.setItemMeta(meta);
		return it;
	}

	public static ItemStack createItem(final ItemStack it, final String display, final ArrayList<String> lore,
			final boolean enchantment) {
		final ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(display);
		meta.setLore(lore);
		meta.addEnchant(Enchantment.DURABILITY, 1, enchantment);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		it.setItemMeta(meta);
		return it;
	}

	public static ItemStack createItem(final ItemStack it, final String display, final ArrayList<String> lore,
			final ItemFlag flag) {
		final ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(display);
		meta.addItemFlags(flag);
		meta.setLore(lore);
		it.setItemMeta(meta);
		return it;
	}

	public static ItemStack createItem(final ItemStack it, final String display, final ArrayList<String> lore,
			final boolean enchantment, final ItemFlag flag) {
		final ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(display);
		meta.setLore(lore);
		meta.addEnchant(Enchantment.DURABILITY, 1, enchantment);
		meta.addItemFlags(flag);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		it.setItemMeta(meta);
		return it;
	}

	public static ItemStack getSkullByTextureURL(String textureURL) {
		ItemStack head = new ItemStack(XMaterial.PLAYER_HEAD.parseItem());
		try {
			ItemMeta headMeta = head.getItemMeta();
			GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
			gameProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString(
					"{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/" + textureURL + "\"}}}")));
			Field profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, gameProfile);
			head.setItemMeta(headMeta);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return head;
	}

	public static ItemStack getSkullByTextureURL(String textureURL, final String display) {
		ItemStack head = new ItemStack(XMaterial.PLAYER_HEAD.parseItem());
		try {
			ItemMeta headMeta = head.getItemMeta();
			headMeta.setDisplayName(display);
			GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
			gameProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString(
					"{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/" + textureURL + "\"}}}")));
			Field profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, gameProfile);
			head.setItemMeta(headMeta);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return head;
	}

	public static ItemStack getSkullByTextureURL(String textureURL, final String display,
			final ArrayList<String> lore) {
		ItemStack head = new ItemStack(XMaterial.PLAYER_HEAD.parseItem());
		try {
			ItemMeta headMeta = head.getItemMeta();
			headMeta.setDisplayName(display);
			headMeta.setLore(lore);
			GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
			gameProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString(
					"{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/" + textureURL + "\"}}}")));
			Field profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, gameProfile);
			head.setItemMeta(headMeta);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return head;
	}

}
