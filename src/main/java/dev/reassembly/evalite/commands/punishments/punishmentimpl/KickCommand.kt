package dev.reassembly.evalite.commands.punishments.punishmentimpl

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import dev.reassembly.evalite.commands.punishments.BasePunishmentCommand
import dev.reassembly.evalite.evalite
import dev.reassembly.evalite.models.PunishmentType
import dev.reassembly.evalite.utils.GeneralUtils.toMiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

@CommandAlias("kick")
// @CommandPermission("evalite.staff.kick")
class KickCommand : BasePunishmentCommand(PunishmentType.KICK) {

    // kick <player> [reason] [-s/-p]

    // TODO: When ranks are implemented, add a check where the player cannot kick someone above their rank priority (toggleable in config)
    @Default
    fun onKick(sender: Player, target: String, reasonAndFlags: String) {
        val reason = reasonAndFlags.split(" ").filter { it != "-s" && it != "-p" }.joinToString(" ")
        var isSilent = evalite.config.getBoolean("punishments.silent-by-default")

        if (reasonAndFlags.contains("-s")) {
            isSilent = true
        } else if (reasonAndFlags.contains("-p")) {
            isSilent = false
        }

        if (evalite.networkHandler.isOnline(target)) {
            evalite.networkHandler.kickPlayer(target, reason, sender)
            if (isSilent) {
                val msg = (evalite.messagesConfig.getString("punishments.silent-punishment-message")
                    ?: "<light_gray>[SILENT] <dark_purple>%player% <white>was %type-suffixed% by <dark_purple>%punisher%")
                    .replace("%player%", target)
                    .replace("%type-suffixed%", "kicked")
                    .replace("%punisher%", sender.name)
                    .replace("%reason%", reason)
                    .replace("%type%", "KICK")
                    .toMiniMessage()
                Bukkit.broadcast(msg, evalite.config.getString("punishments.see-silent-punishments-permission") ?: "evalite.staff.silent")
            } else {
                val msg = (evalite.messagesConfig.getString("punishments.punishment-message")
                    ?: "<dark_purple>%player% <white>was %type-suffixed% by <dark_purple>%punisher%")
                    .replace("%player%", target)
                    .replace("%type-suffixed%", "kicked")
                    .replace("%punisher%", sender.name)
                    .replace("%reason%", reason)
                    .replace("%type%", "KICK")
                    .toMiniMessage()
                Bukkit.broadcast(msg)
            }
        } else {
            sender.sendMessage("§cThat player is not online.")
        }
    }
}