package dev.sky_lock.pocketlifevehicle.item

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */
class GlowEnchantment : Enchantment(NamespacedKey.minecraft("glowing")) {

    init {
        val field = Enchantment::class.java.getDeclaredField("acceptingNew")
        field.isAccessible = true
        field.set(null, true)
        registerEnchantment(this)
    }

    override fun canEnchantItem(itemStack: ItemStack): Boolean {
        return false
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
        return EnchantmentTarget.ALL
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
}