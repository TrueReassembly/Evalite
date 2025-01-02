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
                INSERT INTO punishments(target_uuid, mod_uuid, type, reason, ladder, level, issued_at, last_modified_at, last_modified_by_uuid, expires_at, unpunished_by_uuid, unpunished_reason, unpunished_at, chat_snapshot_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
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
            punishmentData.chatSnapshotId
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
        chatSnapshot: Int
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
            chat_snapshot_id = ?
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
            chat_snapshot_id INTEGER,
            
            FOREIGN KEY (target_uuid) REFERENCES users(id),
            FOREIGN KEY (mod_uuid) REFERENCES users(id),
            FOREIGN KEY (last_modified_by_uuid) REFERENCES users(id),
            FOREIGN KEY (unpunished_by_uuid) REFERENCES users(id),
            FOREIGN KEY (chat_snapshot_id) REFERENCES chat_snapshots(id)
        );
        """.trimIndent()
        )
    }

    override suspend fun getPunishments(targetUUID: String): List<PunishmentData> {
        val result = evalite.database.sqlConnection.prepare(
            """
                SELECT * FROM punishments WHERE target_uuid=?
            """.trimIndent()
        )

        return result.all<PunishmentData>()
    }

    override suspend fun getPunishments(targetUUID: String, type: PunishmentType): List<PunishmentData> {
        val result = evalite.database.sqlConnection.prepare(
            """
                SELECT * FROM punishments WHERE target_uuid=? AND type=?
            """.trimIndent(),
            targetUUID,
            type.name
        )

        return result.all<PunishmentData>()
    }

    override suspend fun getActivePunishments(targetUUID: String): List<PunishmentData> {
        val result = evalite.database.sqlConnection.prepare(
            """
                SELECT * FROM punishments WHERE target_uuid=? AND ( expires_at > ? OR expires_at IS NULL) 
            """.trimIndent(),
            targetUUID,
            System.currentTimeMillis()
        )

        return result.all<PunishmentData>()
    }

    override suspend fun getWarns(targetUUID: String): List<PunishmentData> {
        val result = evalite.database.sqlConnection.prepare(
            """
                SELECT * FROM punishments WHERE target_uuid=? AND type='WARN'
            """.trimIndent(),
            targetUUID
        )

        return result.all<PunishmentData>()
    }

    override suspend fun getKicks(targetUUID: String): List<PunishmentData> {
        val result = evalite.database.sqlConnection.prepare(
            """
                SELECT * FROM punishments WHERE target_uuid=? AND type='KICK'
            """.trimIndent(),
            targetUUID
        )

        return result.all<PunishmentData>()
    }

    override suspend fun getMutes(targetUUID: String): List<PunishmentData> {
        val result = evalite.database.sqlConnection.prepare(
            """
                SELECT * FROM punishments WHERE target_uuid=? AND type='MUTE' AND type='TEMP_MUTE'
            """.trimIndent(),
            targetUUID
        )

        return result.all<PunishmentData>()
    }

    override suspend fun getBans(targetUUID: String): List<PunishmentData> {
        val result = evalite.database.sqlConnection.prepare(
            """
                SELECT * FROM punishments WHERE target_uuid=? AND type='BAN' AND type='TEMP_BAN'
            """.trimIndent(),
            targetUUID
        )

        return result.all<PunishmentData>()
    }

    override suspend fun getBlacklists(targetUUID: String): List<PunishmentData> {
        val result = evalite.database.sqlConnection.prepare(
            """
                SELECT * FROM punishments WHERE target_uuid=? AND type='BLACKLIST'
            """.trimIndent(),
            targetUUID
        )

        return result.all<PunishmentData>()
    }

    override suspend fun isBanned(targetUUID: String): Boolean {
        val result = evalite.database.sqlConnection.prepare(
            """
                SELECT * FROM punishments WHERE target_uuid=? AND type='BAN' OR (type='TEMP_BAN' AND (expires_at > ?  OR expires_at IS NULL))
            """.trimIndent(),
            targetUUID,
            System.currentTimeMillis()
        )

        return result.all<PunishmentData>().isNotEmpty()
    }

    override suspend fun isMuted(targetUUID: String): Boolean {
        val result = evalite.database.sqlConnection.prepare(
            """
                SELECT * FROM punishments WHERE target_uuid=? AND type='MUTE' AND (expires_at > ?  OR expires_at IS NULL)
            """.trimIndent(),
            targetUUID,
            System.currentTimeMillis()
        )

        return result.all<PunishmentData>().isNotEmpty()
    }

    override suspend fun isBlacklisted(targetUUID: String): Boolean {
        val result = evalite.database.sqlConnection.prepare(
            """
                SELECT * FROM punishments WHERE target_uuid=? AND type='BLACKLIST'
            """.trimIndent(),
            targetUUID,
        )

        return result.all<PunishmentData>().isNotEmpty()
    }

    override suspend fun getActiveBan(targetUUID: String): PunishmentData? {
        val result = evalite.database.sqlConnection.prepare(
            """
                SELECT * FROM punishments WHERE target_uuid=? AND type='BAN' OR (type='TEMP_BAN' AND (expires_at > ?  OR expires_at IS NULL)) LIMIT 1
            """.trimIndent(),
            targetUUID,
            System.currentTimeMillis()
        )

        return result.singleNullable<PunishmentData>()
    }
}