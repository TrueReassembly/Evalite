package dev.reassembly.evalite.controllers.players

import dev.reassembly.evalite.evalite
import dev.reassembly.evalite.models.UserData

class SQLiteUserController : UserController {
    override suspend fun create(
        data: UserData
    ) {
        evalite.database.sqlConnection.prepare(
            """
                INSERT INTO users(id, current_username, join_date, last_join, active_grant, current_ip, past_usernames) VALUES (?, ?, ?, ?, ?, ?, ?)
            """.trimIndent(),
            data.id,
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
                WHERE id = ?
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
                    past_usernames TEXT NOT NULL,
                    
                    FOREIGN KEY (active_grant) REFERENCES grants(id)
                )
            """.trimIndent()
        )
    }

    override suspend fun getUser(uuid: String): UserData? {
        return evalite.database.sqlConnection.prepare("SELECT * FROM users WHERE id = ? LIMIT 1;", uuid).singleNullable<UserData>()
    }
}