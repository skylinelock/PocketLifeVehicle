package dev.sky_lock.pocketlifevehicle.command.new

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import dev.sky_lock.pocketlifevehicle.command.new.builder.ArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.new.builder.LiteralArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.new.builder.RequiredArgumentBuilder


/**
 * @author sky_lock
 */

interface ICommand {

    val permissionMessage: String
    val builder: ArgumentBuilder

    fun literal(literal: String): LiteralArgumentBuilder {
        return LiteralArgumentBuilder(literal)
    }

    fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<T> {
        return RequiredArgumentBuilder(name, type)
    }

    fun integer(): IntegerArgumentType {
        return IntegerArgumentType.integer()
    }

    fun player(): PlayerArgumentType {
        return PlayerArgumentType.player()
    }
}