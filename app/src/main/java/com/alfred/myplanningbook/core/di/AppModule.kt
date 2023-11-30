package com.alfred.myplanningbook.core.di

import com.alfred.myplanningbook.data.repository.UsersRepositoryImpl
import com.alfred.myplanningbook.domain.repositoryapi.UsersRepository
import com.alfred.myplanningbook.domain.usecase.UsersServiceImpl
import com.alfred.myplanningbook.domain.usecaseapi.UsersService
import com.alfred.myplanningbook.ui.view.viewmodel.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single(named("IODispatcher")) {
        Dispatchers.IO
    }

    single<UsersRepository> {
        UsersRepositoryImpl(get(named("IODispatcher")))
    }

    //factoryOf(::UsersServiceImpl) { bind<UsersService>() }
    factory<UsersService> {
        UsersServiceImpl(get())
    }

    viewModelOf(::RegisterViewModel)
}