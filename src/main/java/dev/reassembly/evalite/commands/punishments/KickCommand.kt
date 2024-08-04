package dev.reassembly.evalite.commands.punishments

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import dev.reassembly.evalite.evalite
import org.bukkit.entity.Player

@CommandAlias("kick")
@CommandPermission("evalite.staff.kick")
class KickCommand {

    // kick <player> [reason] [-s/-p]

    // TODO: Implement silent and public flags
    @Default
    fun onKick(sender: Player, target: String, reasonAndFlags: String) {
        val reason = reasonAndFlags.split(" ").filter { it != "-s" && it != "-p" }.joinToString(" ")
        val isSilent = reasonAndFlags.startsWith("-s")
        val isPublic = reasonAndFlags.startsWith("-p")

        if (evalite.networkHandler.isOnline(target)) {
            evalite.networkHandler.kickPlayer(target, reason)

        } else {
            sender.sendMessage("§cThat player is not online.")
        }
    }
}