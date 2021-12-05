package com.example.dindinn.data.entities.order

import java.io.Serializable

data class Status(
    val message: String,
    val statusCode: Int,
    val success: Boolean
): Serializable