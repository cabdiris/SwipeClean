package com.example.swipeclean.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Brightness6
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swipeclean.R
import com.example.swipeclean.ui.navigation.Privacys
import com.example.swipeclean.ui.theme.PjsFontFamily
import com.example.swipeclean.ui.viewmodel.ThemeViewModel
import com.example.swipeclean.utils.ThemeMode
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
private fun SettingScreenPreview() {
//    SettingScreen()
}


@Composable
fun SettingScreen(
    paddingValues: PaddingValues,
    viewModel: ThemeViewModel = koinViewModel(),
    onNavigatePrivacys: () -> Unit,
    onNavigateTerms: () -> Unit
    ) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.background)
            .padding(paddingValues)
            .padding(16.dp)
    ){
        SettingAppBar(colors)
        Spacer(Modifier.size(30.dp))
        PremiumCard(colors)
        PrefrenceCard(viewModel = viewModel,colors)
        SupportMore(colors, onNavigatePrivacys = onNavigatePrivacys,onNavigateTerms = onNavigateTerms)
    }
}

@Composable
fun SettingAppBar( colors: ColorScheme) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = "Settings",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PjsFontFamily,
                color = colors.onBackground
            ),
        )
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share App",
            modifier = Modifier.size(26.dp),
            tint = colors.primary
        )

    }
}

@Composable
fun ItemCard(
    onClick: () -> Unit,
    shapes: RoundedCornerShape,
    trailIcon: @Composable () -> Unit,
    title: String,
    Subtitle: String? = null,
    actionIcon: @Composable () -> Unit,
    colors: ColorScheme
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colors.surfaceContainer, shapes)
            .clip(shapes),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .background(colors.secondaryContainer, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center,
        ){
            trailIcon()
        }
        Row(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = PjsFontFamily,
                        color = colors.onSurface
                    ),
                )
                if (Subtitle != null) {
                    Text(
                        text = Subtitle,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = PjsFontFamily,
                            color = colors.onSurfaceVariant
                        ),
                    )
                }
            }
            actionIcon()
        }
    }
}

@Composable
fun PremiumCard(colors: ColorScheme) {
    ItemCard(
        onClick = { /*TODO*/ },
        shapes = RoundedCornerShape(6),
        trailIcon = { Icon(Icons.Outlined.WorkspacePremium, contentDescription = "delete", tint = colors.primary) },
        title = "Unlock Premium",
        actionIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = "arrow down",tint = colors.onSurface) },
        colors = colors
    )
}

@Composable
fun PrefrenceCard(viewModel: ThemeViewModel, colors: ColorScheme) {
    val current by viewModel.themeMode.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val themeOptions = listOf(
        ThemeMode.LIGHT to "Light",
        ThemeMode.DARK to "Dark",
        ThemeMode.SYSTEM to "System"
    )
    Column(
        modifier = Modifier.padding(top = 20.dp)
    ){
        Text(
            text = "Preferences",
            modifier = Modifier.padding(vertical = 12.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PjsFontFamily
            )
        )
        ItemCard(
            onClick = { expanded = true},
            shapes = RoundedCornerShape(  6.dp),
            trailIcon = { Icon(Icons.Outlined.Brightness6, contentDescription = "delete", tint = colors.primary) },
            title = "Theme",
            Subtitle = themeOptions.first { it.first == current }.second,
            colors = colors,
            actionIcon = {
                Box() {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "arrow down",tint = colors.onSurface
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        themeOptions.forEach { (mode, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    viewModel.setTheme(mode)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        )

    }
}

@Composable
fun Storage(colors: ColorScheme) {
    Column(
        modifier = Modifier.padding(top = 20.dp)
    ){
        Text(
            text = "Storage",
            modifier = Modifier.padding(12.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PjsFontFamily
            )
        )
        ItemCard(
            onClick = { /*TODO*/ },
            shapes = RoundedCornerShape( 6.dp),
            trailIcon = { Icon(Icons.Outlined.Delete, contentDescription = "delete", tint = colors.primary) },
            title = "Manage Trash",
            actionIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = "arrow down",tint = colors.onSurface) },
            colors = colors
        )


    }
}


@Composable
fun SupportMore(colors: ColorScheme, onNavigatePrivacys: () -> Unit,onNavigateTerms: () -> Unit) {
    Column(
        modifier = Modifier.padding(top = 20.dp)
    ){
        Text(
            text = "Support & More",
            modifier = Modifier.padding(12.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PjsFontFamily
            )
        )
        ItemCard(
            onClick = { /*TODO*/ },
            shapes = RoundedCornerShape(topEnd = 6.dp, topStart = 6.dp),
            trailIcon = { Icon(Icons.Sharp.Star, contentDescription = "delete", tint = colors.primary) },
            title = "Rate us",
            actionIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = "arrow down",tint = colors.onSurface) },
            colors = colors
        )
        Spacer(Modifier.size(1.dp))
        ItemCard(
            onClick = onNavigatePrivacys,
            shapes = RoundedCornerShape(bottomEnd = 6.dp, bottomStart = 6.dp),
            trailIcon = { Icon(Icons.Outlined.Info, contentDescription = "delete", tint = colors.primary) },
            title = "About & Privacy Policy",
            actionIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = "arrow down",tint = colors.onSurface) },
            colors = colors
        )
        Spacer(Modifier.size(1.dp))
        ItemCard(
            onClick = onNavigateTerms,
            shapes = RoundedCornerShape(bottomEnd = 6.dp, bottomStart = 6.dp),
            trailIcon = { Icon(Icons.Outlined.Description, contentDescription = "terms", tint = colors.primary) },
            title = "Trems & Conditions",
            actionIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = "arrow down",tint = colors.onSurface) },
            colors = colors
        )



    }
}