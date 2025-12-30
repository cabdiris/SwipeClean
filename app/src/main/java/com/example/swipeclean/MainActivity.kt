package com.example.swipeclean

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.swipeclean.ui.navigation.RootNavGraph
import com.example.swipeclean.ui.theme.SwipeCleanTheme
import com.example.swipeclean.ui.viewmodel.ThemeViewModel
import com.example.swipeclean.utils.ThemeMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by themeViewModel.themeMode.collectAsState()

            val isDarkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            SwipeCleanTheme(darkTheme = isDarkTheme, dynamicColor = themeMode == ThemeMode.SYSTEM ){
                val navController = rememberNavController()
                RootNavGraph(navController)
            }
        }
    }
}

