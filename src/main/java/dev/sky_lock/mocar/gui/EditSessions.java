package dev.sky_lock.mocar.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author sky_lock
 */

public class EditSessions {
    private static final Map<UUID, EditModelData> edits = new HashMap<>();

    public static void newSession(UUID uuid) {
        if (!edits.containsKey(uuid)) {
            edits.put(uuid, new EditModelData());
        }
    }

    public static void destroy(UUID uuid) {
        edits.remove(uuid);
    }

    public static EditModelData get(UUID uuid) {
        return edits.get(uuid);
    }

}
