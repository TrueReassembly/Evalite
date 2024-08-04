package dev.reassembly.evalite.controllers.ranks

import dev.reassembly.evalite.evalite
import dev.reassembly.evalite.models.RankData
import gg.ingot.iron.sql.jsonField

class SQLiteRankController : RankController {
    override suspend fun create(data: RankData) {
        evalite.database.sqlConnection.prepare(
            """
                INSERT INTO ranks(name, color, prefix, suffix, priority, permissions, inherits_from) VALUES (?, ?, ?, ?, ?, ?, ?)
            """.trimIndent(),
            data.name,
            data.color,
            data.prefix,
            data.suffix,
            data.priority,
            jsonField(data.permissions),
            jsonField(data.inheritsFrom)
        )
    }

    override suspend fun edit(
        name: String,
        color: String,
        prefix: String,
        suffix: String,
        priority: Int,
        permissions: MutableList<String>,
        inheritsFrom: MutableList<String>
    ) {
        evalite.database.sqlConnection.prepare(
            """
                UPDATE ranks SET
                color = ?,
                prefix = ?,
                suffix = ?,
                priority = ?,
                permissions = ?,
                inherits_from = ?
                WHERE name = ?
            """.trimIndent(),
            name,
            color,
            prefix,
            suffix,
            priority,
            jsonField(permissions),
            jsonField(inheritsFrom)
        )
    }

    override suspend fun createTable() {
        evalite.database.sqlConnection.prepare(
            """
                CREATE TABLE IF NOT EXISTS ranks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    color TEXT NOT NULL,
                    prefix TEXT NOT NULL,
                    suffix TEXT NOT NULL,
                    priority INTEGER NOT NULL,
                    permissions TEXT NOT NULL,
                    inherits_from TEXT NOT NULL
                )
            """.trimIndent()
        )
    }
}