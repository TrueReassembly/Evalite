package dev.reassembly.evalite.models

import gg.ingot.iron.annotations.Model

@Model
data class IPData(
    val ip: String,
    val players: MutableList<String>
)
