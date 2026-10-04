package com.fluxclient.game.command.manager

import com.fluxclient.game.command.Command
import com.fluxclient.game.command.impl.FriendCommand
import com.fluxclient.game.command.impl.HelpCommand

object CommandManager {

    val commands = mutableListOf<Command>()

    fun init() {
        commands.add(FriendCommand)
        commands.add(HelpCommand)
    }

    fun handleChat(message: String) {
        if (!message.startsWith(".")) return

        val split = message.substring(1).split(" ")
        val name = split[0].lowercase()
        val args = split.drop(1)

        val command = commands.find { it.name == name } ?: return
        command.execute(args)
    }
}
