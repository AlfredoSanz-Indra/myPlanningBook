package com.alfred.myplanningbook

import androidx.navigation.NavHostController
import com.alfred.myplanningbook.AppScreens.BOOKMENU_SCREEN
import com.alfred.myplanningbook.AppScreens.LOGIN_SCREEN
import com.alfred.myplanningbook.AppScreens.MAIN_SCREEN
import com.alfred.myplanningbook.AppScreens.REGISTER_SCREEN
import com.alfred.myplanningbook.AppScreens.RESETPWD_SCREEN
import com.alfred.myplanningbook.AppScreens.PLANNINGBOOK_SCREEN
import com.alfred.myplanningbook.AppScreens.TASKS_SCREEN
import com.alfred.myplanningbook.AppScreens.ACTIVITIES_SCREEN


/**
 * @author Alfredo Sanz
 * @time 2023
 */

private object AppScreens {
    const val MAIN_SCREEN = "main"
    const val LOGIN_SCREEN = "login"
    const val RESETPWD_SCREEN = "resetPwd"
    const val REGISTER_SCREEN = "register"
    const val BOOKMENU_SCREEN = "booklist"
    const val PLANNINGBOOK_SCREEN = "planningbook"
    const val TASKS_SCREEN = "tasks"
    const val ACTIVITIES_SCREEN = "activities"
}

object AppArgs {
    const val LOGIN_ARG = "loginArg"
    const val SIGN_ARG = "signArg"
}

object AppRoutes {
    const val MAIN_ROUTE = "$MAIN_SCREEN"
    const val LOGIN_ROUTE = "$LOGIN_SCREEN"
    const val RESETPWD_ROUTE = "$RESETPWD_SCREEN"
    const val REGISTER_ROUTE = "$REGISTER_SCREEN"
    const val BOOKMENU_ROUTE = "$BOOKMENU_SCREEN"
    const val PLANNINGBOOK_ROUTE = "$PLANNINGBOOK_SCREEN"
    const val TASKS_ROUTE = "$TASKS_SCREEN"
    const val ACTIVITIES_ROUTE = "$ACTIVITIES_SCREEN"
}

class NavigationActions(private val navController: NavHostController) {

    fun navigateToMain() {
        navController.navigate("$MAIN_SCREEN")
    }
    fun navigateToLogin() {
        navController.navigate("$LOGIN_SCREEN")
    }

    fun navigateToResetPwd() {
        navController.navigate("$RESETPWD_SCREEN")
    }

    fun navigateToRegister() {
        navController.navigate("$REGISTER_SCREEN")
    }

    fun navigateToBookMenu() {
        navController.navigate("$BOOKMENU_SCREEN")
    }

    fun navigateToPlanningBook() {
        navController.navigate("$PLANNINGBOOK_SCREEN")
    }

    fun navigateToTasks() {
        navController.navigate("$TASKS_SCREEN")
    }

    fun navigateToActivities() {
        navController.navigate("$ACTIVITIES_SCREEN")
    }
}