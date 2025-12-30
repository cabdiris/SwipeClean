package com.example.swipeclean.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.swipeclean.ui.screens.OnboardingScreenUI


fun NavGraphBuilder.OnboardingNavGraph(navController: NavHostController,) {
    navigation(
        route = Graph.Onboarding,
        startDestination = Onboarding.OnboardingScreen.route
    ) {
        composable(route = Onboarding.OnboardingScreen.route) {
            OnboardingScreenUI(
                onNavigateToHome = {
//                    navController.popBackStack()
//                    navController.navigate(Graph.Home)
                    navController.navigate(Graph.Home) {
                        popUpTo(Graph.Onboarding) { inclusive = true }
                    }
                }
            )
        }
    }
}