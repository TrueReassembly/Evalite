package dev.reassembly.evalite.models

import gg.ingot.iron.annotations.Model

@Model
data class GrantData(
    val id: String,
    val playerUuid: String,
    val rankerUUID: String,
    val rankUuid: String,
    val reason: String,
    val expiresAt: Long,
    val scope: String
)