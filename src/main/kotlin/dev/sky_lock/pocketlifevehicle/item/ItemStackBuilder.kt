package dev.sky_lock.pocketlifevehicle.item

import dev.sky_lock.pocketlifevehicle.extension.chat.Line
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

/**
 * @author sky_lock
 */
open class ItemStackBuilder(itemStack: ItemStack) {
    private var itemStack: ItemStack = itemStack.clone()
    protected var itemMeta: ItemMeta = itemStack.itemMeta

    constructor(type: Material, amount: Int) : this(ItemStack(type, amount))

    fun setCustomModelData(id: Int): ItemStackBuilder {
        itemMeta.setCustomModelData(id)
        return this
    }

    private fun setLore(vararg components: Component): ItemStackBuilder {
        itemMeta.lore(mutableListOf(*components))
        return this
    }

    fun setLore(lines: List<Line>): ItemStackBuilder {
        setLore(*lines.toTypedArray())
        return this
    }

    fun setLore(vararg lines: Line): ItemStackBuilder {
        setLore(*mutableListOf(*lines).map { line -> line.toComponent() }.toTypedArray())
        return this
    }

    fun addLore(vararg lines: Line): ItemStackBuilder {
        val origin = itemMeta.lore()
        if (origin == null) {
            this.setLore(*lines)
        } else {
            val new = ArrayList(origin)
            new.addAll(mutableListOf(*lines).map { line -> line.toComponent() })
            itemMeta.lore(new)
        }
        return this
    }

    fun setName(name: String): ItemStackBuilder {
        itemMeta.setDisplayName(name)
        return this
    }

    fun setName(line: Line): ItemStackBuilder {
        itemMeta.displayName(line.toComponent())
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