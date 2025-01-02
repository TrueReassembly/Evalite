package dev.reassembly.evalite.commands.punishments

import co.aikar.commands.BaseCommand
import com.github.shynixn.mccoroutine.bukkit.launch
import dev.reassembly.evalite.evalite
import dev.reassembly.evalite.models.PunishmentData
import dev.reassembly.evalite.models.PunishmentType
import dev.reassembly.evalite.utils.GeneralUtils
import dev.reassembly.evalite.utils.GeneralUtils.expandTimeSpan
import dev.reassembly.evalite.utils.GeneralUtils.toMiniMessage
import dev.reassembly.evalite.utils.GeneralUtils.toTimeSpanMillis
import net.kyori.adventure.text.event.HoverEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import org.bukkit.Bukkit
import org.bukkit.entity.Player

abstract class BasePunishmentCommand(val type: PunishmentType): BaseCommand() {

    fun issue(target: String, moderator: Player, duration: String?, reason: String?) {
        val client = OkHttpClient()
        val request = Request.Builder().url("https://api.mojang.com/users/profiles/minecraft/$target").build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                moderator.sendMessage("§cThat player does not exist.")
                return@use
            }
            val uuid = GeneralUtils.toDashedUUID(response.body!!.string().substring(12, 44))
            val isSilent = evalite.config.getBoolean("punishments.force-silent") || reason?.startsWith(
                "-s",
                ignoreCase = true
            ) == false
            var theReason =
                reason?.removePrefix("-s") ?: (evalite.messagesConfig.getString("punishments.default-ban-reason")
                    ?: "Breaking the rules")
            val expires = if (duration == null) {
                null
            } else {
                (System.currentTimeMillis() + duration.toTimeSpanMillis())
            }

            val punishment = PunishmentData(
                targetUUID = uuid,
                modUUID = moderator.uniqueId.toString(),
                type = type,
                reason = theReason,
                ladder = null,
                level = 0,
                issuedAt = System.currentTimeMillis(),
                lastModifiedAt = System.currentTimeMillis(),
                lastModifiedByUUID = moderator.uniqueId.toString(),
                expiresAt = expires,
                unpunishedByUUID = null,
                unpunishedReason = null,
                unpunishedAt = null,
                chatSnapshot = null,
                true
            )

            evalite.launch {
                evalite.punishmentHandler.issue(punishment)
                val kickableTypes =
                    listOf<PunishmentType>(PunishmentType.KICK, PunishmentType.BAN, PunishmentType.TEMP_BAN)
                if (kickableTypes.contains(type)) {

                    val reason =

                    evalite.networkHandler.kickPlayer(target, )
                }


                val playerRankColor = "<dark_purple>"
                val punisher = moderator.name
                val punisherRankColor = "<dark_purple>"
                val newDuration = duration?.expandTimeSpan() ?: "Permanent"


                // TODO: Make this work cross server when I set up redis
                if (isSilent) {
                    val message = (evalite.messagesConfig.getString("punishments.silent-ban-message")
                        ?: "<light_gray>[SILENT] <dark_purple>%player% <white>was banned by <dark_purple>%punisher%")
                        .replace("%player%", target)
                        .replace("%player_rank_color%", playerRankColor)
                        .replace("%reason%", theReason)
                        .replace("%punisher%", punisher)
                        .replace("%punisher_rank_color%", punisherRankColor)
                        .replace("%duration%", newDuration)
                        .toMiniMessage()


                    val tooltip = (evalite.messagesConfig.getString("punishments.ban-message-tooltip")
                        ?: "<white>Reason: <dark_purple>%reason%")
                        .replace("%player%", target)
                        .replace("%player_rank_color%", playerRankColor)
                        .replace("%reason%", theReason)
                        .replace("%punisher%", punisher)
                        .replace("%punisher_rank_color%", punisherRankColor)
                        .replace("%duration%", newDuration)
                    message.hoverEvent(HoverEvent.showText(tooltip.toMiniMessage()))
                    Bukkit.broadcast(message, "evalite.staff.silent")

                } else {
                    val message = (evalite.messagesConfig.getString("punishments.ban-message")
                        ?: "<dark_purple>%player% <white>was banned by <dark_purple>%punisher%")
                        .replace("%player%", target)
                        .replace("%player_rank_color%", playerRankColor)
                        .replace("%reason%", theReason)
                        .replace("%punisher%", punisher)
                        .replace("%punisher_rank_color%", punisherRankColor)
                        .replace("%duration%", newDuration)

                    val tooltip = (evalite.messagesConfig.getString("punishments.ban-message-tooltip")
                        ?: "<white>Reason: <dark_purple>%reason%")
                        .replace("%player%", target)
                        .replace("%player_rank_color%", playerRankColor)
                        .replace("%reason%", theReason)
                        .replace("%punisher%", punisher)
                        .replace("%punisher_rank_color%", punisherRankColor)
                        .replace("%duration%", newDuration)
                    val parsedMessage = message.toMiniMessage()
                    parsedMessage.hoverEvent(HoverEvent.showText(tooltip.toMiniMessage()))
                    Bukkit.broadcast(parsedMessage)
                }
            }
        }
    }
}