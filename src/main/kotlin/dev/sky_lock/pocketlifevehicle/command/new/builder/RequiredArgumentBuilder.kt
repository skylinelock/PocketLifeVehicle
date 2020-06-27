package dev.sky_lock.pocketlifevehicle.command.new.builder

import com.mojang.brigadier.arguments.ArgumentType
import dev.sky_lock.pocketlifevehicle.command.new.node.ArgumentCommandNode
import dev.sky_lock.pocketlifevehicle.command.new.node.BukkitCommandNode

/**
 * @author sky_lock
 */

class RequiredArgumentBuilder<T>(val name: String, val type: ArgumentType<T>): ArgumentBuilder() {

    override fun build(): BukkitCommandNode {
        val result = ArgumentCommandNode(this.name, this.type, this.permission, this.runnable)
        this.rootNode.getChildNodes().forEach(result::addChild)
        return result
    }

    override fun getThis(): RequiredArgumentBuilder<T> {
        return this
    }
}