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
import com.alfred.myplanningbook.AppRoutes.RESETPWD_ROUTE
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.loggedview.BookListView
import com.alfred.myplanningbook.ui.view.LoginView
import com.alfred.myplanningbook.ui.view.RegisterView
import com.alfred.myplanningbook.ui.view.MainView
import com.alfred.myplanningbook.ui.view.ResetPwdView

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
            val mainView = MainView()
            mainView.createView(
                onLogin = {
                            Klog.line("NavigationController", "NavigationGraph", "navHost Main go to login !")
                            navActions.navigateToLogin()
                          },
                onRegister = {
                            Klog.line("NavigationController", "NavigationGraph", "navHost Main go to register !")
                            navActions.navigateToRegister()
                            },
                onBook = {
                            Klog.line("NavigationController", "NavigationGraph", "navHost Main go to Booklist !")
                            navActions.navigateToBookList()
                        }
            )
        }

        composable(REGISTER_ROUTE,
                   arguments = listOf()
        ) {
            val registerView = RegisterView()
            registerView.createView(
                onBack = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost register go to Back!")
                    navController.popBackStack()
                },
                onRegister = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost register Go to Main!")
                    navActions.navigateToMain()
                })
        }

        composable(LOGIN_ROUTE,
                   arguments = listOf()
        ) {
            val loginView = LoginView()
            loginView.createView(
                onBack = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost login go to Back!")
                    navController.popBackStack()
                },
                onLogin = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost login go to booklist!")
                    navActions.navigateToBookList()
                },
                onReset = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost login go to resetPassword!")
                    navActions.navigateToResetPwd()
                }
            )
        }

        composable(RESETPWD_ROUTE,
                    arguments = listOf()
        ) {
            val resetPwdView = ResetPwdView()
            resetPwdView.createView(
                onBack = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost resetPwd go to Back!")
                    navController.popBackStack()
                },
                onReset = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost resetPwd go to main!")
                    navActions.navigateToMain()
                }
            )
        }

        composable(BOOKLIST_ROUTE,
                    arguments = listOf()
        ) {
            val bookListView = BookListView()
            bookListView.createView(
                onLogout = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost booklist go to logout!")
                    navActions.navigateToMain()
                })
        }
    }
}


