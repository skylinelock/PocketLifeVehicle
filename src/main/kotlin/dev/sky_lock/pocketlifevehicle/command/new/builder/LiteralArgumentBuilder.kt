package dev.sky_lock.pocketlifevehicle.command.new.builder

import dev.sky_lock.pocketlifevehicle.command.new.node.LiteralCommandNode

/**
 * @author sky_lock
 */

class LiteralArgumentBuilder(val literal: String) : ArgumentBuilder() {
    val aliases = mutableListOf<String>()
    var description = ""

    fun alias(vararg aliases: String): LiteralArgumentBuilder {
        // TODO("when alias duplicated")
        this.aliases.addAll(aliases)
        return getThis()
    }

    fun description(description: String): LiteralArgumentBuilder {
        this.description = description
        return getThis()
    }


    override fun build(): LiteralCommandNode {
        val result = LiteralCommandNode(
            this.literal,
            this.aliases,
            this.description,
            this.permission,
            this.runnable)
        this.rootNode.getChildNodes().forEach(result::addChild)
        return result
    }

    override fun getThis(): LiteralArgumentBuilder {
        return this
    }
}