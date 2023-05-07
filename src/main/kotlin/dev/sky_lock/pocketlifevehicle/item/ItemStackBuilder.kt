package dev.sky_lock.pocketlifevehicle.item

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

/**
 * @author sky_lock
 */
open class ItemStackBuilder constructor(itemStack: ItemStack) {
    private var itemStack: ItemStack = itemStack.clone()
    protected var itemMeta: ItemMeta = itemStack.itemMeta

    constructor(type: Material, amount: Int) : this(ItemStack(type, amount))

    fun setCustomModelData(id: Int): ItemStackBuilder {
        itemMeta.setCustomModelData(id)
        return this
    }

    fun setLore(vararg lore: String): ItemStackBuilder {
        this.setLore(listOf(*lore))
        return this
    }

    fun setLore(lore: List<String>): ItemStackBuilder {
        itemMeta.lore = lore
        return this
    }

    fun addLore(vararg lore: String): ItemStackBuilder {
        val origin = itemMeta.lore?.toTypedArray()
        if (origin == null) {
            this.setLore(*lore)
        } else {
            itemMeta.lore = listOf(*origin, *lore)
        }
        return this
    }

    fun setName(name: String): ItemStackBuilder {
        itemMeta.setDisplayName(name)
        return this
    }

    fun setUnbreakable(unbreakable: Boolean): ItemStackBuilder {
        itemMeta.isUnbreakable = unbreakable
        return this
    }

    fun addItemFlags(vararg flags: ItemFlag): ItemStackBuilder {
        itemMeta.addItemFlags(*flags)
        return this
    }

    fun <T, Z> setPersistentData(key: NamespacedKey, type: PersistentDataType<T, Z>, obj: Z): ItemStackBuilder {
        val container = itemMeta.persistentDataContainer
        container.set(key, type, obj)
        return this
    }

    fun addGlowEffect(): ItemStackBuilder {
        itemMeta.addEnchant(GLOW_ENCHANT, 1, true)
        return this
    }

    fun build(): ItemStack {
        itemStack.itemMeta = itemMeta
        return itemStack
    }

    companion object {
        // ItemStackBuilderが参照された初回にエンチャントを登録する
        val GLOW_ENCHANT = GlowEnchantment()
    }

}