package de.zillolp.ffa.serializer;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64Serializer {
    public String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        String inventoryContent = toBase64(playerInventory);
        String armorContent = itemStackArrayToBase64(playerInventory.getArmorContents());
        return new String[]{inventoryContent, armorContent};
    }

    public String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
            bukkitObjectOutputStream.writeInt(items.length);
            for (ItemStack item : items) {
                bukkitObjectOutputStream.writeObject(item);
            }
            bukkitObjectOutputStream.close();
            return Base64Coder.encodeLines(byteArrayOutputStream.toByteArray());
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to save item stacks.", exception);
        }
    }

    public String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
            bukkitObjectOutputStream.writeInt(inventory.getSize());
            for (ItemStack item : inventory.getContents()) {
                bukkitObjectOutputStream.writeObject(item);
            }
            bukkitObjectOutputStream.close();
            return Base64Coder.encodeLines(byteArrayOutputStream.toByteArray());
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to save item stacks.", exception);
        }
    }

    public Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, bukkitObjectInputStream.readInt());
            for (int slot = 0; slot < inventory.getSize(); slot++) {
                inventory.setItem(slot, (ItemStack) bukkitObjectInputStream.readObject());
            }
            bukkitObjectInputStream.close();
            return inventory;
        } catch (ClassNotFoundException exception) {
            throw new IOException("Unable to decode class type.", exception);
        }
    }

    public ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);
            ItemStack[] items = new ItemStack[bukkitObjectInputStream.readInt()];
            for (int number = 0; number < items.length; number++) {
                items[number] = (ItemStack) bukkitObjectInputStream.readObject();
            }
            bukkitObjectInputStream.close();
            return items;
        } catch (ClassNotFoundException exception) {
            throw new IOException("Unable to decode class type.", exception);
        }
    }
}
