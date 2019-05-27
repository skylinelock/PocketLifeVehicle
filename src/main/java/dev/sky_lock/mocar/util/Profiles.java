package dev.sky_lock.mocar.util;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author sky_lock
 */

public class Profiles {

    public static UUID getUUID(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            return player.getUniqueId();
        }
        String apiUri = "https://api.mojang.com/users/profiles/minecraft/" + name;
        try {
            String uuidJson = IOUtils.toString(new URL(apiUri), StandardCharsets.UTF_8);
            if (uuidJson.isEmpty()) {
                return null;
            }
            JSONObject uuid = (JSONObject) JSONValue.parseWithException(uuidJson);
            return toUUID(String.valueOf(uuid.get("id")));
        } catch (ParseException | IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static UUID toUUID(String plain) {
        plain = plain.substring(0, 8) + "-" + plain.substring(8, 12) + "-" + plain.substring(12, 16) + "-" + plain.substring(16, 20) + "-" + plain.substring(20, 32);
        return UUID.fromString(plain);
    }

    public static String getName(UUID player) {
        return Bukkit.getOfflinePlayer(player).getName();
    }
}
