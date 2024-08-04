package dev.reassembly.evalite.models

import gg.ingot.iron.annotations.Column
import gg.ingot.iron.annotations.Model

@Model
data class RankData(
    val name: String,
    val color: String,
    val prefix: String,
    val suffix: String,
    val priority: Int,
    @Column(json = true)
    val permissions: MutableList<String>,

    @Column(json = true)
    val inheritsFrom: MutableList<String>
)
