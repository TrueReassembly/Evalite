package dev.reassembly.evalite.models

import gg.ingot.iron.annotations.Column
import gg.ingot.iron.annotations.Model

@Model
data class PunishmentData(
    val id: String,
    @Column("target_uuid")
    val targetUUID: String,
    @Column("mod_uuid")
    val modUUID: String,
    val type: PunishmentType,
    val reason: String,
    val ladder: String?,
    val level: Short,
    @Column("issued_at")
    val issuedAt: Long,
    @Column("last_modified_at")
    val lastModifiedAt: Long?,
    @Column("last_modified_by_uuid")
    val lastModifiedByUUID: String?,
    @Column("expires_at")
    val expiresAt: Long?,
    @Column("unpunished_by_uuid")
    val unpunishedByUUID: String?,
    @Column("unpunished_reason")
    val unpunishedReason: String?,
    @Column("unpunished_at")
    val unpunishedAt: Long?,
    @Column(json = true, name = "chat_snapshot_ud")
    val chatSnapshotId: String?,
    val active: Boolean
) {
    constructor(
        targetUUID: String,
        modUUID: String,
        type: PunishmentType,
        reason: String,
        ladder: String?,
        level: Short,
        issuedAt: Long,
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
    TEMP_MUTE,
    MUTE,
    TEMP_BAN,
    BAN,
    BLACKLIST
}
