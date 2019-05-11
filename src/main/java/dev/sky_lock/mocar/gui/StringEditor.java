package dev.sky_lock.mocar.gui;


import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author sky_lock
 */

public class StringEditor extends ContainerAnvil {
    private static final Map<UUID, StringEditor> openingMaps = new HashMap<>();
    private final Type editorType;
    private ModelSettingMenu menu;

    private StringEditor(Type editorType, PlayerInventory playerinventory, World world, EntityHuman entityhuman) {
        super(playerinventory, world, BlockPosition.ZERO, entityhuman);
        this.checkReachable = false;
        this.editorType = editorType;
    }

    public static void open(Player player, Type editorType, ModelSettingMenu menu) {
        PlayerInventory inventory = ((CraftInventoryPlayer) player.getInventory()).getInventory();
        World world = ((CraftWorld) player.getWorld()).getHandle();
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        int containerId = entityPlayer.nextContainerCounter();
        StringEditor editor = new StringEditor(editorType, inventory, world, entityPlayer);
        editor.setMenu(menu);

        entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, "minecraft:anvil", new ChatMessage("ChangeTheName"), 0));
        entityPlayer.activeContainer = editor;
        entityPlayer.activeContainer.windowId = containerId;
        entityPlayer.activeContainer.addSlotListener(entityPlayer);

        org.bukkit.inventory.ItemStack itemStack = new org.bukkit.inventory.ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
        meta.setDisplayName(editorType.getName());
        itemStack.setItemMeta(meta);
        ItemStack item = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbt = item.getOrCreateTag();
        nbt.setBoolean("editor-result", true);
/*        NBTTagCompound displayNBT = new NBTTagCompound();
        displayNBT.setString("Name", editor.getEditorType().getName());
        nbt.set("display", displayNBT);*/
        item.setTag(nbt);

        editor.getBukkitView().getTopInventory().setItem(0, CraftItemStack.asBukkitCopy(item));

        openingMaps.put(player.getUniqueId(), editor);
    }

    public ModelSettingMenu getMenu() {
        return menu;
    }

    public void setMenu(ModelSettingMenu menu) {
        this.menu = menu;
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

    public static boolean isOpening(Player player) {
        return openingMaps.keySet().contains(player.getUniqueId());
    }

    public Type getEditorType() {
        return editorType;
    }

    @Override
    public void d() {
        super.d();
        this.levelCost = 0;
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
