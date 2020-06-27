package dev.sky_lock.pocketlifevehicle.command.new.node

import dev.sky_lock.pocketlifevehicle.command.new.CommandRunnable
import org.bukkit.permissions.Permission

/**
 * @author sky_lock
 */

abstract class BukkitCommandNode(val permission: Permission?, val runnable: CommandRunnable?) {

    abstract val name: String
    val children = LinkedHashMap<String, BukkitCommandNode>()
    val literals = LinkedHashMap<String, LiteralCommandNode>()
    val arguments = LinkedHashMap<String, ArgumentCommandNode<Any>>()

    fun findChild(name: String): BukkitCommandNode? {
        this.literals.values.forEach { literalNode ->
            val node = literalNode.aliases.find { alias -> name.equals(alias, ignoreCase = true) }
            if (node != null) {
                return literalNode
            }
        }
        return this.children[name]
    }

    fun addChild(node: BukkitCommandNode) {
        val child = this.children[node.name.toLowerCase()]
        if (child == null) {
            this.children[node.name.toLowerCase()] = node
            if (node is LiteralCommandNode) {
                this.literals[node.name] = node as LiteralCommandNode
            } else if (node is ArgumentCommandNode<*>) {
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