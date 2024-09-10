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
import com.alfred.myplanningbook.AppRoutes.ACTIVITIES_ROUTE
import com.alfred.myplanningbook.AppRoutes.BOOKMENU_ROUTE
import com.alfred.myplanningbook.AppRoutes.LOGIN_ROUTE
import com.alfred.myplanningbook.AppRoutes.MAIN_ROUTE
import com.alfred.myplanningbook.AppRoutes.PLANNINGBOOKMANAGER_ROUTE
import com.alfred.myplanningbook.AppRoutes.REGISTER_ROUTE
import com.alfred.myplanningbook.AppRoutes.RESETPWD_ROUTE
import com.alfred.myplanningbook.AppRoutes.TASKS_ROUTE
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.ViewsStore
import com.alfred.myplanningbook.ui.view.LoginView
import com.alfred.myplanningbook.ui.view.MainView
import com.alfred.myplanningbook.ui.view.RegisterView
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
                            navActions.navigateToBookMenu()
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
                    Klog.line("NavigationController", "NavigationGraph", "navHost login go to bookMenu!")
                    navActions.navigateToBookMenu()
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

        composable(BOOKMENU_ROUTE,
                    arguments = listOf()
        ) {
            val bookMenuView = ViewsStore.getBookMenuView()
            bookMenuView.CreateView(
                onPlanningBooks = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost bookmenu go to PlanningBookManager!")
                    navActions.navigateToPlanningManagerBook()
                },
                onTasks = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost bookmenu go to Tasks!")
                    navActions.navigateToTasks()
                },
                onActivities = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost bookmenu go to Activities!")
                    navActions.navigateToActivities()
                },
                onLogout = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost bookmenu go to logout!")
                    ViewsStore.cleanLoggedViews()
                    navActions.navigateToMain()
                })
        }

        composable(PLANNINGBOOKMANAGER_ROUTE,
            arguments = listOf()
        ) {
            val planningBookManagerView = ViewsStore.getPlanningBookManagerView()
            planningBookManagerView.createView(
                onBack = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost planningBookManager go back!")
                    navController.popBackStack()
                })
        }

        composable(TASKS_ROUTE,
            arguments = listOf()
        ) {
            val tasksManagerViewModel = ViewsStore.getTasksManagerView()
            tasksManagerViewModel.createView(
                onBack = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost tasksManager go back!")
                    navController.popBackStack()
                })
        }

        composable(
            ACTIVITIES_ROUTE,
            arguments = listOf()
        ) {
            val activitiesManagerViewModel = ViewsStore.getActivitiesManagerView()
            activitiesManagerViewModel.createView(
                onBack = {
                    Klog.line("NavigationController", "NavigationGraph", "navHost activitiesManager go back!")
                    navController.popBackStack()
                })
        }
    }
}


