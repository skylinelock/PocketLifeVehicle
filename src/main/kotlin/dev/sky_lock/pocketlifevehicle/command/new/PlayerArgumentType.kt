package dev.sky_lock.pocketlifevehicle.command.new

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

/**
 * @author sky_lock
 */

class PlayerArgumentType private constructor() : ArgumentType<Player>  {

    companion object {
        fun player(): PlayerArgumentType {
            return PlayerArgumentType()
        }
    }

    override fun <S : Any> listSuggestions(context: CommandContext<S>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        Bukkit.getOnlinePlayers().forEach { player ->
            if (player.name.startsWith(builder.remaining.lowercase())) {
                builder.suggest(player.name)
            }
        }
        return builder.buildFuture()
    }

    override fun parse(p0: StringReader): Player {
        TODO("Not yet implemented")
    }

    override fun getExamples(): MutableCollection<String> {
        return Bukkit.getOnlinePlayers().map { player -> player.name }.toMutableList()
    }
}