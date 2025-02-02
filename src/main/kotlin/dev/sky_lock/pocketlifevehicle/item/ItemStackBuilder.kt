package dev.sky_lock.pocketlifevehicle.item

import dev.sky_lock.pocketlifevehicle.text.Line
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
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
        itemMeta.lore(mutableListOf(*components).map { component -> resetDefault(component) })
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

    private fun resetDefault(component: Component): Component {
        return Component.text().decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE).append(component).build()
    }

    fun setName(line: Line): ItemStackBuilder {
        itemMeta.displayName(resetDefault(line.toComponent()))
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

    fun <T, Z : Any> setPersistentData(key: NamespacedKey, type: PersistentDataType<T, Z>, obj: Z): ItemStackBuilder {
        itemMeta.persistentDataContainer.set(key, type, obj)
        return this
    }

    fun addGlowEffect(): ItemStackBuilder {
        itemMeta.addEnchant(Enchantment.OXYGEN, 1, true)
        addItemFlags(ItemFlag.HIDE_ENCHANTS)
        return this
    }

    fun build(): ItemStack {
        itemStack.itemMeta = itemMeta
        return itemStack
    }

}