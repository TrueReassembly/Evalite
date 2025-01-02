package dev.reassembly.evalite.commands.punishments.punishmentimpl

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import dev.reassembly.evalite.commands.punishments.BasePunishmentCommand
import dev.reassembly.evalite.models.PunishmentType
import org.bukkit.entity.Player

@CommandAlias("ban")
@CommandPermission("evalite.staff.ban")
class BanCommand : BasePunishmentCommand(PunishmentType.BAN) {

    @Default
    suspend fun onBan(sender: Player, target: String, reason: String?) {
        issue(target, sender, null, reason)
    }

}