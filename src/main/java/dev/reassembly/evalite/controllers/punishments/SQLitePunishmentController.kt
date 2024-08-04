package dev.reassembly.evalite.controllers.punishments

import dev.reassembly.evalite.evalite
import dev.reassembly.evalite.models.PunishmentData
import dev.reassembly.evalite.models.PunishmentType
import gg.ingot.iron.sql.jsonField

class SQLitePunishmentController : PunishmentController {
    override suspend fun issue(
        punishmentData: PunishmentData
    ) {
        evalite.database.sqlConnection.prepare(
            """
                INSERT INTO punishments(target_uuid, mod_uuid, type, reason, ladder, level, issued_at, last_modified_at, last_modified_by_uuid, expires_at, unpunished_by_uuid, unpunished_reason, unpunished_at, chat_snapshot) VALUES (?, ?, ?, ?, ?, ?)
            """.trimIndent(),
            punishmentData.targetUUID,
            punishmentData.modUUID,
            punishmentData.type,
            punishmentData.reason,
            punishmentData.ladder,
            punishmentData.level,
            punishmentData.issuedAt,
            punishmentData.lastModifiedAt,
            punishmentData.lastModifiedByUUID,
            punishmentData.expiresAt,
            punishmentData.unpunishedByUUID,
            punishmentData.unpunishedReason,
            punishmentData.unpunishedAt,
            jsonField(punishmentData.chatSnapshot)
        )
    }

    override suspend fun revoke(id: String) {
        evalite.database.sqlConnection.prepare(
            """
                DELETE FROM punishments WHERE id=?
            """.trimIndent(),
            id
        )
    }

    override suspend fun overwrite(
        id: String,
        targetUUID: String,
        modUUID: String,
        type: PunishmentType,
        reason: String,
        ladder: String,
        level: Short,
        issuedAt: Long,
        lastModifiedAt: Long,
        lastModifiedByUUID: String,
        expiresAt: Long,
        unpunishedByUUID: String,
        unpunishedReason: String,
        unpunishedAt: Long,
        chatSnapshot: String
    ) {
        evalite.database.sqlConnection.prepare(
            """
            UPDATE punishments SET
            target_uuid = ?,
            mod_uuid = ?,
            type = ?,
            reason = ?,
            ladder = ?,
            level = ?,
            issued_at = ?,
            last_modified_at = ?,
            last_modified_by_uuid = ?,
            expires_at = ?,
            unpunished_by_uuid = ?,
            unpunished_reason = ?,
            unpunished_at = ?,
            chat_snapshot = ?
            WHERE id = ?
        """.trimIndent(),
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
            jsonField(chatSnapshot),
            id
        )
    }

    override suspend fun createTable() {
        evalite.database.sqlConnection.execute(
            """
        CREATE TABLE IF NOT EXISTS punishments (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            target_uuid TEXT,
            mod_uuid TEXT,
            type TEXT,
            reason TEXT,
            ladder TEXT,
            level INTEGER,
            issued_at INTEGER,
            last_modified_at INTEGER,
            last_modified_by_uuid TEXT,
            expires_at INTEGER,
            unpunished_by_uuid TEXT,
            unpunished_reason TEXT,
            unpunished_at INTEGER,
            chat_snapshot TEXT,
            
            FOREIGN KEY (target_uuid) REFERENCES users(id),
            FOREIGN KEY (mod_uuid) REFERENCES users(id)
        );
        """.trimIndent()
        )
    }
}