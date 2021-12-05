package com.example.dindinn.data.entities.order

import java.io.Serializable

data class Addon(
    val id: Int,
    val quantity: Int,
    val title: String
): Serializable