package com.alfred.myplanningbook

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alfred.myplanningbook.AppRoutes.BOOKLIST_ROUTE
import com.alfred.myplanningbook.AppRoutes.LOGIN_ROUTE
import com.alfred.myplanningbook.AppRoutes.MAIN_ROUTE
import com.alfred.myplanningbook.AppRoutes.REGISTER_ROUTE
import com.alfred.myplanningbook.ui.loggedview.BookListView
import com.alfred.myplanningbook.ui.view.LoginView
import com.alfred.myplanningbook.ui.view.RegisterView
import com.alfred.myplanningbook.ui.view.MainView

/**
 * @author Alfredo Sanz
 * @time 2023
 */


@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MAIN_ROUTE,
    navActions: NavigationActions = remember(navController) { NavigationActions(navController) }
) {

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(MAIN_ROUTE,
                   arguments = listOf()
        ) {
            //navActions.navigateToMain()

            val mainView = MainView()
            mainView.createView(
                onLogin = {
                            println("*** navHost go to login !")
                            navActions.navigateToLogin()
                          },
                onRegister = {
                                println("*** navHost go to register !")
                                navActions.navigateToRegister()
                            }
            )
        }

        composable(REGISTER_ROUTE,
                   arguments = listOf()
        ) {
            val registerView = RegisterView()
            registerView.createView(
                onBack = {
                    println("*** navHost register Back!")
                    navController.popBackStack()
                },
                onRegister = {
                    println("*** navHost register Go to Main!")
                    navActions.navigateToMain()
                })
        }

        composable(LOGIN_ROUTE,
                   arguments = listOf()
        ) {
            val loginView = LoginView()
            loginView.createView(
                onBack = {
                    println("*** navHost login Back!")
                    navController.popBackStack()
                })
        }

        composable(BOOKLIST_ROUTE,
            arguments = listOf()
        ) {
            val bookListView = BookListView()
            bookListView.createView(
                onLogout = {
                    println("*** navHost logout!")
                    navController.popBackStack(MAIN_ROUTE, false)
                })
        }
    }
}


