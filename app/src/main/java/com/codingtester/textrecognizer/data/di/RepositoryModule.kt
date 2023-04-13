package com.codingtester.textrecognizer.data.di

import com.codingtester.textrecognizer.data.repo.IMainRepository
import com.codingtester.textrecognizer.data.repo.MainRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideRepository(repoImpl: MainRepositoryImpl): IMainRepository = repoImpl
}