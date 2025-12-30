package com.example.swipeclean.ui.screens

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.swipeclean.R
import com.example.swipeclean.domain.model.Photo
import com.example.swipeclean.domain.model.PhotoGroup
import com.example.swipeclean.ui.theme.PjsFontFamily
import com.example.swipeclean.ui.viewmodel.GroupViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreens(
    paddingValues: PaddingValues,
    onNavigateSwipe: (String, Int) -> Unit,
    viewModel: GroupViewModel = koinViewModel<GroupViewModel>()
    ) {
    val group by viewModel.groups.collectAsState()
    val album by viewModel.albums.collectAsState()

    val permissionsGranted by viewModel.permissionsGranted.collectAsState()
    val context = LocalContext.current

    // Define the permissions we need
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        viewModel.setPermissionsGranted(allGranted)

        if (!allGranted) {
            Toast.makeText(
                context,
                "Storage permissions are required to access your photos",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Check permissions on launch
    LaunchedEffect(Unit) {
        val hasPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) ==
                    android.content.pm.PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    android.content.pm.PackageManager.PERMISSION_GRANTED
        }

        if (hasPermissions) {
            viewModel.setPermissionsGranted(true)
        } else {
            permissionLauncher.launch(permissions)
        }
    }

    val colors = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.background)
            .padding(paddingValues)
            .padding(16.dp)
    ){
        HomeAppBar()
        TabsSection(group,album,onNavigateSwipe)
    }
}

@Composable
fun HomeAppBar(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = "SwipeClean",
            style = TextStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.onBackground
            ),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = colors.onBackground
            )
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Search",
                modifier = Modifier.size(26.dp),
                tint = colors.onBackground
            )
        }
    }
}

@Composable
fun TabsSection(groups: List<PhotoGroup>,album: List<PhotoGroup>, onNavigateSwipe: (String,Int) -> Unit) {
    val colors = MaterialTheme.colorScheme
    var index by remember { mutableStateOf(0) }
    val tabs = listOf("Months", "Albums")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ){
        TabRow(selectedTabIndex = index, containerColor = colors.background){
            tabs.forEachIndexed { tabIndex, title ->
                Tab(
                    selected = index == tabIndex,
                    onClick = { index = tabIndex },
                    selectedContentColor = colors.primary,
                    unselectedContentColor = colors.surfaceVariant,
                    text = {
                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                )
            }
        }
        when(index) {
            0 -> {
                MonthsSection(groups = groups,onNavigateSwipe)
            }
            1 -> {
                AlbumsSection(album = album, onNavigateSwipe)
            }
        }
    }
}

@Composable
fun MonthsSection(groups: List<PhotoGroup>, onNavigateSwipe: (String, Int) -> Unit) {
    val colors = MaterialTheme.colorScheme
    val state = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        state = state,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(groups.size) { it ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.69f, true),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Card(
                    onClick = {
                        onNavigateSwipe(groups[it].title,groups[it].count)
                    },
                    colors = CardDefaults.cardColors(containerColor = colors.onSecondary),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.925f)
                ){
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.925f)){
                        if (groups[it].photos.size > 2) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(groups[it].photos[2].uri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .graphicsLayer {
                                        rotationZ = -11.5f
                                        translationX = -20f
                                        translationY = 10f
                                    }
                                    .height(150.dp)
                                    .width(145.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            )
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(groups[it].photos[1].uri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .graphicsLayer {
                                        rotationZ = 5f
                                        translationX = 10f
                                        translationY = -5f
                                    }
                                    .height(150.dp)
                                    .width(145.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            )
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(groups[it].photos[0].uri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .graphicsLayer {
                                        rotationZ = 0f
                                        translationX = 0f
                                        translationY = 0f
                                    }
                                    .height(160.dp)
                                    .width(145.dp)
                                    .padding(bottom = 10.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            )
                        }else{
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(groups[it].photos[0].uri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .graphicsLayer {
                                        rotationZ = 0f
                                        translationX = 0f
                                        translationY = 0f
                                    }
                                    .height(160.dp)
                                    .width(145.dp)
                                    .padding(bottom = 10.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            )
                        }

                    }
                }
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = "${groups[it].title}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PjsFontFamily,
                        color = colors.onBackground
                    )
                    Text(
                        text = "${groups[it].count} photos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = PjsFontFamily,
                        color = colors.onSurfaceVariant
                    )
                }
            }

        }
    }
}

@Composable
fun AlbumsSection(album: List<PhotoGroup>,onNavigateSwipe: (String, Int) -> Unit) {
    val colors = MaterialTheme.colorScheme
    val state =  rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        state = state,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(album.size) { it ->
            Card(
                onClick = {onNavigateSwipe(album[it].title,album[it].count)},
                colors = CardDefaults.cardColors(containerColor = colors.onSecondary),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(end = 16.dp)
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                                .width(100.dp)
                                .height(100.dp)
                        ) {
                            if (album[it].photos.size > 2) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(album[it].photos[2].uri)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .graphicsLayer {
                                            rotationZ = -11.5f
                                            translationX = -20f
                                            translationY = 10f
                                        }
                                        .height(85.dp)
                                        .width(80.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                )
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(album[it].photos[1].uri)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .graphicsLayer {
                                            rotationZ = 5f
                                            translationX = 10f
                                            translationY = -5f
                                        }
                                        .height(85.dp)
                                        .width(80.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                )
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(album[it].photos[0].uri)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .graphicsLayer {
                                            rotationZ = 0f
                                            translationX = 0f
                                            translationY = 0f
                                        }
                                        .height(85.dp)
                                        .width(80.dp)
                                        .padding(bottom = 10.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                )
                            }else{
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(album[it].photos[0].uri)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .graphicsLayer {
                                            rotationZ = 0f
                                            translationX = 0f
                                            translationY = 0f
                                        }
                                        .height(85.dp)
                                        .width(80.dp)
                                        .padding(bottom = 10.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                )
                            }
                        }
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            Text(
                                text = "${album[it].title}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = PjsFontFamily,
                                color = colors.onBackground
                            )
                            Text(
                                text = "${album[it].count} photos",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = PjsFontFamily,
                                color = colors.onSurfaceVariant
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        tint = colors.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}