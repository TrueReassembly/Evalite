package dev.reassembly.evalite.controllers.players

import dev.reassembly.evalite.evalite
import dev.reassembly.evalite.models.UserData

class SQLiteUserController : UserController {
    override suspend fun create(
        data: UserData
    ) {
        evalite.database.sqlConnection.prepare(
            """
                INSERT INTO users(uuid, current_username, join_date, last_join, active_grant, current_ip, past_usernames) VALUES (?, ?, ?, ?, ?, ?, ?)
            """.trimIndent(),
            data.uuid,
            data.currentUsername,
            data.joinDate,
            data.lastJoin,
            data.activeGrant,
            data.currentIP,
            data.pastUsernames
        )
    }

    override suspend fun edit(
        uuid: String,
        currentUsername: String,
        joinDate: Long,
        lastJoin: Long,
        activeGrant: String?,
        currentIP: String,
        pastUsernames: MutableList<String>
    ) {
        evalite.database.sqlConnection.prepare(
            """
                UPDATE users SET
                current_username = ?,
                join_date = ?,
                last_join = ?,
                active_grant = ?,
                current_ip = ?,
                past_usernames = ?
                WHERE uuid = ?
            """.trimIndent(),
            currentUsername,
            joinDate,
            lastJoin,
            activeGrant,
            currentIP,
            pastUsernames,
            uuid
        )
    }

    override suspend fun createTable() {
        evalite.database.sqlConnection.prepare(
            """
                CREATE TABLE IF NOT EXISTS users (
                    id TEXT PRIMARY KEY NOT NULL,
                    current_username TEXT NOT NULL,
                    join_date INTEGER NOT NULL,
                    last_join INTEGER NOT NULL,
                    active_grant TEXT,
                    current_ip TEXT NOT NULL,
                    past_usernames TEXT NOT NULL
                )
            """.trimIndent()
        )
    }

    override suspend fun getUser(uuid: String): UserData? {
        return try {
            evalite.database.sqlConnection.prepare("SELECT * FROM users WHERE uuid = ? LIMIT 1;", uuid).single<UserData>()
        } catch (e: IllegalStateException) {
            null
        }
    }
}