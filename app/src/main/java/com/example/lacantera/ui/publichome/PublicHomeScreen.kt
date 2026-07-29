package com.example.lacantera.ui.publichome

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PublicHomeScreen(
    onLoginClick: () -> Unit,
    onProgramsClick: () -> Unit,
    onRulesClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onTermsClick: () -> Unit,
    onSupportClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FA)),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {

        item {
            PublicHeader(
                onLoginClick = onLoginClick
            )
        }

        item {
            Column(
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                )
            ) {
                Text(
                    text = "Bienvenido a La Cantera",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF071B4A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Consulta información del centro deportivo, programas, reglamentos y servicios disponibles.",
                    fontSize = 15.sp,
                    color = Color(0xFF5F6778),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Explora La Cantera",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF071B4A)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PublicOptionCard(
                    title = "Programas",
                    description = "Conoce los deportes, actividades y programas disponibles.",
                    onClick = onProgramsClick
                )

                PublicOptionCard(
                    title = "Reglamento",
                    description = "Consulta las normas generales y deportivas de La Cantera.",
                    onClick = onRulesClick
                )

                PublicOptionCard(
                    title = "Privacidad",
                    description = "Consulta cómo se recopilan y protegen tus datos.",
                    onClick = onPrivacyClick
                )

                PublicOptionCard(
                    title = "Términos y condiciones",
                    description = "Revisa las condiciones de uso de la plataforma.",
                    onClick = onTermsClick
                )

                PublicOptionCard(
                    title = "Soporte",
                    description = "Obtén ayuda o información para resolver un problema.",
                    onClick = onSupportClick
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "La Cantera · Centro Deportivo",
                    fontSize = 13.sp,
                    color = Color(0xFF7A8291),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun PublicHeader(
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF071B4A))
            .padding(
                horizontal = 20.dp,
                vertical = 28.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "LA CANTERA",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Centro Deportivo",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Organiza, compite y vive el deporte.",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Iniciar sesión")
        }
    }
}

@Composable
private fun PublicOptionCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF071B4A)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF626B7A),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onClick
            ) {
                Text(text = "Ver información")
            }
        }
    }
}