package com.example.dindinn.data.entities

import io.reactivex.rxjava3.disposables.Disposable
import java.io.Serializable

data class Data(
    val addon: List<Addon>,
    val alerted_at: String,
    val created_at: String,
    val expired_at: String,
    val id: Int,
    val quantity: Int,
    val title: String,
    var lastTime: Long = 0,
    var expiredTime: Long,
    var expired: Boolean = false,
    var hasCounter: Boolean = false,
    var disposableProcess: Disposable?
): Serializable