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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swipeclean.ui.theme.PjsFontFamily

@Composable
fun TermsAndConditionsScreen(
    padding: PaddingValues,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(padding)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
            Text(
                text = "Terms and Conditions",
                style = TextStyle(
                    fontFamily = PjsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 19.sp,
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
                        "Welcome to {APP_NAME}, developed by {DEVELOPER_NAME}.\n" +
                        "By using this application, you agree to the following terms:\n" +
                        "\n" +
                        "1. Acceptance of Terms\n" +
                        "\n" +
                        "By downloading or using {APP_NAME}, you agree to comply with these Terms & Conditions.\n" +
                        "\n" +
                        "If you do not agree, please uninstall the app immediately.\n" +
                        "\n" +
                        "2. License\n" +
                        "\n" +
                        "You are granted a non-exclusive, non-transferable license to use the app.\n" +
                        "\n" +
                        "For CodeCanyon buyers:\n" +
                        "\n" +
                        "A Regular License allows use in one project\n" +
                        "\n" +
                        "An Extended License allows use in a paid/end-product app\n" +
                        "\n" +
                        "Reselling the source code is not allowed, except through CodeCanyon\n" +
                        "\n" +
                        "3. Modifications\n" +
                        "\n" +
                        "You may modify the app for personal use or client projects.\n" +
                        "However:\n" +
                        "\n" +
                        "Attribution removal is permitted only after purchase\n" +
                        "\n" +
                        "You must replace all placeholder content before releasing\n" +
                        "\n" +
                        "4. Restrictions\n" +
                        "\n" +
                        "You agree not to:\n" +
                        "\n" +
                        "Reverse engineer the app\n" +
                        "\n" +
                        "Redistribute or sell the source code outside CodeCanyon\n" +
                        "\n" +
                        "Use the app illegally or in ways that violate local laws\n" +
                        "\n" +
                        "5. Disclaimer\n" +
                        "\n" +
                        "{DEVELOPER_NAME} is not responsible for:\n" +
                        "\n" +
                        "Any data loss (the app deletes files at user request)\n" +
                        "\n" +
                        "Device damage\n" +
                        "\n" +
                        "Misuse of the app\n" +
                        "\n" +
                        "Use the app at your own discretion.\n" +
                        "\n" +
                        "6. Ads & Monetization\n" +
                        "\n" +
                        "The app may include ads such as AdMob.\n" +
                        "By using the app, you agree to any terms required by the ad provider.\n" +
                        "\n" +
                        "7. Updates & Support (CodeCanyon Buyers)\n" +
                        "\n" +
                        "Free updates are provided according to the CodeCanyon policy\n" +
                        "\n" +
                        "Support includes bug fixes and basic guidance\n" +
                        "\n" +
                        "Custom modifications are not included in support\n" +
                        "\n" +
                        "8. Termination\n" +
                        "\n" +
                        "We may suspend or terminate access if the app is misused or abused.\n" +
                        "\n" +
                        "9. Contact\n" +
                        "\n" +
                        "For support inquiries:\n" +
                        "\uD83D\uDCE9 {SUPPORT_EMAIL}\n" +
                        "\n" +
                        "⚠\uFE0F IMPORTANT FOR BUYER (CodeCanyon):\n" +
                        "\n" +
                        "Replace all placeholders before publishing:\n" +
                        "\n" +
                        "{APP_NAME}\n" +
                        "\n" +
                        "{DEVELOPER_NAME}\n" +
                        "\n" +
                        "{SUPPORT_EMAIL}\n" +
                        "\n" +
                        "{LAST_UPDATE_DATE}\n" +
                        "\n" +
                        "Failure to update may cause Google Play rejection.",
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
