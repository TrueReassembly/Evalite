package dev.reassembly.evalite.listeners

import dev.reassembly.evalite.evalite
import com.github.shynixn.mccoroutine.bukkit.launch
import dev.reassembly.evalite.utils.GeneralUtils.toMiniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerListeners : Listener {

    @EventHandler
    suspend fun onJoin(event: PlayerJoinEvent) {
        val discordLink = evalite.messagesConfig.getString("social-links.discord") ?: "N/A"
        val twitterLink = evalite.messagesConfig.getString("social-links.twitter") ?: "N/A"
        val websiteLink = evalite.messagesConfig.getString("social-links.website") ?: "N/A"
        val storeLink = evalite.messagesConfig.getString("social-links.store") ?: "N/A"
        val serverName = evalite.messagesConfig.getString("social-links.server-name") ?: "N/A"

        val message = evalite.messagesConfig.getStringList("join.join-message")

        for (line in message) {
            event.player.sendMessage(line
                .replace("%discord%", discordLink)
                .replace("%twitter%", twitterLink)
                .replace("%website%", websiteLink)
                .replace("%store%", storeLink)
                .replace("%player%", event.player.name)
                .replace("%server-name%", serverName)
                .toMiniMessage()
            )
        }


    }
}