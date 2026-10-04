package com.ranielschneider.ritimofit.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.background.RitimoAmbientBackground
import com.ranielschneider.ritimofit.core.design.components.button.RitimoSecondaryButton
import com.ranielschneider.ritimofit.core.design.components.card.RitimoGlassCard
import com.ranielschneider.ritimofit.core.design.components.indicator.BadgeType
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoStatusBadge
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme

@Composable
fun RoutePlaceholderScreen(
    badgeText: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    subTabs: List<String> = emptyList(),
    activeTab: String? = null,
    onTabSelected: ((String) -> Unit)? = null,
    onLogout: (() -> Unit)? = null
) {
    val scrollState = rememberScrollState()

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = RitimoTheme.spacing.lg, vertical = RitimoTheme.spacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            RitimoStatusBadge(
                text = badgeText,
                type = BadgeType.PRIMARY
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            Text(
                text = title,
                style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

            Text(
                text = description,
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

            if (subTabs.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = RitimoTheme.spacing.md)
                ) {
                    items(subTabs) { tab ->
                        val isSelected = tab == activeTab
                        val backgroundColor = if (isSelected) RitimoTheme.colors.primaryContainer else RitimoTheme.colors.surface
                        val contentColor = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.onSurfaceVariant
                        val borderColor = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.outlineVariant

                        Box(
                            modifier = Modifier
                                .clip(RitimoTheme.shapes.pill)
                                .background(backgroundColor)
                                .border(1.dp, borderColor, RitimoTheme.shapes.pill)
                                .clickable { onTabSelected?.invoke(tab) }
                                .padding(horizontal = RitimoTheme.spacing.lg, vertical = RitimoTheme.spacing.sm)
                        ) {
                            Text(
                                text = tab,
                                style = RitimoTheme.typography.titleMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = contentColor,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }

            RitimoGlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(RitimoTheme.spacing.md),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Estrutura de Navegação Pronta",
                        style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = RitimoTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                    Text(
                        text = "A rota foi configurada na arquitetura KMP do Ritimo Fit. A interface desta seção será desenvolvida posteriormente.",
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    if (onLogout != null) {
                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))
                        RitimoSecondaryButton(
                            text = "Sair da Conta",
                            onClick = onLogout,
                            icon = RitimoIcons.Logout,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxl))
        }
    }
}
