package dev.reassembly.evalite.models

import gg.ingot.iron.annotations.Model

@Model
data class PunishmentLadderData(
    val id: String,
    val reason: String,
    val type: PunishmentType
)
