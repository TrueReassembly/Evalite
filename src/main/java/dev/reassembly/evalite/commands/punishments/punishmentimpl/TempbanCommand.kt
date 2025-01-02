package dev.reassembly.evalite.commands.punishments.punishmentimpl

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import dev.reassembly.evalite.commands.punishments.BasePunishmentCommand
import dev.reassembly.evalite.models.PunishmentType
import org.bukkit.entity.Player

@CommandAlias("tempban")
@CommandPermission("evalite.staff.tempban")
class TempbanCommand : BasePunishmentCommand(PunishmentType.BAN) {

    @Default
    suspend fun onTempban(sender: Player, target: String, duration: String, reason: String?) {
        issue(target, sender, duration, reason)
    }

}