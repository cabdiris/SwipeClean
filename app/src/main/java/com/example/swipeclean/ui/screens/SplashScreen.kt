package com.example.swipeclean.ui.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.swipeclean.R
import com.example.swipeclean.ui.navigation.Graph
import com.example.swipeclean.ui.viewmodel.SplashViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = koinViewModel()
) {
    val onBoardingCompleted by viewModel.onBoardingCompleted.collectAsState()
    val scale = remember { Animatable(0f) }

    LaunchedEffect(true) {
        scale.animateTo(
            targetValue = 0.4f,
            animationSpec = tween(
                durationMillis = 300,
                easing = { OvershootInterpolator(4f).getInterpolation(it) }
            )
        )
        delay(800L)
        // Navigate depending on onboarding state
        if (onBoardingCompleted) {
            navController.navigate(Graph.Home) {
                popUpTo(Graph.Root) { inclusive = true }
            }
        } else {
            navController.navigate(Graph.Onboarding) {
                popUpTo(Graph.Root) { inclusive = true }
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo2),
            contentDescription = null,
            modifier = Modifier.clip(RoundedCornerShape(14.dp))
                .size((200 * scale.value).dp)
        )
    }
}
