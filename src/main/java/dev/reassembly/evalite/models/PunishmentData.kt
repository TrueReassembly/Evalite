package dev.reassembly.evalite.models

import gg.ingot.iron.annotations.Column
import gg.ingot.iron.annotations.Model

@Model
data class PunishmentData(
    val id: String,
    val targetUUID: String,
    val modUUID: String,
    val type: PunishmentType,
    val reason: String,
    val ladder: String?,
    val level: Short,
    val issuedAt: Long?,
    val lastModifiedAt: Long?,
    val lastModifiedByUUID: String?,
    val expiresAt: Long?,
    val unpunishedByUUID: String?,
    val unpunishedReason: String?,
    val unpunishedAt: Long?,
    @Column(json = true)
    val chatSnapshot: String?,
    val active: Boolean
) {
    constructor(
        targetUUID: String,
        modUUID: String,
        type: PunishmentType,
        reason: String,
        ladder: String?,
        level: Short,
        issuedAt: Long?,
        lastModifiedAt: Long?,
        lastModifiedByUUID: String?,
        expiresAt: Long?,
        unpunishedByUUID: String?,
        unpunishedReason: String?,
        unpunishedAt: Long?,
        chatSnapshot: String?,
        active: Boolean
    ) : this(
        "",
        targetUUID,
        modUUID,
        type,
        reason,
        ladder,
        level,
        issuedAt,
        lastModifiedAt,
        lastModifiedByUUID,
        expiresAt,
        unpunishedByUUID,
        unpunishedReason,
        unpunishedAt,
        chatSnapshot,
        active
    )
}

enum class PunishmentType {
    WARN,
    KICK,
    MUTE,
    BAN,
    BLACKLIST
}
