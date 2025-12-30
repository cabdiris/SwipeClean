package com.example.swipeclean.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.swipeclean.ui.screens.HomeScreens
import com.example.swipeclean.ui.screens.PrivacyPolicyScreen
import com.example.swipeclean.ui.screens.SettingScreen
import com.example.swipeclean.ui.screens.SwipeScreen
import com.example.swipeclean.ui.screens.TermsAndConditionsScreen
import com.example.swipeclean.ui.screens.TrashScreen

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun HomeNavGraph(navController: NavHostController, paddingValues: PaddingValues){
    NavHost(
        navController = navController,
        route = Graph.Home,
        startDestination = BottomBarScreens.Home.route
    ){
        composable(route = BottomBarScreens.Home.route){
            // Camera screen navigates to Crop via route with uri param
            HomeScreens(
                paddingValues = paddingValues,
                onNavigateSwipe = {photos, size->
                    navController.navigate(Swipes.SwipeScreen.passArgs(groupName = photos, size = size))
                }
            )
        }
        composable(route = BottomBarScreens.Trash.route){
            TrashScreen(paddingValues = paddingValues)
        }
        composable(route = BottomBarScreens.Settings.route){
            SettingScreen(
                paddingValues = paddingValues,
                onNavigatePrivacys = { navController.navigate(Privacys.PrivacyPolicys.route) },
                onNavigateTerms = { navController.navigate(Terms.TermsAndConditions.route) },
            )
        }
        // Nested Crop graph is contributed by extension below
        SwipeNavGraph(paddingValues = paddingValues,navController = navController)

        PrivacyPolicyNavGraph(paddingValues = paddingValues,navController = navController)

        TermsNavGraph(paddingValues = paddingValues,navController = navController)
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
fun NavGraphBuilder.SwipeNavGraph(navController: NavController, paddingValues: PaddingValues){
    navigation(
        route = Graph.Swipe,
        startDestination = Swipes.SwipeScreen.route
    ){
        composable(
            route = Swipes.SwipeScreen.route,
            arguments = listOf(
                navArgument("Groupname") { type = NavType.StringType },
                navArgument("Size") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val uriArg = backStackEntry.arguments?.getString("Groupname") ?: ""
            val sizeArg = backStackEntry.arguments?.getInt("Size") ?: 0
            SwipeScreen(
                paddingValues = paddingValues,
                Groupname = uriArg,
                navController = navController,
                size = sizeArg,
                onReviewClick = {navController.navigate(BottomBarScreens.Trash.route){
                    popUpTo(BottomBarScreens.Home.route) { inclusive = true }
                } }
            )
        }
    }
}

fun NavGraphBuilder.PrivacyPolicyNavGraph(paddingValues: PaddingValues,navController: NavHostController){
    navigation(
        route = Graph.Privacy,
        startDestination = Privacys.PrivacyPolicys.route
    ){
        composable(route = Privacys.PrivacyPolicys.route,){ backStackEntry ->
            PrivacyPolicyScreen(
                onBack = { navController.popBackStack() },
                padding = paddingValues
            )
        }
    }
}
fun NavGraphBuilder.TermsNavGraph(paddingValues: PaddingValues,navController: NavHostController){
    navigation(
        route = Graph.Term,
        startDestination = Terms.TermsAndConditions.route
    ){
        composable(route = Terms.TermsAndConditions.route,){ backStackEntry ->
            TermsAndConditionsScreen(
                onBack = { navController.popBackStack() },
                padding = paddingValues
            )
        }
    }
}