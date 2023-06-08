package dev.sky_lock.pocketlifevehicle

import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions.strings
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder
import dev.jorel.commandapi.arguments.LocationType
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.kotlindsl.*
import dev.sky_lock.pocketlifevehicle.inventory.impl.InventoryModelList
import dev.sky_lock.pocketlifevehicle.text.ext.sendVehiclePrefixedErrorMessage
import dev.sky_lock.pocketlifevehicle.text.ext.sendVehiclePrefixedRawMessage
import dev.sky_lock.pocketlifevehicle.text.ext.sendVehiclePrefixedSuccessMessage
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class CommandManager {
    fun initialize() {
        commandTree("vehicle") {
            withAliases("car")
            withShortDescription("Vehicle Plugin Commands")
            literalArgument("debug") {
                withPermission(Permission.ADMIN_COMMAND.name)
            }
            literalArgument("give") {
                withPermission(Permission.ADMIN_COMMAND.name)
                vehicleModelArgument {
                    playerArgument("player") {
                        playerExecutor { player, args ->
                            val model = args[0] as Model
                            val target = args[1] as Player
                            target.inventory.addItem(model.itemStack)
                            target.sendVehiclePrefixedSuccessMessage("乗り物を受け取りました")
                            player.sendVehiclePrefixedSuccessMessage(target.name + "に" + model.id + "を与えました")
                        }
                    }
                    playerExecutor { player, args ->
                        val model = args[0] as Model
                        player.inventory.addItem(model.itemStack)
                        player.sendVehiclePrefixedSuccessMessage(model.id + "を取得しました")
                    }
                }
            }
            literalArgument("search") {
                playerArgument("player") {
                    withPermission(Permission.ADMIN_COMMAND.name)
                    playerExecutor { player, args ->
                        val location = VehicleManager.getLocation((args[0] as Player).uniqueId)
                        if (location == null) {
                            player.sendVehiclePrefixedErrorMessage("プレイヤーの乗り物の現在地を取得できませんでした")
                            return@playerExecutor
                        }
                        player.sendVehiclePrefixedSuccessMessage(
                                "(world=" + location.world.name + ", " + getLocationString(
                                        location
                                ) + ")"
                        )
                    }
                }
                playerExecutor { player, _ ->
                    val location = VehicleManager.getLocation(player.uniqueId)
                    if (location == null) {
                        player.sendVehiclePrefixedErrorMessage("乗り物の現在地を取得できませんでした")
                        return@playerExecutor
                    }
                    if (location.world == player.location.world) {
                        player.sendVehiclePrefixedSuccessMessage("乗り物は現在(" + getLocationString(location) + ")にあります")
                        return@playerExecutor
                    }
                    player.sendVehiclePrefixedErrorMessage("別ワールドにある乗り物の現在地は取得できません")
                }
            }
            literalArgument("spawn") {
                withPermission(Permission.ADMIN_COMMAND.name)
                vehicleModelArgument {
                    playerArgument("player") {
                        playerExecutor { player, args ->
                            val model = args[0] as Model
                            val target = args[1] as Player
                            if (!VehiclePlugin.instance.pluginConfiguration.isWorldVehicleCanPlaced(target.world)) {
                                player.sendVehiclePrefixedErrorMessage("対象のプレイヤーがいるワールドは乗り物の使用が許可されていません")
                                return@playerExecutor
                            }
                            val name = target.name
                            if (model.flag.eventOnly) {
                                VehicleManager.placeEventVehicle(target.location, model)
                                player.sendVehiclePrefixedSuccessMessage(name + "の位置にイベント専用車両を設置しました")
                                return@playerExecutor
                            }
                            if (!VehicleManager.verifyPlaceableLocation(target.location)) {
                                player.sendVehiclePrefixedErrorMessage("対象のプレイヤーの位置に乗り物を設置することができませんでした")
                                return@playerExecutor
                            }
                            VehicleManager.placeVehicle(target.uniqueId, target.location, model, model.spec.maxFuel)
                            player.sendVehiclePrefixedSuccessMessage("$name に $model.id を渡しました")
                            target.sendVehiclePrefixedSuccessMessage("乗り物を受け取りました")
                        }
                    }
                    locationArgument("position", LocationType.PRECISE_POSITION) {
                        anyExecutor { sender, args ->
                            val model = args[0] as Model
                            val location = args[1] as Location
                            if (!VehiclePlugin.instance.pluginConfiguration.isWorldVehicleCanPlaced(location.world)) {
                                sender.sendVehiclePrefixedErrorMessage("対象のワールドは乗り物の使用が許可されていません")
                                return@anyExecutor
                            }
                            if (!model.flag.eventOnly) {
                                sender.sendVehiclePrefixedErrorMessage("位置指定でスポーンできるのはイベント専用車両だけです")
                                return@anyExecutor
                            }
                            VehicleManager.placeEventVehicle(location, model)
                        }
                    }
                }
                playerExecutor { player, args ->
                    val model = args[1] as Model
                    val location = player.location
                    if (model.flag.eventOnly) {
                        VehicleManager.placeEventVehicle(location, model)
                        player.sendVehiclePrefixedSuccessMessage("イベント専用車両を設置しました")
                        return@playerExecutor
                    }
                    if (!VehicleManager.verifyPlaceableLocation(location)) {
                        player.sendVehiclePrefixedErrorMessage("この位置に乗り物は設置できません")
                        return@playerExecutor
                    }
                    VehicleManager.remove(player.uniqueId)
                    VehicleManager.placeVehicle(player.uniqueId, player.location, model, model.spec.maxFuel)
                    player.sendVehiclePrefixedSuccessMessage("$model.id を取得しました")
                }
            }
            literalArgument("pop") {
                playerArgument("player") {
                    withPermission(Permission.ADMIN_COMMAND.name)
                    playerExecutor { player, args ->
                        val target = args[0] as Player
                        val uuid = target.uniqueId
                        val name = target.name
                        if (VehicleManager.hasVehicle(uuid)) {
                            VehicleManager.pop(uuid)
                            player.sendVehiclePrefixedSuccessMessage("$name の乗り物をアイテム化しました")
                        } else {
                            player.sendVehiclePrefixedErrorMessage("$name の乗り物をアイテム化できませんでした")
                        }
                    }
                }
                playerExecutor { player, _ ->
                    if (VehicleManager.hasVehicle(player.uniqueId)) {
                        VehicleManager.pop(player.uniqueId)
                        player.sendVehiclePrefixedSuccessMessage("乗り物をアイテム化しました")
                    } else {
                        player.sendVehiclePrefixedErrorMessage("乗り物をアイテム化できませんでした")
                    }
                }
            }
            literalArgument("forgive") {
                withPermission(Permission.ADMIN_COMMAND.name)
                playerArgument("player") {
                    playerExecutor { player, args ->
                        val target = args[0] as Player
                        val name = target.name
                        if (VehicleManager.unregisterIllegalParking(target.uniqueId)) {
                            player.sendVehiclePrefixedSuccessMessage("$name の駐車違反登録を解除しました")
                        } else {
                            player.sendVehiclePrefixedErrorMessage("$name は駐車違反登録されていません")
                        }
                    }
                }
                playerExecutor { player, _ ->
                    val name = player.name
                    if (VehicleManager.unregisterIllegalParking(player.uniqueId)) {
                        player.sendVehiclePrefixedSuccessMessage("$name の駐車違反登録を解除しました")
                    } else {
                        player.sendVehiclePrefixedErrorMessage("$name は駐車違反登録されていません")
                    }
                }
            }
            literalArgument("event") {
                withPermission(Permission.ADMIN_COMMAND.name)
                literalArgument("clear") {
                    playerExecutor { player, _ ->
                        VehicleManager.removeEventVehicles()
                        player.sendVehiclePrefixedSuccessMessage("全てのイベント車両を削除しました")
                    }
                }
                literalArgument("unlock") {
                    playerExecutor { player, _ ->
                        VehicleManager.setLockForEventVehicles(false)
                        player.sendVehiclePrefixedSuccessMessage("全てのイベント車両をアンロックしました")
                    }
                }
                literalArgument("lock") {
                    playerExecutor { player, _ ->
                        VehicleManager.setLockForEventVehicles(true)
                        player.sendVehiclePrefixedSuccessMessage("全てのイベント車両をロックしました")
                    }
                }
            }
            literalArgument("model") {
                withPermission(Permission.ADMIN_COMMAND.name)
                playerExecutor { player, _ ->
                    player.openInventory(InventoryModelList(player))
                }
            }
            literalArgument("world") {
                withPermission(Permission.ADMIN_COMMAND.name)
                literalArgument("list") {
                    playerExecutor { player, _ ->
                        val config = VehiclePlugin.instance.pluginConfiguration
                        Bukkit.getWorlds().forEach { world ->
                            if (config.isWorldVehicleCanPlaced(world)) {
                                player.sendVehiclePrefixedSuccessMessage("- " + world.name)
                            } else {
                                player.sendVehiclePrefixedRawMessage("- " + world.name)
                            }
                        }
                    }
                }
                literalArgument("add") {
                    playerExecutor { player, _ ->
                        val config = VehiclePlugin.instance.pluginConfiguration
                        val uid = player.world.uid
                        config.setWorldVehicleCanPlaced(uid, true)
                        player.sendVehiclePrefixedSuccessMessage("このワールドでの乗り物の使用を許可しました")
                    }
                }
                literalArgument("remove") {
                    playerExecutor { player, _ ->
                        val config = VehiclePlugin.instance.pluginConfiguration
                        val uid = player.world.uid
                        config.setWorldVehicleCanPlaced(uid, false)
                        player.sendVehiclePrefixedSuccessMessage("このワールドでの乗り物の使用をできないようにしました")
                    }
                }
                playerExecutor { player, _ ->
                    player.sendVehiclePrefixedErrorMessage("/vehicle world [add|remove|list]")
                }
            }
            literalArgument("reload") {
                withPermission(Permission.ADMIN_COMMAND.name)
                literalArgument("from") {
                    anyExecutor { player, _ ->
                        ModelRegistry.reloadConfig()
                        VehiclePlugin.instance.pluginConfiguration.load()
                        VehiclePlugin.instance.parkingViolationList.load()
                        player.sendVehiclePrefixedSuccessMessage("ディスクからデータを読み込みました")
                    }
                }
                literalArgument("to") {
                    anyExecutor { player, _ ->
                        VehiclePlugin.instance.pluginConfiguration.save()
                        VehiclePlugin.instance.parkingViolationList.save()
                        ModelRegistry.saveToFile()
                        player.sendVehiclePrefixedSuccessMessage("ディスクへデータを保存しました")
                    }
                }
                anyExecutor { player, _ ->
                    player.sendVehiclePrefixedErrorMessage("/vehicle reload [from/to]")
                }
            }
        }
    }

    private inline fun Argument<*>.vehicleModelArgument(optional: Boolean = false, block: Argument<*>.() -> Unit = {}): Argument<*> = then(
        CustomArgument(StringArgument("model")) { info ->
            ModelRegistry.findById(info.input())
                ?: throw CustomArgumentException.fromMessageBuilder(MessageBuilder("Unknown model"))
        }.apply(block).setOptional(optional).replaceSuggestions(strings {
            ModelRegistry.set().map { it.id }.toTypedArray()
        })
    )

    private fun getLocationString(loc: Location): String {
        return "x=" + loc.blockX.toString() + ", y=" + loc.blockY.toString() + ", z=" + loc.blockZ.toString()
    }
}