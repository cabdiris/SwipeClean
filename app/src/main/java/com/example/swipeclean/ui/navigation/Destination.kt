package com.example.swipeclean.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.RestoreFromTrash
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

object Graph{
    const val Root = "root"
    const val Splash = "splash"
    const val Onboarding = "onboarding"
    const val Home = "home"
    const val Swipe = "swipe"
    const val Summary = "summary"
    const val Privacy = "privacy"
    const val Term = "terms"
}
sealed class Splash(val route: String){
    object SplashScreen: Splash("splash")
}

sealed class Onboarding(val route: String){
    object OnboardingScreen: Onboarding("onboarding_screen")
}

sealed class BottomBarScreens(val route: String, val title: String, val selectedicon: ImageVector,val unselectedicon: ImageVector){
    object Home: BottomBarScreens(route = "Home", title = "Home",  selectedicon = Icons.Filled.Home, unselectedicon = Icons.Outlined.Home)
    object Trash: BottomBarScreens(route = "Archive", title = "Archive", selectedicon = Icons.Filled.Archive,unselectedicon = Icons.Outlined.Archive)
    object Settings: BottomBarScreens(route = "Settings", title = "Settings", selectedicon = Icons.Filled.Settings, unselectedicon = Icons.Outlined.Settings,)
}

sealed class Summarys(val route: String){
    object SummaryScreen: Summarys("summary_screen")
}

sealed class Swipes(val route: String) {
    object SwipeScreen : Swipes("swipe_screen/{Groupname}/{Size}") {
        fun passArgs(groupName: String, size: Int): String {
            return "swipe_screen/$groupName/$size"
        }
    }
}
sealed class Privacys(val route: String){
    object PrivacyPolicys: Privacys("privacy_screen")
}
sealed class Terms(val route: String){
    object TermsAndConditions: Terms("terms_screen")
}
