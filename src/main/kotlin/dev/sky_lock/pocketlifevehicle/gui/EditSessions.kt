package dev.sky_lock.pocketlifevehicle.gui

import java.util.*

/**
 * @author sky_lock
 */
object EditSessions {
    private val edits: MutableMap<UUID, ModelOption> = HashMap()
    fun newSession(uuid: UUID) {
        destroy(uuid)
        edits[uuid] = ModelOption()
    }

    fun destroy(uuid: UUID) {
        edits.remove(uuid)
    }

    fun of(uuid: UUID): Optional<ModelOption> {
        return Optional.ofNullable(edits[uuid])
    }
}