package com.example.datingapp

import com.example.datingapp.connection.InternetCheckService
import com.example.datingapp.firebase.FirebaseAuthController
import com.example.datingapp.firebase.FirebaseAuthControllerImpl
import com.example.datingapp.firebase.FirebaseDataController
import com.example.datingapp.firebase.FirebaseDataControllerImpl
import com.example.datingapp.user.UserController
import com.example.datingapp.user.UserControllerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideUserController(): UserController = UserControllerImpl(FirebaseDataControllerImpl())

    @Provides
    @Singleton
    fun provideFirebaseDataController(): FirebaseDataController = FirebaseDataControllerImpl()

    @Provides
    @Singleton
    fun provideFirebaseAuthController(): FirebaseAuthController = FirebaseAuthControllerImpl()

    @Provides
    @Singleton
    fun provideInternetCheckService(): InternetCheckService = InternetCheckService()
}