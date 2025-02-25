package com.clp.tasks.utils

sealed class UiState<out T> {

    object LOADING: UiState<Nothing>()

    data class SUCCESS<T>(val message: T): UiState<T>()

    data class ERROR(val error: Exception): UiState<Nothing>()

}