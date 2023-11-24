package com.alfred.myplanningbook

import androidx.navigation.NavHostController
import com.alfred.myplanningbook.AppScreens.BOOKLIST_SCREEN
import com.alfred.myplanningbook.AppScreens.LOGIN_SCREEN
import com.alfred.myplanningbook.AppScreens.MAIN_SCREEN
import com.alfred.myplanningbook.AppScreens.REGISTER_SCREEN

/**
 * @author Alfredo Sanz
 * @time 2023
 */

private object AppScreens {
    const val MAIN_SCREEN = "main"
    const val LOGIN_SCREEN = "login"
    const val REGISTER_SCREEN = "register"
    const val BOOKLIST_SCREEN = "booklist"
}

object AppArgs {
    const val LOGIN_ARG = "loginArg"
    const val SIGN_ARG = "signArg"
}

object AppRoutes {
    const val MAIN_ROUTE = "$MAIN_SCREEN"
    const val LOGIN_ROUTE = "$LOGIN_SCREEN"
    const val REGISTER_ROUTE = "$REGISTER_SCREEN"
    const val BOOKLIST_ROUTE = "$BOOKLIST_SCREEN"
}

class NavigationActions(private val navController: NavHostController) {

    fun navigateToMain() {
        navController.navigate("$MAIN_SCREEN")
    }
    fun navigateToLogin() {
        navController.navigate("$LOGIN_SCREEN")
    }

    fun navigateToRegister() {
        navController.navigate("$REGISTER_SCREEN")
    }

    fun navigateToBookList() {
        navController.navigate("$BOOKLIST_SCREEN")
    }
}