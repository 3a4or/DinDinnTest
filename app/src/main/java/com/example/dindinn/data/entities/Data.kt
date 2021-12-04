package com.example.dindinn.data.entities

import java.io.Serializable

data class Data(
    val addon: List<Addon>,
    val alerted_at: String,
    val created_at: String,
    val expired_at: String,
    val id: Int,
    val quantity: Int,
    val title: String,
    var lastTime: Long?,
    var expired: Boolean = false,
    var hasCounter: Boolean = false
): Serializable