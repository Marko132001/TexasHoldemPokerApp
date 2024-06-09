package com.example.pokerapp.model.firebase.module

import com.example.pokerapp.model.firebase.AccountService
import com.example.pokerapp.model.firebase.impl.AccountServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService
}