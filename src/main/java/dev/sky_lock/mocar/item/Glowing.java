package dev.sky_lock.mocar.item;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

/**
 * @author sky_lock
 */

public class Glowing extends Enchantment {

    Glowing() {
        super(NamespacedKey.minecraft("glowing"));
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return false;
    }

    @Override
    public String getName() {
        return getKey().getNamespace();
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    public static void register() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (NoSuchFieldException | IllegalAccessException ex) {

        }
        try {
            Glowing glow = new Glowing();
            Enchantment.registerEnchantment(glow);
        } catch (IllegalArgumentException e){
        }
    }
}
