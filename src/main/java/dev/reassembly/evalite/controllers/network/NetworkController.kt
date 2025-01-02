package dev.reassembly.evalite.controllers.network

import com.github.shynixn.mccoroutine.bukkit.launch
import dev.reassembly.evalite.evalite
import dev.reassembly.evalite.models.PunishmentData
import dev.reassembly.evalite.models.PunishmentType
import dev.reassembly.evalite.utils.GeneralUtils.toMiniMessage
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID

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

    /**
     * Kicks a player from the network if they are online.
     * @param uuid the UUID of the player
     * @param reason the reason for the kick
     *
     */
    fun kickPlayer(uuid: UUID, reason: String) {
        val player = Bukkit.getPlayer(uuid) ?: return
        player.kick(reason.toMiniMessage())
    }

    /**
     * Kicks a player from the network if they are online.
     * @param playerName the name of the player
     * @param reason the reason for the kick
     * @param punisher the player who issued the kick
     *
     */
    fun kickPlayer(playerName: String, reason: String) {
        val player = Bukkit.getPlayerExact(playerName) ?: return
        player.kick(reason.toMiniMessage())
    }

    fun broadcastGlobal(msg: Component) {
        Bukkit.broadcast(msg)
    }


}