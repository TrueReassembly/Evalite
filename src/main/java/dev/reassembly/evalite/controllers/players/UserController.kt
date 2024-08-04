package dev.reassembly.evalite.controllers.players

import dev.reassembly.evalite.models.UserData
import javax.annotation.Nullable

interface UserController {

    suspend fun create(data: UserData)

    suspend fun edit(uuid: String, currentUsername: String, joinDate: Long, lastJoin: Long, activeGrant: String?, currentIP: String, pastUsernames: MutableList<String>)

    suspend fun createTable()

    suspend fun getUser(uuid: String): UserData?
}