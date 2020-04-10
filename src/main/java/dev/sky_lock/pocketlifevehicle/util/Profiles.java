package dev.sky_lock.pocketlifevehicle.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.sky_lock.pocketlifevehicle.PLVehicle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * @author sky_lock
 */

public class Profiles {

    public static UUID fetchUUID(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            return player.getUniqueId();
        }
        String urlString = "https://api.mojang.com/users/profiles/minecraft/" + name;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                return null;
            }
            InputStream inputStream = connection.getInputStream();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String jsonText = reader.readLine();
                JsonObject json = new JsonParser().parse(jsonText).getAsJsonObject();
                return toUUID(json.get("id").getAsString());
            }
        } catch (IOException ex) {
            PLVehicle.getInstance().getLogger().warning("Could not fetch an uuid for " + name);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    private static UUID toUUID(String plain) {
        plain = plain.substring(0, 8) + "-" + plain.substring(8, 12) + "-" + plain.substring(12, 16) + "-" + plain.substring(16, 20) + "-" + plain.substring(20, 32);
        return UUID.fromString(plain);
    }

    public static String getName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }
}
