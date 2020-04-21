package dev.sky_lock.pocketlifevehicle.util

import com.google.gson.JsonParser
import dev.sky_lock.pocketlifevehicle.PLVehicle
import org.bukkit.Bukkit
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
 * @author sky_lock
 */
object Profiles {
    fun fetchUUID(name: String): UUID? {
        val player = Bukkit.getPlayer(name)
        if (player != null) {
            return player.uniqueId
        }
        val urlString = "https://api.mojang.com/users/profiles/minecraft/$name"
        var connection: HttpURLConnection? = null
        try {
            val url = URL(urlString)
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            val status = connection.responseCode
            if (status != HttpURLConnection.HTTP_OK) {
                return null
            }
            val inputStream = connection.inputStream
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                val jsonText = reader.readLine()
                val json = JsonParser().parse(jsonText).asJsonObject
                return toUUID(json["id"].asString)
            }
        } catch (ex: IOException) {
            PLVehicle.instance.logger.warning("Could not fetch an uuid for $name")
        } finally {
            connection?.disconnect()
        }
        return null
    }

    private fun toUUID(plain: String): UUID {
        val uuidStr = plain.substring(0, 8) + "-" + plain.substring(8, 12) + "-" + plain.substring(12, 16) + "-" + plain.substring(16, 20) + "-" + plain.substring(20, 32)
        return UUID.fromString(uuidStr)
    }

    fun getName(uuid: UUID?): String {
        if (uuid == null) {
            return "unknown"
        }
        return Bukkit.getOfflinePlayer(uuid).name ?: "unknown"
    }
}