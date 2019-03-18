package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.util.ItemStackBuilder;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author sky_lock
 */

public class StringEditor extends ContainerAnvil {
    private static final Map<UUID, StringEditor> openingMaps = new HashMap<>();
    private final Type editorType;

    public StringEditor(Type editorType, PlayerInventory playerinventory, World world, EntityHuman entityhuman) {
        super(playerinventory, world, BlockPosition.ZERO, entityhuman);
        this.checkReachable = false;
        this.editorType = editorType;
    }

    public static void open(Player player, Type editorType) {
        PlayerInventory inventory = ((CraftInventoryPlayer) player.getInventory()).getInventory();
        World world = ((CraftWorld) player.getWorld()).getHandle();
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        int containerId = entityPlayer.nextContainerCounter();
        StringEditor editor = new StringEditor(editorType, inventory, world, entityPlayer);

        entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, "minecraft:anvil", new ChatMessage("ChangeTheName", new Object[]{}), 0));

        entityPlayer.activeContainer = editor;
        entityPlayer.activeContainer.windowId = containerId;
        entityPlayer.activeContainer.addSlotListener(entityPlayer);

        org.bukkit.inventory.ItemStack itemStack = new ItemStackBuilder(org.bukkit.Material.STAINED_GLASS_PANE, 1).build();
        ItemStack item = CraftItemStack.asNMSCopy(itemStack);

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("editor-result", true);
        NBTTagCompound displayNBT = new NBTTagCompound();
        displayNBT.setString("Name", editor.getEditorType().getName());
        nbt.set("display", displayNBT);
        item.setTag(nbt);

        editor.getBukkitView().getTopInventory().setItem(0, CraftItemStack.asBukkitCopy(item));

        openingMaps.put(player.getUniqueId(), editor);
    }

    public static StringEditor get(Player player) {
        return openingMaps.get(player.getUniqueId());
    }

    public static void close(Player player) {
        if (!openingMaps.containsKey(player.getUniqueId())) {
            return;
        }
        openingMaps.get(player.getUniqueId()).getBukkitView().getTopInventory().clear();
        openingMaps.remove(player.getUniqueId());
    }

    public Type getEditorType() {
        return editorType;
    }

    public enum Type {
        ID("Id"),
        NAME("Name");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
