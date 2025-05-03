package com.location.livetracker.domain.model

sealed class ResponseState<out T> {
    data object Idle : ResponseState<Nothing>()
    data object Loading : ResponseState<Nothing>()
    data class Error(val error: String = "") : ResponseState<Nothing>()
    data class Success<R>(val data: R? = null) : ResponseState<R>()
}
