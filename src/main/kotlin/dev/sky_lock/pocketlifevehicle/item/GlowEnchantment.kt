package dev.sky_lock.pocketlifevehicle.item

import io.papermc.paper.enchantments.EnchantmentRarity
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */
class GlowEnchantment : Enchantment(NamespacedKey.minecraft("just_glow")) {

    init {
        val field = Enchantment::class.java.getDeclaredField("acceptingNew")
        field.isAccessible = true
        field.set(null, true)
        registerEnchantment(this)
    }

    override fun canEnchantItem(itemStack: ItemStack): Boolean {
        return false
    }

    override fun displayName(level: Int): Component {
        return Component.empty()
    }

    override fun isTradeable(): Boolean {
        return false
    }

    override fun isDiscoverable(): Boolean {
        return false
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.COMMON
    }

    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float {
        return 0.0F
    }

    override fun getActiveSlots(): MutableSet<EquipmentSlot> {
        return mutableSetOf()
    }

    override fun getName(): String {
        return key.namespace
    }

    override fun getMaxLevel(): Int {
        return 0
    }

    override fun getStartLevel(): Int {
        return 0
    }

    override fun getItemTarget(): EnchantmentTarget {
        return EnchantmentTarget.VANISHABLE
    }

    override fun isTreasure(): Boolean {
        return false
    }

    override fun isCursed(): Boolean {
        return false
    }

    override fun conflictsWith(enchantment: Enchantment): Boolean {
        return false
    }

    override fun translationKey(): String {
        return "descriptionId"
    }
}