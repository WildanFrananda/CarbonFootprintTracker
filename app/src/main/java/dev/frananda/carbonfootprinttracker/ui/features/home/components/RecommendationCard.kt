package dev.frananda.carbonfootprinttracker.ui.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.frananda.carbonfootprinttracker.ui.theme.AppTypography
import dev.frananda.carbonfootprinttracker.ui.theme.Spacing

@Composable
fun RecommendationCard(recommendation: String): Unit {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.md)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(Spacing.md)
                )
                Spacer(modifier = Modifier.width(Spacing.xs))
                Text(
                    text = "AI Eco-Recommendations",
                    style = AppTypography.headlineSmall.copy(fontSize = 20.sp),
                    color = Color(0xFF111827)
                )
            }

            Spacer(modifier = Modifier.height(Spacing.sm))

            Text(
                text = recommendation,
                style = AppTypography.bodyLarge.copy(
                    lineHeight = 24.sp,
                    color = Color(0xFF4B5563)
                )
            )

            Spacer(modifier = Modifier.height(Spacing.md))

            OutlinedButton(
                onClick = { /* Learn More */ },
                shape = RoundedCornerShape(Spacing.md),
                border = BorderStroke(1.dp, Color(0xFFD1D5DB)),
                contentPadding = PaddingValues(horizontal = 28.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Learn More",
                        style = AppTypography.labelLarge.copy(
                            color = Color(0xFF111827),
                            fontSize = 14.sp,
                            letterSpacing = 0.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(Spacing.xs))
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(Spacing.sm),
                        tint = Color(0xFF111827)
                    )
                }
            }
        }
    }
}