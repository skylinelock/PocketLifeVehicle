package dev.sky_lock.pocketlifevehicle.command.lib.builder

import dev.sky_lock.pocketlifevehicle.command.lib.node.LiteralCommandNode

/**
 * @author sky_lock
 */

class LiteralArgumentBuilder(val literal: String): ArgumentBuilder() {

    override fun build(): LiteralCommandNode {
        val result = LiteralCommandNode(this.literal, this.command)
        this.rootNode.getChildNodes().forEach(result::addChild)
        return result
    }

    override fun getThis(): LiteralArgumentBuilder {
        return this
    }
}