package dev.sky_lock.pocketlifevehicle.command.new.context

/**
 * @author sky_lock
 */

class CommandContextBuilder {
    private val arguments: MutableMap<String, ParsedArgument> = HashMap()

    fun withArgument(name: String, parsed: ParsedArgument) {
        arguments[name] = parsed
    }

/*    fun build(): CommandContext {
        return CommandContext()
    }*/
}