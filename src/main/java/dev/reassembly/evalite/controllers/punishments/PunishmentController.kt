package dev.reassembly.evalite.controllers.punishments

import dev.reassembly.evalite.models.PunishmentData
import dev.reassembly.evalite.models.PunishmentType

interface PunishmentController {

    suspend fun issue(punishmentData: PunishmentData)

    suspend fun revoke(id: String)

    suspend fun overwrite(id: String, targetUUID: String, modUUID: String, type: PunishmentType, reason: String, ladder: String, level: Short, issuedAt: Long, lastModifiedAt: Long, lastModifiedByUUID: String, expiresAt: Long, unpunishedByUUID: String, unpunishedReason: String, unpunishedAt: Long, chatSnapshot: String)

    suspend fun createTable()
}

