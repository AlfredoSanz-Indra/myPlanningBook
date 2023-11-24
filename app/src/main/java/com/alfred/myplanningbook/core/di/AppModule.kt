package com.alfred.myplanningbook.core.di

import com.alfred.myplanningbook.data.repository.UsersRepositoryImpl
import com.alfred.myplanningbook.domain.repositoryapi.UsersRepository
import com.alfred.myplanningbook.domain.usecase.UsersServiceImpl
import com.alfred.myplanningbook.domain.usecaseapi.UsersService
import com.alfred.myplanningbook.ui.view.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {

    singleOf(::UsersRepositoryImpl) { bind<UsersRepository>() }
    factoryOf(::UsersServiceImpl) { bind<UsersService>() }
    viewModelOf(::RegisterViewModel)
}