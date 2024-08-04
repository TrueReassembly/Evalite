package dev.reassembly.evalite.controllers.ranks

import dev.reassembly.evalite.models.RankData

interface RankController {

    suspend fun create(data: RankData)

    suspend fun edit(name: String, color: String, prefix: String, suffix: String, priority: Int, permissions: MutableList<String>, inheritsFrom: MutableList<String>)

    suspend fun createTable()
}