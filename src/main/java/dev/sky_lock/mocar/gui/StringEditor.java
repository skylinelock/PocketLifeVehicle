package dev.sky_lock.mocar.gui;

import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
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

    private StringEditor(int windowId, Type editorType, PlayerInventory playerinventory, World world) {
        super(windowId, playerinventory, ContainerAccess.at(world, BlockPosition.ZERO));
        this.checkReachable = false;
        this.editorType = editorType;
    }

    public static void open(Player player, Type editorType, ModelSettingMenu menu) {
        PlayerInventory inventory = ((CraftInventoryPlayer) player.getInventory()).getInventory();
        World world = ((CraftWorld) player.getWorld()).getHandle();
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        int containerId = entityPlayer.nextContainerCounter();
        StringEditor editor = new StringEditor(containerId, editorType, inventory, world);
        editor.setMenu(menu);

        entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, Containers.ANVIL, new ChatMessage("ChangeTheName")));
        entityPlayer.activeContainer = editor;
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
    public void e() {
        super.e();
        this.levelCost.set(0);
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
