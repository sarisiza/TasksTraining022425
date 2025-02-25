package com.clp.tasks.data.login

import android.content.Context
import androidx.credentials.Credential
import com.clp.tasks.utils.UiState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun handleLogin(context: Context) : Flow<UiState<FirebaseUser>>

    suspend fun handleLogout(): Flow<UiState<Unit>>

}