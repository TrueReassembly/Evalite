package dev.reassembly.evalite.listeners

import dev.reassembly.evalite.evalite
import com.github.shynixn.mccoroutine.bukkit.launch
import dev.reassembly.evalite.utils.GeneralUtils
import dev.reassembly.evalite.utils.GeneralUtils.toMiniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent

class PlayerListeners : Listener {

    @EventHandler
    suspend fun onPreLogin(event: AsyncPlayerPreLoginEvent) {
        val mostRecentBan = evalite.punishmentHandler.getActiveBan(event.uniqueId.toString())
        if (mostRecentBan != null) {
            val punisher = evalite.server.getOfflinePlayer(mostRecentBan.modUUID).name ?: "a staff member"
            event.disallow(
                AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                (evalite.messagesConfig.getString("punishments.ban-message")
                    ?: "%prefix% <red>You are banned from this server.<newline><newline>Reason: <dark_purple>%reason%<newline><red>Your punishment will expire in <dark_purple>%time-remaining%<newline><newline><white>If you believe this is a mistake, please appeal on our discord at <dark_purple>%discord%")
                    .replace("%reason%", mostRecentBan.reason)
                    .replace("%expires%", mostRecentBan.expiresAt.toString())
                    .replace("%punisher%", punisher)
                    .replace(
                        "%time-remaining%",
                        if (mostRecentBan.expiresAt == null) "Permanent" else GeneralUtils.millisToExpanded(
                            mostRecentBan.expiresAt - System.currentTimeMillis()
                        )
                    )
                    .replace("%player%", event.name)
                    .replace("%reason%", mostRecentBan.reason)
                    .replace("%duration%",
                        if (mostRecentBan.expiresAt == null) "Permanent" else GeneralUtils.millisToExpanded(
                        mostRecentBan.expiresAt - System.currentTimeMillis()
                    ))
                    .toMiniMessage()
            )
        }
    }

    @EventHandler
    suspend fun onJoin(event: PlayerJoinEvent) {
        val message = evalite.messagesConfig.getStringList("join.join-message")
        for (line in message) {
            event.player.sendMessage(line.replace("%player%", event.player.name).toMiniMessage())
        }
        evalite.playerHandler.loadPlayer(event.player)
    }
}
