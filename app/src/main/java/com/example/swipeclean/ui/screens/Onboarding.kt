package com.example.swipeclean.ui.screens



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swipeclean.R
import com.example.swipeclean.domain.model.OnBoardModel
import com.example.swipeclean.ui.viewmodel.SplashViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel



@Composable
fun OnboardingScreenUI(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = koinViewModel(),
    onNavigateToHome: () -> Unit,
) {
    val pages = listOf(
        OnBoardModel(
            title = "Welcome to SwipeClean - Your photo " +
                    "\n cleaning companion",
            description = "Keep your gallery organized with a fast, simple, and smart photo-cleaning experience.",
            imageRes = R.drawable.onbim1

        ),
        OnBoardModel(
            title = "Swipe Left to Delete, Right to Keep",
            description = "Review your photos quickly—swipe right to keep, swipe left to delete. Cleaning has never been easier.",
            imageRes = R.drawable.onbim2
        ),
        OnBoardModel(
            title = "Free Up Storage Instantly",
            description = "Remove unwanted photos and reclaim storage space with smart auto-cleanup and trash management.",
            imageRes = R.drawable.onbim3
        )
    )

    val pagerState = rememberPagerState(
        pageCount = { pages.size }
    )
    val coroutineScope = rememberCoroutineScope()
    val color = MaterialTheme.colorScheme
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ){innerpadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.White
                )
                .padding(innerpadding)
                .padding(16.dp),
        ) {

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                OnBoardItem(pages[page])
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)

            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    repeat(pages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .width(if (isSelected) 18.dp else 8.dp)
                                .height(if (isSelected) 8.dp else 8.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) color.primary else color.outline,
//                                    color = if (isSelected) Color(0xff5948ea) else Color.Gray,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .background(
                                    color = if (isSelected) color.primary else color.onPrimary,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
            Button(
                onClick = {
                    if (pagerState.currentPage < 2) {
                            val nextPage = pagerState.currentPage + 1
                            coroutineScope.launch { pagerState.animateScrollToPage(nextPage) }
                        }else{
                            viewModel.completeOnBoarding()
                            onNavigateToHome()
                        }
                },
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .padding(vertical = 16.dp).fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = color.primary,
                    contentColor = color.onPrimary
                )
            ) {
                if (pagerState.currentPage < 2){
                    Text(
                        text = "Next",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = color.onPrimary
                        )
                    )
                }else{
                    Text(
                        text = "Get Started",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = color.onPrimary
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun OnBoardItem(page: OnBoardModel) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = null,
            modifier = Modifier
                .height(350.dp)
                .width(350.dp)
                .padding(bottom = 20.dp)
        )
        Text(
            text = page.title, style = TextStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground,
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.padding(horizontal = 25.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.description,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
                color = colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        )

    }

}

