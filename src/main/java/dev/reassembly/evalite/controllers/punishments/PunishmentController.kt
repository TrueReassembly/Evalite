package dev.reassembly.evalite.controllers.punishments

import dev.reassembly.evalite.models.PunishmentData
import dev.reassembly.evalite.models.PunishmentType

interface PunishmentController {

    suspend fun issue(punishmentData: PunishmentData)

    /**
     * Completely removes a punishment from the database. This is not the same as unpunishing a player. This should be used for false punishments. If you want to unpunish a player, use [overwrite] and set the expiresAt to [System.currentTimeMillis] with all the unpunished fields.
     */
    suspend fun revoke(id: String)

    suspend fun overwrite(id: String, targetUUID: String, modUUID: String, type: PunishmentType, reason: String, ladder: String, level: Short, issuedAt: Long, lastModifiedAt: Long, lastModifiedByUUID: String, expiresAt: Long, unpunishedByUUID: String, unpunishedReason: String, unpunishedAt: Long, chatSnapshot: Int)

    suspend fun createTable()

    suspend fun getPunishments(targetUUID: String): List<PunishmentData>

    suspend fun getPunishments(targetUUID: String, type: PunishmentType): List<PunishmentData>

    suspend fun getActivePunishments(targetUUID: String): List<PunishmentData>

    suspend fun getWarns(targetUUID: String): List<PunishmentData>

    suspend fun getKicks(targetUUID: String): List<PunishmentData>

    suspend fun getMutes(targetUUID: String): List<PunishmentData>

    suspend fun getBans(targetUUID: String): List<PunishmentData>

    suspend fun getBlacklists(targetUUID: String): List<PunishmentData>

    suspend fun isBanned(targetUUID: String): Boolean

    suspend fun isMuted(targetUUID: String): Boolean

    suspend fun isBlacklisted(targetUUID: String): Boolean

    suspend fun getActiveBan(targetUUID: String): PunishmentData?

}

