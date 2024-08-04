package dev.reassembly.evalite.utils

import dev.reassembly.evalite.evalite
import dev.reassembly.evalite.models.UserData
import org.bukkit.entity.Player

object PlayerExtensions {

    fun Player.getUserData(): UserData {
        return evalite.playerHandler.playerData[this.uniqueId.toString()]!!
    }

    fun Player.getPlayerData(): UserData {
        return evalite.playerHandler.playerData[this.uniqueId.toString()]!!
    }
}