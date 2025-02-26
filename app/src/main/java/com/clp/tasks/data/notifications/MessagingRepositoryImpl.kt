package com.clp.tasks.data.notifications

import com.clp.tasks.utils.UiState
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MessagingRepositoryImpl @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging
) : MessagingRepository{

    override fun getToken(): Flow<UiState<String>> = flow{
        emit(UiState.LOADING)
        try {
            val token = firebaseMessaging.token.await()
            emit(UiState.SUCCESS(token))
        } catch (e: Exception){
            emit(UiState.ERROR(e))
        }
    }

}