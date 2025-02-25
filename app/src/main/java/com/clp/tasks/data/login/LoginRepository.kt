package com.clp.tasks.data.login

import androidx.credentials.Credential
import com.clp.tasks.utils.UiState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun handleLogin(credential: Credential) : Flow<UiState<FirebaseUser>>

}