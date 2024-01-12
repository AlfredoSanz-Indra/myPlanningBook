package com.alfred.myplanningbook.core.di

import com.alfred.myplanningbook.data.repository.PlanningBookRepositoryImpl
import com.alfred.myplanningbook.data.repository.UsersRepositoryImpl
import com.alfred.myplanningbook.domain.repositoryapi.PlanningBookRepository
import com.alfred.myplanningbook.domain.repositoryapi.UsersRepository
import com.alfred.myplanningbook.domain.usecase.PlanningBookServiceImpl
import com.alfred.myplanningbook.domain.usecase.UsersServiceImpl
import com.alfred.myplanningbook.domain.usecaseapi.PlanningBookService
import com.alfred.myplanningbook.domain.usecaseapi.UsersService
import com.alfred.myplanningbook.ui.loggedview.viewmodel.BookMenuViewModel
import com.alfred.myplanningbook.ui.loggedview.viewmodel.PlanningBookManagerViewModel
import com.alfred.myplanningbook.ui.view.viewmodel.LoginViewModel
import com.alfred.myplanningbook.ui.view.viewmodel.MainViewModel
import com.alfred.myplanningbook.ui.view.viewmodel.RegisterViewModel
import com.alfred.myplanningbook.ui.view.viewmodel.ResetPwdViewModel
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

    single<PlanningBookRepository> {
        PlanningBookRepositoryImpl(get(named("IODispatcher")))
    }

    //factoryOf(::UsersServiceImpl) { bind<UsersService>() }
    factory<UsersService> {
        UsersServiceImpl(get())
    }

    factory<PlanningBookService> {
        PlanningBookServiceImpl(get())
    }

    viewModelOf(::MainViewModel)
    viewModelOf(::ResetPwdViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::BookMenuViewModel)
    viewModelOf(::PlanningBookManagerViewModel)
}