package com.codingtester.textrecognizer.data.di

import com.codingtester.textrecognizer.data.repo.data.DataRepositoryImpl
import com.codingtester.textrecognizer.data.repo.data.IDataRepository
import com.codingtester.textrecognizer.data.repo.register.IRegisterRepository
import com.codingtester.textrecognizer.data.repo.register.RegisterRepositoryImpl
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
    fun provideRegisterRepository(repoImpl: RegisterRepositoryImpl): IRegisterRepository = repoImpl

    @Provides
    fun provideDataRepository(repoImpl: DataRepositoryImpl): IDataRepository = repoImpl
}