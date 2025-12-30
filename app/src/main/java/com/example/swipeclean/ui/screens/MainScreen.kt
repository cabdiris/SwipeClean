package com.example.swipeclean.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.swipeclean.ui.navigation.BottomBarScreens
import com.example.swipeclean.ui.navigation.HomeNavGraph
import com.example.swipeclean.ui.viewmodel.GroupViewModel
import org.koin.androidx.compose.koinViewModel

import kotlin.collections.any
import kotlin.collections.forEach
import kotlin.sequences.any

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                BottomBar(navController)
                BannerAd()
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ){paddingValues ->
        HomeNavGraph(navController,paddingValues)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {

    val screens = listOf(
        BottomBarScreens.Home,
        BottomBarScreens.Trash,
        BottomBarScreens.Settings
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }




    if (bottomBarDestination){
        NavigationBar(
//            containerColor = backgroundColor,
        ){
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreens,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val isSelected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true

    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = if (isSelected) screen.selectedicon else screen.unselectedicon,
                contentDescription = screen.title
            )
        },
        selected = isSelected,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}

@Composable
fun BannerAd(viewModel: GroupViewModel = koinViewModel()) {
    AndroidView(
        factory = { viewModel.bannerAd() }
    )
}