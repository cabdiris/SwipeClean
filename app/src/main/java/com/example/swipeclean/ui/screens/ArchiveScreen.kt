package com.example.swipeclean.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.swipeclean.data.local.TrashImage
import com.example.swipeclean.domain.model.trashModel
import com.example.swipeclean.ui.theme.PjsFontFamily
import com.example.swipeclean.ui.viewmodel.ArchiveViewModel
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
private fun TrashScreenPreview() {
//    TrashScreen()
}

@Composable
fun TrashScreen(paddingValues: PaddingValues, archiveViewModel: ArchiveViewModel = koinViewModel<ArchiveViewModel>()) {
    val trash by archiveViewModel.trash.collectAsState()

    LaunchedEffect(Unit) {
        archiveViewModel.loadTrash()
    }
    val selectedItems by archiveViewModel.trashSelected.collectAsState()
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(paddingValues)
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
        ){
            TrashAppBar(trash,selectedItems)
            Spacer(Modifier.size(20.dp))
            TrashImages(trash, archiveViewModel,selectedItems)
        }
        if (selectedItems.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(colors.onSecondary)
                    .padding(vertical = 10.dp)
            ){
                RestoreDeleteButtons(
                    archiveViewModel = archiveViewModel,
                    selectedItems = selectedItems
                )
            }
        }
    }
}

@Composable
fun TrashAppBar(trash: List<trashModel>, selectedItems: List<trashModel>) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){

        Text(
            text = "Archive ",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PjsFontFamily,
                color = colors.onBackground
            ),
        )
        if (selectedItems.isNotEmpty()) {
            Text(
                text = "${selectedItems.size}Selected",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PjsFontFamily,
                    color = colors.primary
                ),
            )
        }
    }
}

@Composable
fun TrashImages(
    trashs: List<trashModel>,
    archiveViewModel: ArchiveViewModel,
    selectedItems: List<trashModel>
) {
    val colors = MaterialTheme.colorScheme
    val trash by archiveViewModel.trash.collectAsState()
    val state = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp)
            .padding(bottom = 40.dp),
        state = state,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        items(trash.size){it->
            val item = trash[it]
            val isSelected = selectedItems.contains(item)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .clickable(onClick = { archiveViewModel.toggleSelection(item) })
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(trash[it].originalUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp). align(Alignment.TopEnd),
                ){
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = colors.primary,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.End)
                        )
                    }else{Spacer(modifier = Modifier.size(24.dp))}
                }
            }
        }
    }
}

@Composable
fun RestoreDeleteButtons(
    modifier: Modifier = Modifier,
    archiveViewModel: ArchiveViewModel,
    selectedItems: List<trashModel>,
) {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // user confirmed deletion
            Toast.makeText(context, "Deleted permanently", Toast.LENGTH_SHORT).show()
            archiveViewModel.confirmDeletionForAndroid11Plus(selectedItems)
            archiveViewModel.loadTrash()
            archiveViewModel.clearSelection()
        } else {
            Toast.makeText(context, "Deletion cancelled", Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ){
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Button(
                onClick = {
                    archiveViewModel.restoreItem(
                        selectedItems.map { TrashImage(it.id, it.originalUri, null,it.deletedAt) }
                    )
                } ,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1db954),
                    contentColor = Color.White,
                ),
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(.47f)
            ) {
                Text(
                    text = "Restore(${selectedItems.size})",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PjsFontFamily
                    )
                )
            }
            Button(
                onClick = {
                    val images = selectedItems.map { TrashImage(it.id, it.originalUri, null, it.deletedAt) }
                    archiveViewModel.deleteSelected(
                        onRequiresConfirmation = { req ->
                            launcher.launch(Builder(req.intentSender).build())
                        },
                        onCompleted = {
                            Toast.makeText(context, "Deleted permanently", Toast.LENGTH_SHORT)
                                .show()
                        },
                        onError = { message ->
                            Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                        },
                        selected = selectedItems
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFFdd3d4c),
                ),
                border = BorderStroke(2.dp, Color(0xFFdd3d4c)),
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Delete Permanently",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PjsFontFamily,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

