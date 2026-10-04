package com.ranielschneider.ritimofit.core.design.components.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.components.button.RitimoPrimaryButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoSecondaryButton
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoCircularProgress
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme

@Composable
fun RitimoLoadingState(
    modifier: Modifier = Modifier.fillMaxSize(),
    message: String = "Carregando..."
) {
    Column(
        modifier = modifier.padding(RitimoTheme.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RitimoCircularProgress(size = 48.dp)
        Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))
        Text(
            text = message,
            style = RitimoTheme.typography.bodyMedium,
            color = RitimoTheme.colors.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RitimoEmptyState(
    title: String,
    modifier: Modifier = Modifier.fillMaxSize(),
    description: String? = null,
    icon: ImageVector = RitimoIcons.Search,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.padding(RitimoTheme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = RitimoTheme.colors.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))
        Text(
            text = title,
            style = RitimoTheme.typography.titleLarge,
            color = RitimoTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
        if (description != null) {
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
            Text(
                text = description,
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        if (actionText != null && onAction != null) {
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))
            RitimoSecondaryButton(
                text = actionText,
                onClick = onAction
            )
        }
    }
}

@Composable
fun RitimoErrorState(
    title: String = "Ocorreu um erro",
    message: String = "Não foi possível carregar os dados. Tente novamente.",
    modifier: Modifier = Modifier.fillMaxSize(),
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.padding(RitimoTheme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = RitimoIcons.Warning,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = RitimoTheme.colors.error
        )
        Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))
        Text(
            text = title,
            style = RitimoTheme.typography.titleLarge,
            color = RitimoTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
        Text(
            text = message,
            style = RitimoTheme.typography.bodyMedium,
            color = RitimoTheme.colors.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))
            RitimoPrimaryButton(
                text = "Tentar novamente",
                onClick = onRetry,
                icon = RitimoIcons.Refresh
            )
        }
    }
}

@Composable
fun RitimoSuccessState(
    title: String,
    modifier: Modifier = Modifier.fillMaxSize(),
    message: String? = null,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.padding(RitimoTheme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = RitimoIcons.Check,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = RitimoTheme.colors.success
        )
        Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))
        Text(
            text = title,
            style = RitimoTheme.typography.titleLarge,
            color = RitimoTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
        if (message != null) {
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
            Text(
                text = message,
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        if (actionText != null && onAction != null) {
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))
            RitimoPrimaryButton(
                text = actionText,
                onClick = onAction
            )
        }
    }
}
