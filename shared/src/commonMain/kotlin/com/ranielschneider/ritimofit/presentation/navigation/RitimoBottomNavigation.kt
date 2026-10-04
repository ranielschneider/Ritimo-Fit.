package com.ranielschneider.ritimofit.presentation.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ranielschneider.ritimofit.core.design.glass.GlassSurface
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme

data class NavigationTabItem(
    val label: String,
    val icon: ImageVector,
    val isSelected: Boolean,
    val onClick: () -> Unit
)

@Composable
fun RitimoBottomNavigation(
    items: List<NavigationTabItem>,
    modifier: Modifier = Modifier
) {
    GlassSurface(
        modifier = modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .padding(horizontal = RitimoTheme.spacing.md, vertical = RitimoTheme.spacing.xxs),
        shape = RitimoTheme.shapes.extraLarge,
        backgroundColor = RitimoTheme.colors.surfaceElevated.copy(alpha = 0.92f),
        borderColor = RitimoTheme.colors.outlineVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = RitimoTheme.spacing.xs),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val contentColor = if (item.isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.onSurfaceVariant

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RitimoTheme.shapes.medium)
                        .clickable(onClick = item.onClick)
                        .padding(vertical = RitimoTheme.spacing.xxs),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(20.dp),
                        tint = contentColor
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        style = RitimoTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = if (item.isSelected) FontWeight.Bold else FontWeight.Medium
                        ),
                        color = contentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
