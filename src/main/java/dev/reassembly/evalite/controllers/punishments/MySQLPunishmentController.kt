package dev.reassembly.evalite.controllers.punishments

import dev.reassembly.evalite.evalite
import dev.reassembly.evalite.models.PunishmentData
import dev.reassembly.evalite.models.PunishmentType

class MySQLPunishmentController : PunishmentController {
    override suspend fun issue(
        punishmentData: PunishmentData
    ) {
        evalite.database.sqlConnection.prepare(
            """
                INSERT INTO punishments(target_uuid, mod_uuid, type, reason, ladder, level) VALUES (?, ?, ?, ?, ?, ?)
            """.trimIndent(),
            punishmentData.targetUUID, punishmentData.modUUID, punishmentData.type, punishmentData.reason, punishmentData.ladder, punishmentData.level
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
            chat_snapshot_uuid = ?
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
            chatSnapshot,
            id
        )
    }

    override suspend fun createTable() {
        evalite.database.sqlConnection.execute(
            """
        CREATE TABLE IF NOT EXISTS punishments (
            id INT AUTO_INCREMENT PRIMARY KEY,
            target_uuid VARCHAR(36),
            mod_uuid VARCHAR(36),
            type VARCHAR(10),
            reason TEXT,
            ladder VARCHAR(255),
            level SMALLINT,
            issued_at DATETIME,
            last_modified_at BIGINT,
            last_modified_by_uuid INT,
            expires_at BIGINT,
            unpunished_by_uuid INT,
            unpunished_reason TEXT,
            unpunished_at BIGINT,
            chat_snapshot_uuid INT,
            FOREIGN KEY(target_uuid) REFERENCES users(id),
            FOREIGN KEY(mod_uuid) REFERENCES users(id),
            FOREIGN KEY(last_modified_by_uuid) REFERENCES users(id),
            FOREIGN KEY(unpunished_by_uuid) REFERENCES users(id),
            FOREIGN KEY(ladder) REFERENCES punishment_ladders(id)
            FOREIGN KEY
        );
        """.trimIndent()
        )
    }
}