package dev.reassembly.evalite.models

import gg.ingot.iron.annotations.Column
import gg.ingot.iron.annotations.Model

@Model
data class UserData(

    val id: String,
    @Column("current_username")
    var currentUsername: String,
    @Column("join_date")
    val joinDate: Long,
    @Column("last_join")
    val lastJoin: Long,
    @Column("active_grant")
    val activeGrant: String?,
    @Column("current_ip")
    var currentIP: String,
    @Column(json = true, name = "past_usernames")
    val pastUsernames: MutableList<String>


)
