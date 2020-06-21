package dev.sky_lock.pocketlifevehicle.command.lib.node

import dev.sky_lock.pocketlifevehicle.command.lib.ICommand

/**
 * @author sky_lock
 */

abstract class BukkitCommandNode (val command: ICommand?){

    abstract val name: String
    val children = LinkedHashMap<String, BukkitCommandNode>()
    val literals = LinkedHashMap<String, LiteralCommandNode>()
    val arguments = LinkedHashMap<String, ArgumentCommandNode<Any>>()

    fun findChild(name: String): BukkitCommandNode? {
        return this.children[name]
    }

    fun addChild(node: BukkitCommandNode) {
        val child = this.children[node.name.toLowerCase()]
        if (child == null) {
            this.children[node.name] = node
            if (child is LiteralCommandNode) {
                this.literals[node.name] = node as LiteralCommandNode
            } else if (child is ArgumentCommandNode<*>) {
                this.arguments[node.name] = node as ArgumentCommandNode<Any>
            }
        } else {
            TODO("when child is already exists")
        }
    }

    fun getChildNodes(): Collection<BukkitCommandNode> {
        return this.children.values
    }
}