package com.clp.tasks.di

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.GetCredentialRequest
import androidx.room.Room
import com.clp.tasks.OAUTH_ID
import com.clp.tasks.data.login.LoginRepository
import com.clp.tasks.data.login.LoginRepositoryImpl
import com.clp.tasks.data.notifications.MessagingRepository
import com.clp.tasks.data.notifications.MessagingRepositoryImpl
import com.clp.tasks.data.room.TasksDao
import com.clp.tasks.data.room.TasksDatabase
import com.clp.tasks.data.room.TasksRepository
import com.clp.tasks.data.room.TasksRepositoryImpl
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
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
    fun providesClearCredentialsState(): ClearCredentialStateRequest =
        ClearCredentialStateRequest()

    @Provides
    @Singleton
    fun providesAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Provides
    fun providesLogin(
        auth: FirebaseAuth,
        credentialRequest: GetCredentialRequest,
        clearCredentialStateRequest: ClearCredentialStateRequest
    ) : LoginRepository = LoginRepositoryImpl(auth, credentialRequest, clearCredentialStateRequest)

    @Provides
    fun providesCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providesTaskDatabase(
        @ApplicationContext context: Context
    ): TasksDatabase =
        Room.databaseBuilder(
            context,
            TasksDatabase::class.java,
            "tasks-db"
        ).build()

    @Provides
    @Singleton
    fun providesDao(
        tasksDatabase: TasksDatabase
    ): TasksDao = tasksDatabase.getTasksDao()

    @Provides
    @Singleton
    fun providesTasksRepository(
        dao: TasksDao
    ): TasksRepository = TasksRepositoryImpl(dao)

    @Provides
    @Singleton
    fun providesMessagingRepository(
        firebaseMessaging: FirebaseMessaging
    ): MessagingRepository = MessagingRepositoryImpl(firebaseMessaging)

}