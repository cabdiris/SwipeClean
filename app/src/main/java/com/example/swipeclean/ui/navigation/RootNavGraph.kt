package com.example.swipeclean.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.swipeclean.ui.screens.MainScreen
import com.example.swipeclean.ui.screens.SplashScreen

@Composable
fun RootNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        route = Graph.Root,
//        startDestination = Graph.Onboarding,
        startDestination = Graph.Splash,
    ){
        composable (route = Graph.Splash){
            SplashScreen(navController)
        }
        OnboardingNavGraph(navController)
        composable(route = Graph.Home) {
            MainScreen()
        }
    }
}