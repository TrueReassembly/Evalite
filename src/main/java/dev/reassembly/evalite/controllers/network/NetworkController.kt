package dev.reassembly.evalite.controllers.network

import com.github.shynixn.mccoroutine.bukkit.launch
import dev.reassembly.evalite.evalite
import dev.reassembly.evalite.models.PunishmentData
import dev.reassembly.evalite.utils.GeneralUtils.toMiniMessage
import org.bukkit.Bukkit

class NetworkController {

    /**
     * Checks if the player is online on the network.
     * @param playerName the name of the player
     *
     * @return true if the player is online, false otherwise
     */
    // TODO: Connect this to redis to check if the player is online on the network.
    fun isOnline(playerName: String): Boolean {
        return Bukkit.getPlayer(playerName) != null
    }

    fun kickPlayer(playerName: String, reason: String, ) {
        val player = Bukkit.getPlayer(playerName)
        player?.kick(reason.toMiniMessage())
        /*evalite.launch {
            PunishmentData (
                // TODO: Implement punishment data and find some way to handle ladders in the meantime.
            )
        }*/
    }
}