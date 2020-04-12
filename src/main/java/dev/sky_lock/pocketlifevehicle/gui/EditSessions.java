package dev.sky_lock.pocketlifevehicle.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author sky_lock
 */

public class EditSessions {
    private static final Map<UUID, ModelOption> edits = new HashMap<>();

    public static void newSession(UUID uuid) {
        destroy(uuid);
        edits.put(uuid, new ModelOption());
    }

    public static void destroy(UUID uuid) {
        edits.remove(uuid);
    }

    public static Optional<ModelOption> of(UUID uuid) {
        return Optional.ofNullable(edits.get(uuid));
    }

}