package dev.sky_lock.pocketlifevehicle.item

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

/**
 * @author sky_lock
 */
class ItemStackBuilder private constructor(itemStack: ItemStack) {
    private val itemStack: ItemStack = itemStack.clone()
    fun customModelData(id: Int): ItemStackBuilder {
        val meta = requireNotNull(itemStack.itemMeta)
        meta.setCustomModelData(id)
        itemStack.itemMeta = meta
        return this
    }

    fun lore(vararg lore: String): ItemStackBuilder {
        this.lore(listOf(*lore))
        return this
    }

    fun lore(lore: List<String>): ItemStackBuilder {
        val meta = requireNotNull(itemStack.itemMeta)
        meta.lore = lore
        itemStack.itemMeta = meta
        return this
    }

    fun name(name: String): ItemStackBuilder {
        val meta = requireNotNull(itemStack.itemMeta)
        meta.setDisplayName(name)
        itemStack.itemMeta = meta
        return this
    }

    fun unbreakable(unbreakable: Boolean): ItemStackBuilder {
        val meta = requireNotNull(itemStack.itemMeta)
        meta.isUnbreakable = unbreakable
        itemStack.itemMeta = meta
        return this
    }

    fun itemFlags(vararg flags: ItemFlag): ItemStackBuilder {
        val meta = requireNotNull(itemStack.itemMeta)
        meta.addItemFlags(*flags)
        itemStack.itemMeta = meta
        return this
    }

    fun <T, Z> persistentData(key: NamespacedKey, type: PersistentDataType<T, Z>, obj: Z): ItemStackBuilder {
        val meta = requireNotNull(itemStack.itemMeta)
        val container = meta.persistentDataContainer
        container.set(key, type, obj)
        itemStack.itemMeta = meta;
        return this
    }

    fun glow(): ItemStackBuilder {
        val itemMeta = requireNotNull(itemStack.itemMeta)
        itemMeta.addEnchant(GlowEnchantment(), 1, true)
        itemStack.itemMeta = itemMeta
        return this
    }

    fun build(): ItemStack {
        return itemStack
    }

    companion object {
        @JvmStatic
        fun of(type: Material, amount: Int): ItemStackBuilder {
            require(type != Material.AIR) { "Material cannot be Material.AIR" }
            return ItemStackBuilder(ItemStack(type, amount))
        }

        @JvmStatic
        fun of(itemStack: ItemStack): ItemStackBuilder {
            require(itemStack.type != Material.AIR) { "Material cannot be Material.AIR" }
            return ItemStackBuilder(itemStack)
        }
    }

}