package dev.sky_lock.pocketlifevehicle.task

/**
 * @author sky_lock
 */

/*
class BurnExplosionTask {
    fun run(vehicle: EntityVehicle) {
        object : BukkitRunnable() {
            var count = VehiclePlugin.instance.pluginConfiguration.warningCount()
            var warning = BurnExplosionWarning()
            override fun run() {
                if (vehicle.passengers.isEmpty()) {
                    if (count == 0) {
                        vehicle.playExplosionEffect()
                        VehicleManager.remove(vehicle)
                        cancel()
                    }
                    count--
                    return
                }
                if (count == 0) {
                    vehicle.playExplosionEffect()
                    vehicle.owner?.let { ownerUuid ->
                        val owner = Bukkit.getPlayer(ownerUuid) ?: return@let
                        if (vehicle.passengers.any { player: Player -> player.uniqueId == ownerUuid }) {
                            return@let
                        }
                        owner.sendVehiclePrefixedErrorMessage( "乗り物が" + vehicle.passengers[0].name + "の運転によって破壊されました")
                    }
                    vehicle.passengers.forEach { player ->
                        warning.stop(player)
                        ((player as CraftPlayer).handle as net.minecraft.world.entity.player.Player).kill()
                    }
                    VehicleManager.remove(vehicle)
                    cancel()
                    return
                }
                warning.count = count
                vehicle.passengers.forEach { player: Player -> warning.send(player) }
                count--
            }
        }.runTaskTimer(VehiclePlugin.instance, 5L, 20L)
    }
}*/