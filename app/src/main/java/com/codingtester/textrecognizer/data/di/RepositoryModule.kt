package com.codingtester.textrecognizer.data.di

import com.codingtester.textrecognizer.data.repo.data.DataRepositoryImpl
import com.codingtester.textrecognizer.data.repo.register.RegisterRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // inject instance from firebase auth to inject itself without creating object
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    // inject instance from register repository to inject itself without creating object
    @Provides
    fun provideRegisterRepository(): RegisterRepositoryImpl = RegisterRepositoryImpl(FirebaseAuth.getInstance())

    // inject instance from data repository to inject itself without creating object
    @Provides
    fun provideDataRepository(): DataRepositoryImpl = DataRepositoryImpl()
}