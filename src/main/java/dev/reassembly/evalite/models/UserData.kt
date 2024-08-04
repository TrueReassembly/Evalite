package dev.reassembly.evalite.models

import gg.ingot.iron.annotations.Column
import gg.ingot.iron.annotations.Model

@Model
data class UserData(

    val uuid: String,
    var currentUsername: String,
    val joinDate: Long,
    val lastJoin: Long,
    val activeGrant: String?,
    var currentIP: String,
    @Column(json = true)
    val pastUsernames: MutableList<String>


)
