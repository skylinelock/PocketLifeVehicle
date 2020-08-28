package dev.sky_lock.pocketlifevehicle.command.new

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import dev.sky_lock.pocketlifevehicle.command.new.builder.ArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.new.builder.LiteralArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.new.builder.RequiredArgumentBuilder


/**
 * @author sky_lock
 */

abstract class CommandBase {

    abstract val permissionMessage: String
    abstract val builder: ArgumentBuilder

    fun literal(literal: String): LiteralArgumentBuilder {
        return LiteralArgumentBuilder(literal)
    }

    fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<T> {
        return RequiredArgumentBuilder(name, type)
    }

    fun integer(): IntegerArgumentType {
        return IntegerArgumentType.integer()
    }

    fun string(): StringArgumentType {
        return StringArgumentType.string()
    }

    fun player(): PlayerArgumentType {
        return PlayerArgumentType.player()
    }
}