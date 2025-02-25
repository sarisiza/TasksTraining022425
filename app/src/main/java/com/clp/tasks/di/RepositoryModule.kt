package com.clp.tasks.di

import com.clp.tasks.data.login.LoginRepository
import com.clp.tasks.data.login.LoginRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
//
//    @Binds
//    abstract fun providesLogin(
//        loginRepositoryImpl: LoginRepositoryImpl
//    ): LoginRepository

}