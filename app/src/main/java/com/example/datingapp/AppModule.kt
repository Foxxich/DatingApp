package com.example.datingapp

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
    fun provideUserControllerService(): UserController = UserControllerImpl()

}