package dev.reassembly.evalite.commands.punishments

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import com.github.shynixn.mccoroutine.bukkit.launch
import dev.reassembly.evalite.evalite
import dev.reassembly.evalite.models.PunishmentData
import dev.reassembly.evalite.models.PunishmentType
import dev.reassembly.evalite.utils.GeneralUtils.expandTimeSpan
import dev.reassembly.evalite.utils.GeneralUtils.toMiniMessage
import dev.reassembly.evalite.utils.GeneralUtils.toTimeSpanMillis
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import okhttp3.OkHttpClient
import okhttp3.Request
import org.bukkit.Bukkit
import org.bukkit.entity.Player

@CommandAlias("ban")
@CommandPermission("evalite.staff.ban")
class BanCommand : BaseCommand() {

    @Default
    suspend fun onBan(sender: Player, target: String, duration: String?, reason: String?) {
        evalite.launch {
            val client = OkHttpClient()
            val request = Request.Builder().url("https://api.mojang.com/users/profiles/minecraft/$target?at=${System.currentTimeMillis()}").build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    sender.sendMessage("§cThat player does not exist.")
                    return@launch
                }
                val uuid = response.body!!.string().substring(7, 39)
                val isSilent = reason?.startsWith("-s") ?: false
                val theReason = reason ?: (evalite.messagesConfig.getString("punishments.default-ban-reason") ?: "Breaking the rules")
                val expires = if (duration == null) null else System.currentTimeMillis() + duration.toTimeSpanMillis()

                val punishment = PunishmentData(
                    targetUUID = uuid,
                    modUUID = sender.uniqueId.toString(),
                    type = PunishmentType.BAN,
                    reason = theReason,
                    ladder = null,
                    level = 0,
                    issuedAt = System.currentTimeMillis(),
                    lastModifiedAt = System.currentTimeMillis(),
                    lastModifiedByUUID = sender.uniqueId.toString(),
                    expiresAt = expires,
                    unpunishedByUUID = null,
                    unpunishedReason = null,
                    unpunishedAt = null,
                    chatSnapshot = null,
                    true
                )

                evalite.punishmentHandler.issue(punishment)

                val playerRankColor = "<dark_purple>"
                val punisher = sender.name
                val punisherRankColor = "<dark_purple>"
                val newDuration = duration?.expandTimeSpan() ?: "Permanent"



                // TODO: Make this work cross server when I set up redis
                if (isSilent) {
                    val message = (evalite.messagesConfig.getString("punishments.silent-ban-message") ?: "<light_gray>[SILENT] <dark_purple>%player% <white>was banned by <dark_purple>%punisher%")
                        .replace("%player%", target)
                        .replace("%player_rank_color%", playerRankColor)
                        .replace("%reason%", theReason)
                        .replace("%punisher%", punisher)
                        .replace("%punisher_rank_color%", punisherRankColor)
                        .replace("%duration%", newDuration)
                    Bukkit.broadcast(message.toMiniMessage(), "evalite.staff.silent")

                    val tooltip = (evalite.messagesConfig.getString("punishments.ban-message-tooltip") ?: "<white>Reason: <dark_purple>%reason%")
                        .replace("%player%", target)
                        .replace("%player_rank_color%", playerRankColor)
                        .replace("%reason%", theReason)
                        .replace("%punisher%", punisher)
                        .replace("%punisher_rank_color%", punisherRankColor)
                        .replace("%duration%", newDuration)
                    val parsedMessage = message.toMiniMessage()
                    parsedMessage.hoverEvent(HoverEvent.showText(tooltip.toMiniMessage()))


                } else {
                    val message = (evalite.messagesConfig.getString("punishments.ban-message") ?: "<dark_purple>%player% <white>was banned by <dark_purple>%punisher%")
                        .replace("%player%", target)
                        .replace("%player_rank_color%", playerRankColor)
                        .replace("%reason%", theReason)
                        .replace("%punisher%", punisher)
                        .replace("%punisher_rank_color%", punisherRankColor)
                        .replace("%duration%", newDuration)

                    val tooltip = (evalite.messagesConfig.getString("punishments.ban-message-tooltip") ?: "<white>Reason: <dark_purple>%reason%")
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