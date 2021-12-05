package com.example.dindinn.data.entities.order

import java.io.Serializable

data class OrdersResponse(
    val `data`: List<Data>,
    val status: Status
): Serializable