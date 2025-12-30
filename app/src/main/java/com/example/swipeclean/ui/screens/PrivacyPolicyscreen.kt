package com.example.swipeclean.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swipeclean.ui.theme.PjsFontFamily

@Preview
@Composable
private fun test() {
    PrivacyPolicyScreen(
        PaddingValues(vertical = 16.dp),
        onBack = { TODO() }
    )
}

@Composable
fun PrivacyPolicyScreen(
    padding: PaddingValues,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth().background(color = Color.White)
            .padding(padding)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
            Text(
                text = "Privacy Policy",
                style = TextStyle(
                    fontFamily = PjsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp,
//                    lineHeight = 24.sp,
//                    letterSpacing = 0.5.sp
                )
            )
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(start = 18.dp, end = 18.dp, bottom = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Last Updated: {LAST_UPDATE_DATE}\n" +
                        "\n" +
                        "This Privacy Policy applies to the mobile application {APP_NAME}, developed by {DEVELOPER_NAME} (“we”, “our”, or “us”).\n" +
                        "\n" +
                        "1. Information We Collect\n" +
                        "\n" +
                        "{APP_NAME} does not collect, store, or share any personal data from users.\n" +
                        "\n" +
                        "The app processes the following data locally on the device:\n" +
                        "\n" +
                        "Photos and media files (only for viewing, cleaning, or deleting)\n" +
                        "\n" +
                        "Storage information for freeing up space\n" +
                        "\n" +
                        "No data is uploaded to any server.\n" +
                        "\n" +
                        "2. Permissions\n" +
                        "\n" +
                        "The app may request:\n" +
                        "\n" +
                        "Photos / Media Permission – required to display and clean photos\n" +
                        "\n" +
                        "Storage Permission – required for deleting files\n" +
                        "\n" +
                        "These permissions are used only for the app's core functionality.\n" +
                        "We never access other files or personal information.\n" +
                        "\n" +
                        "3. Third-Party Services\n" +
                        "\n" +
                        "The app may include:\n" +
                        "\n" +
                        "Google AdMob or other ad networks\n" +
                        "\n" +
                        "These may collect anonymous device identifiers for ad personalization.\n" +
                        "Users may disable personalized ads in system settings.\n" +
                        "\n" +
                        "4. Children’s Privacy\n" +
                        "\n" +
                        "We do not collect personal data from children under 13.\n" +
                        "Since no data is collected, the app is safe for all ages.\n" +
                        "\n" +
                        "5. Data Security\n" +
                        "\n" +
                        "All processing occurs on the user's device.\n" +
                        "We do not store or transmit any user data.\n" +
                        "\n" +
                        "6. Changes to This Policy\n" +
                        "\n" +
                        "We may update this Privacy Policy.\n" +
                        "If updated, we will display an in-app notice or update the version here.\n" +
                        "\n" +
                        "7. Contact Us\n" +
                        "\n" +
                        "For questions or support:\n" +
                        "\uD83D\uDCE9 {SUPPORT_EMAIL}\n" +
                        "\n" +
                        "⚠\uFE0F IMPORTANT FOR BUYER (CodeCanyon):\n" +
                        "\n" +
                        "Before publishing this item, replace all placeholders:\n" +
                        "\n" +
                        "{APP_NAME}\n" +
                        "\n" +
                        "{DEVELOPER_NAME}\n" +
                        "\n" +
                        "{SUPPORT_EMAIL}\n" +
                        "\n" +
                        "{LAST_UPDATE_DATE}\n" +
                        "\n" +
                        "Failure to update may cause rejection from Google Play.",
                style = TextStyle(
                    fontFamily = PjsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 19.sp,
                    letterSpacing = 0.5.sp
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
