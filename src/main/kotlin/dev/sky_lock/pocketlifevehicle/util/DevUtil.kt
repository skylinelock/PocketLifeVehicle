package dev.sky_lock.pocketlifevehicle.util

import dev.sky_lock.pocketlifevehicle.text.Line
import org.bukkit.Bukkit

/**
 * @author sky_lock
 */

object DevUtil {

    fun sendConsoleMessage(text: String) {
        Bukkit.getLogger().info(text)
    }

    fun broadcast(text: String) {
        Line().raw(text).broadcast()
    }

    fun <T> enumerateFields(clazz: Class<T>) {
        sendConsoleMessage("---------Fields for class ' ${clazz.name}'--------------")
        val fields = clazz.declaredFields
        fields.forEach { field ->
            sendConsoleMessage("name: ${field.name}, type: ${field.type}, genericType: ${field.genericType}")
        }
        sendConsoleMessage("------------------------------------------------------")
    }

    fun <T> enumerateMethods(clazz: Class<T>) {
        sendConsoleMessage("---------Methods for class ' ${clazz.name}'--------------")
        val methods = clazz.declaredMethods
        methods.forEach { method ->
            sendConsoleMessage("name: ${method.name}, returnType: ${method.returnType}, parameters: ${method.parameters}")
        }
        sendConsoleMessage("------------------------------------------------------")
    }
}