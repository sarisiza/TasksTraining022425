package com.clp.tasks.di

import androidx.credentials.GetCredentialRequest
import com.clp.tasks.OAUTH_ID
import com.clp.tasks.data.login.LoginRepository
import com.clp.tasks.data.login.LoginRepositoryImpl
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesGson(): Gson = Gson()

    @Provides
    @Singleton
    fun providesGoogleId(): GetGoogleIdOption =
        GetGoogleIdOption.Builder()
            .setServerClientId(OAUTH_ID)
            .setFilterByAuthorizedAccounts(true)
            .build()

    @Provides
    @Singleton
    fun providesCredentialRequest(
        googleIdOption: GetGoogleIdOption
    ): GetCredentialRequest = GetCredentialRequest
        .Builder()
        .addCredentialOption(googleIdOption)
        .build()

    @Provides
    @Singleton
    fun providesAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun providesLogin(
        auth: FirebaseAuth
    ) : LoginRepository = LoginRepositoryImpl(auth)

    @Provides
    fun providesCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

}