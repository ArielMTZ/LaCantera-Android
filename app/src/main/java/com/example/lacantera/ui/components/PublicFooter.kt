package com.example.lacantera.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lacantera.ui.theme.LcBorder
import com.example.lacantera.ui.theme.LcTextMuted
import com.example.lacantera.ui.theme.LcTextPrimary

@Composable
fun PublicFooter(
    onPrivacyClick: () -> Unit = {},
    onTermsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 26.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(color = LcBorder)

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "© 2026 La Cantera | Centro Deportivo",
            color = LcTextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Privacidad",
                color = LcTextMuted,
                fontSize = 11.sp,
                modifier = Modifier.clickable(onClick = onPrivacyClick)
            )

            Text(
                text = "Términos",
                color = LcTextMuted,
                fontSize = 11.sp,
                modifier = Modifier.clickable(onClick = onTermsClick)
            )

            Text(
                text = "v1.0.0",
                color = LcTextMuted.copy(alpha = 0.55f),
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "●    ◉    ♪",
            color = LcTextMuted,
            fontSize = 16.sp
        )
    }
}