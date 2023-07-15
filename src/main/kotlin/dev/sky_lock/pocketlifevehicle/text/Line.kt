package dev.sky_lock.pocketlifevehicle.text

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.StyleBuilderApplicable
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit

class Line {
    private val component = Component.text()
    private val legacy = LegacyComponentSerializer.legacyAmpersand()

    private fun appendStyledComponent(text: String, vararg applicable: StyleBuilderApplicable) {
        component.append(Component.text(text, Style.style(*applicable)))
    }

    fun raw(text: String): Line {
        appendStyledComponent(text)
        return this
    }

    fun yellow(text: String): Line {
        appendStyledComponent(text, NamedTextColor.YELLOW)
        return this
    }

    fun red(text: String): Line {
        appendStyledComponent(text, NamedTextColor.RED)
        return this
    }

    fun green(text: String): Line {
        appendStyledComponent(text, NamedTextColor.GREEN)
        return this
    }

    fun darkGreen(text: String): Line {
        appendStyledComponent(text, NamedTextColor.DARK_GREEN)
        return this
    }

    fun white(text: String): Line {
        appendStyledComponent(text, NamedTextColor.WHITE)
        return this
    }

    fun gold(text: String): Line {
        appendStyledComponent(text, NamedTextColor.GOLD)
        return this
    }

    fun aqua(text: String): Line {
        appendStyledComponent(text, NamedTextColor.AQUA)
        return this
    }

    fun darkAqua(text: String): Line {
        appendStyledComponent(text, NamedTextColor.DARK_AQUA)
        return this
    }

    fun gray(text: String): Line {
        appendStyledComponent(text, NamedTextColor.GRAY)
        return this
    }

    fun darkGray(text: String): Line {
        appendStyledComponent(text, NamedTextColor.DARK_GRAY)
        return this
    }

    fun grayBold(text: String): Line {
        appendStyledComponent(text, NamedTextColor.GRAY, TextDecoration.BOLD)
        return this
    }

    fun darkPurpleBold(text: String): Line {
        appendStyledComponent(text, NamedTextColor.DARK_PURPLE, TextDecoration.BOLD)
        return this
    }

    fun darkGreenBold(text: String): Line {
        appendStyledComponent(text, NamedTextColor.DARK_GREEN, TextDecoration.BOLD)
        return this
    }

    fun goldBold(text: String): Line {
        appendStyledComponent(text, NamedTextColor.GOLD, TextDecoration.BOLD)
        return this
    }

    fun aquaBold(text: String): Line {
        appendStyledComponent(text, NamedTextColor.AQUA, TextDecoration.BOLD)
        return this
    }

    fun redBold(text: String): Line {
        appendStyledComponent(text, NamedTextColor.RED, TextDecoration.BOLD)
        return this
    }

    fun greenBold(text: String): Line {
        appendStyledComponent(text, NamedTextColor.GREEN, TextDecoration.BOLD)
        return this
    }

    fun yellowBold(text: String): Line {
        appendStyledComponent(text, NamedTextColor.YELLOW, TextDecoration.BOLD)
        return this
    }

    fun colorCoded(text: String): Line {
        component.append(legacy.deserialize(text))
        return this
    }

    fun broadcast() {
        Bukkit.broadcast(toComponent())
    }

    fun connect(line: Line): Line {
        component.append(line.toComponent())
        return this
    }

    fun toComponent(): Component {
        return component.build()
    }

    fun toColorCodedText(): String {
        return legacy.serialize(toComponent())
    }

}