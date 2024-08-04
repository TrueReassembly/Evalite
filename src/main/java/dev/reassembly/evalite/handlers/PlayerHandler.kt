package dev.reassembly.evalite.handlers

import dev.reassembly.evalite.evalite
import dev.reassembly.evalite.models.UserData
import org.bukkit.entity.Player

class PlayerHandler {

    val playerData = mutableMapOf<String, UserData>()

    suspend fun loadPlayer(player: Player) {

        val data = evalite.database.userController.getUser(player.uniqueId.toString())
        if (data != null) {
            data.currentUsername = player.name
            data.currentIP = player.address.address.hostAddress
            playerData[player.uniqueId.toString()] = data
        } else {
            val newData = UserData(
                player.uniqueId.toString(),
                player.name,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                null,
                player.address.hostString,
                mutableListOf()
            )
            evalite.database.userController.create(newData)
            playerData[player.uniqueId.toString()] = newData
        }
    }

    fun savePlayer(player: Player) {
        val data = playerData[player.uniqueId.toString()]

    }
}