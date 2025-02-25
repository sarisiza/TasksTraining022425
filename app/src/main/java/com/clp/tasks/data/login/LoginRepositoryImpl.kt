package com.clp.tasks.data.login

import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.clp.tasks.utils.UiState
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): LoginRepository {

    override suspend fun handleLogin(credential: Credential) : Flow<UiState<FirebaseUser>> = flow {
        emit(UiState.LOADING)
        try {
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                val credentials = GoogleAuthProvider.getCredential(idToken,null)
                val result = auth.signInWithCredential(credentials).await()
                result.user?.let {
                    emit(UiState.SUCCESS(it))
                } ?: throw Exception("User is null")
            } else throw Exception("Credential is not of type Google")
        } catch (e: Exception){
            emit(UiState.ERROR(e))
        }
    }

}