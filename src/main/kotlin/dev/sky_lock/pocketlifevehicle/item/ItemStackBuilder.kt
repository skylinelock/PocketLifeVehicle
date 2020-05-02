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
    private  val itemStack: ItemStack = itemStack.clone()
    internal val itemMeta: ItemMeta = itemStack.itemMeta

    constructor(type: Material, amount: Int) : this(ItemStack(type, amount))

    fun customModelData(id: Int): ItemStackBuilder {
        itemMeta.setCustomModelData(id)
        return this
    }

    fun lore(vararg lore: String): ItemStackBuilder {
        this.lore(listOf(*lore))
        return this
    }

    fun lore(lore: List<String>): ItemStackBuilder {
        itemMeta.lore = lore
        return this
    }

    fun name(name: String): ItemStackBuilder {
        itemMeta.setDisplayName(name)
        return this
    }

    fun unbreakable(unbreakable: Boolean): ItemStackBuilder {
        itemMeta.isUnbreakable = unbreakable
        return this
    }

    fun itemFlags(vararg flags: ItemFlag): ItemStackBuilder {
        itemMeta.addItemFlags(*flags)
        return this
    }

    fun <T, Z> persistentData(key: NamespacedKey, type: PersistentDataType<T, Z>, obj: Z): ItemStackBuilder {
        val container = itemMeta.persistentDataContainer
        container.set(key, type, obj)
        return this
    }

    fun glow(): ItemStackBuilder {
        itemMeta.addEnchant(GLOW_ENCHANT, 1, true)
        return this
    }

    fun build(): ItemStack {
        itemStack.itemMeta = itemMeta
        return itemStack
    }

    companion object {
        // 初回時にItemStackBuilderが参照される時にエンチャントを登録する
        val GLOW_ENCHANT = GlowEnchantment()
    }

}