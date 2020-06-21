package dev.sky_lock.pocketlifevehicle.command.lib.builder

import com.mojang.brigadier.arguments.ArgumentType
import dev.sky_lock.pocketlifevehicle.command.lib.node.ArgumentCommandNode
import dev.sky_lock.pocketlifevehicle.command.lib.node.BukkitCommandNode
import dev.sky_lock.pocketlifevehicle.command.lib.node.LiteralCommandNode
import dev.sky_lock.pocketlifevehicle.command.lib.node.RootCommandNode

/**
 * @author sky_lock
 */

class RequiredArgumentBuilder<T>(val name: String, val type: ArgumentType<T>): ArgumentBuilder() {

    override fun build(): BukkitCommandNode {
        val result = ArgumentCommandNode<T>(this.name, command, type)
        this.rootNode.getChildNodes().forEach(result::addChild)
        return result
    }

    override fun getThis(): RequiredArgumentBuilder<T> {
        return this
    }
}