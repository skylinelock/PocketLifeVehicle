package dev.sky_lock.pocketlifevehicle.command.new

import dev.sky_lock.pocketlifevehicle.command.new.builder.ArgumentBuilder


/**
 * @author sky_lock
 */

interface ICommand {

    val permissionMessage: String
    val builder: ArgumentBuilder
}