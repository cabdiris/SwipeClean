package com.example.swipeclean.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@Preview
@Composable
private fun SwipeSummaryDialogPreview() {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        SwipeSummaryDialog(
            keptCount = 10,
            deletedCount = 5,
            onReviewClick = { /*TODO*/ },
            onLaterClick = { /*TODO*/ },
            onDismiss = { /*TODO*/ }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeSummaryDialog(
    keptCount: Int,
    deletedCount: Int,
    onReviewClick: () -> Unit,
    onLaterClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    BasicAlertDialog (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(35.dp))
            .background(colors.background ) ,
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Box(
                        modifier = Modifier
                            .size(70.dp),
//                        .background(Color(0xFFFFF5E5), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("\uD83C\uDF89", fontSize = 33.sp)
                    }

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "Session complete! 💪",
                        style = TextStyle(
                            color = colors.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.height(4.dp))

                // Subtitle
                Text(
                    text = "Not sure yet? Review your swiped photos now or whenever you're ready.",
                    style = MaterialTheme.typography.bodySmall.copy(color = colors.onSurfaceVariant),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                // 🧮 Counts row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(1f)
                ) {
                    SummaryBox(
                        count = keptCount,
                        label = "Kept",
                        icon = Icons.Default.CheckCircle,
                        background = Color(0xFFDFFFE3),
                        iconColor = Color(0xFF34C759),
                        modifier = Modifier.weight(0.48f)
                    )
                    Spacer(Modifier.weight(0.04f))
                    SummaryBox(
                        count = deletedCount,
                        label = "Trashed",
                        icon = Icons.Default.Delete,
                        background = Color(0xFFFFE6E9),
                        iconColor = Color(0xFFFF3B30),
                        modifier = Modifier.weight(0.48f)
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Review button
                Button(
                    onClick = onReviewClick,
                    contentPadding = PaddingValues(14.dp),
                    modifier = Modifier.padding(top = 6.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = colors.onPrimary
                    )
                ) {
                    Text(
                        text = "Review Archive",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.onPrimary
                        )
                    )
                }

                Spacer(Modifier.size(10.dp))

                // Later button
                TextButton(onClick = onLaterClick) {
                    Text("Later", color = colors.onSurfaceVariant)
                }
            }
        }
    )
}

@Composable
fun SummaryBox(
    count: Int,
    label: String,
    icon: ImageVector,
    background: Color,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(background)
            .padding(16.dp)

    ) {
        Text(text = "$count", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = iconColor)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
            Spacer(Modifier.size(5.dp))
            Text(text = label, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}
