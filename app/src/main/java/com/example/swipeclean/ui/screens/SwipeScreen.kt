package com.example.swipeclean.ui.screens

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.swipeclean.domain.model.Photo
import com.example.swipeclean.ui.theme.PjsFontFamily
import com.example.swipeclean.ui.viewmodel.PhotoViewModel
import com.spartapps.swipeablecards.state.SwipeableCardsState
import com.spartapps.swipeablecards.state.rememberSwipeableCardsState
import com.spartapps.swipeablecards.ui.SwipeableCardDirection
import com.spartapps.swipeablecards.ui.SwipeableCardsFactors
import com.spartapps.swipeablecards.ui.SwipeableCardsProperties
import com.spartapps.swipeablecards.ui.lazy.LazySwipeableCards
import com.spartapps.swipeablecards.ui.lazy.items
import org.koin.androidx.compose.koinViewModel


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun SwipeScreen(
    paddingValues: PaddingValues,
    Groupname: String,
    navController: NavController,
    onReviewClick: () -> Unit,
    viewModel: PhotoViewModel = koinViewModel<PhotoViewModel>(),
    size: Int
) {
    val photos = viewModel.photos.collectAsState().value.size
    val activity = requireNotNull(LocalActivity.current)

    LaunchedEffect(Unit) {
        viewModel.loadPhotos(activity,Groupname,)
    }
    val showSummary = remember { mutableStateOf(false) }

    BackHandler {
        if (viewModel.hasDeletePhoto()) {
            showSummary.value = true
        } else {
            navController.popBackStack()
        }
    }
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(paddingValues)
            .padding(16.dp)
    ){
        val swipephoto by viewModel.swipephotos.collectAsState()
        SwipeAppBar(navController,viewModel,showSummary,size, )
        Spacer(Modifier.size(40.dp))
        SwipeableCards(viewModel,showSummary,size)
        if (showSummary.value){
            SwipeSummaryDialog(
                keptCount = swipephoto.count { it.second == PhotoViewModel.SwipeAction.KEEP},
                deletedCount = swipephoto.count { it.second == PhotoViewModel.SwipeAction.DELETE},
                onReviewClick = onReviewClick,
                onLaterClick = { navController.popBackStack() },
                onDismiss = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun SwipeAppBar(
    navController: NavController,
    viewModel: PhotoViewModel,
    showSummary: MutableState<Boolean>,
    size: Int,
) {
    val colors = MaterialTheme.colorScheme
    val number = viewModel.swipephotos.collectAsState().value.size

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "back",
            tint = colors.onBackground,
            modifier = Modifier
                .size(26.dp)
                .clickable(onClick = {
                    if (viewModel.hasDeletePhoto()) {
                        showSummary.value = true
                    } else {
                        navController.popBackStack()
                    }
                })
        )
        Text(
            text = "${number}/$size",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PjsFontFamily,
                color = colors.onBackground
                ),
        )
        BadgedBox(
            badge = {Badge {  }}
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Search",
                modifier = Modifier.size(28.dp),
                tint = colors.onBackground
            )
        }

    }
}


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun SwipeableCards(viewModel: PhotoViewModel, showSummary: MutableState<Boolean>, size: Int,) {
    val photos by viewModel.photos.collectAsState()
    val state = rememberSwipeableCardsState(
            initialCardIndex = 0,
            itemCount = { size }
        )



    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        LazySwipeableCards<Photo>(
            modifier = Modifier.padding(10.dp),
            state = state,
            properties = SwipeableCardsProperties(
                padding = 20.dp,
                stackedCardsOffset = 0.dp,
            ),
            factors = SwipeableCardsFactors(),

            onSwipe = { photo, direction ->
                val currentIndex = state.currentCardIndex
                when (direction) {
                    SwipeableCardDirection.Right -> { /* Handle right swipe */

                        viewModel.swipeRightKeep(photo,currentIndex)
                        if (viewModel.swipephotos.value.size == size) {
                            showSummary.value = true
                        }
                    }

                    SwipeableCardDirection.Left -> { /* Handle left swipe */
                        viewModel.swipeLeftDelete(photo,currentIndex)
                        if (viewModel.swipephotos.value.size == size) {
                            showSummary.value = true
                        }
                    }
                }

            }
        ) {
            items(items = photos) { photo, index, offset ->
                ImageCard(photos,index)
            }

        }
        ActionRow(state,viewModel)
    }
}

@Composable
fun ImageCard(photo: List<Photo>,Index: Int,) {
    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photo[Index].uri)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun ActionRow(state: SwipeableCardsState, viewModel: PhotoViewModel,) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ){
        ActionButton(
            onClick = {state.swipe(SwipeableCardDirection.Left)},
            icon = Icons.Outlined.Delete,
            text = "Delete",
            color = Color(0xFFdd3d4c)
        )
        ActionButton(
            onClick = {
                viewModel.undoLastAction()
                state.goBack()
            },
            icon = Icons.Default.Undo,
            text = "Undo",
            color = Color.Gray,
            enabled = state.canSwipeBack.value
        )
        ActionButton(
            onClick = { /* Handle super like action */ },
            icon = Icons.Default.Share,
            text = "Share",
            color = Color(0xFF484DEA)
        )
        ActionButton(
            onClick = { state.swipe(SwipeableCardDirection.Right)},
            icon = Icons.Default.FavoriteBorder,
            text = "Keep",
            color = Color(0xFF3bae55)
        )
    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    enabled: Boolean = true,
    contentDescription: String? = null,
    color: Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FloatingActionButton(
            onClick = onClick,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(1.dp),
            containerColor = Color.White,
//            contentColor = if (enabled) {
//                contentColorFor(MaterialTheme.colorScheme.surface)
//            } else {
//                MaterialTheme.colorScheme.onError.copy(alpha = 0.3f)
//            },
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = color,
                modifier = Modifier.padding(7.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            textAlign = TextAlign.Center,
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                fontFamily = PjsFontFamily
            ),
            color = color
        )
    }
}