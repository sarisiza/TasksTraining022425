package com.clp.tasks.data.notifications

import com.clp.tasks.utils.UiState
import kotlinx.coroutines.flow.Flow

interface MessagingRepository {

    fun getToken(): Flow<UiState<String>>

}