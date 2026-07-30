package com.example.lacantera.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lacantera.ui.theme.LcNavyDark
import com.example.lacantera.ui.theme.LcRed
import com.example.lacantera.ui.theme.LcWhite

@Composable
fun PublicHero(
    eyebrow: String,
    titleStart: String,
    titleHighlight: String,
    subtitle: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawRect(color = LcNavyDark)

            val spacing = 26

            for (x in 16..size.width.toInt() step spacing) {
                for (y in 18..size.height.toInt() step spacing) {
                    drawCircle(
                        color = LcWhite.copy(alpha = 0.035f),
                        radius = 1.25f,
                        center = Offset(
                            x = x.toFloat(),
                            y = y.toFloat()
                        )
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    top = 20.dp,
                    end = 20.dp,
                    bottom = 58.dp
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(color = LcRed)
                }
            }

            Spacer(modifier = Modifier.height(34.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Canvas(
                    modifier = Modifier.size(7.dp)
                ) {
                    drawCircle(color = LcRed)
                }

                Spacer(modifier = Modifier.width(9.dp))

                Text(
                    text = eyebrow,
                    color = LcWhite.copy(alpha = 0.52f),
                    fontSize = 9.sp,
                    letterSpacing = 0.8.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(color = LcWhite)
                    ) {
                        append(titleStart)
                    }

                    withStyle(
                        SpanStyle(color = LcRed)
                    ) {
                        append(titleHighlight)
                    }
                },
                fontFamily = FontFamily.SansSerif,
                fontSize = 29.sp,
                lineHeight = 33.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = subtitle,
                color = LcWhite.copy(alpha = 0.50f),
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }

        Canvas(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(54.dp)
        ) {
            val path = Path().apply {
                moveTo(0f, size.height * 0.64f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            drawPath(
                path = path,
                color = LcWhite
            )
        }
    }
}