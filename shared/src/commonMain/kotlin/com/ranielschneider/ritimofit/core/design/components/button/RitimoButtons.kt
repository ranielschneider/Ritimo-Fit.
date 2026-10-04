package com.ranielschneider.ritimofit.core.design.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme

@Composable
fun RitimoPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    shape: Shape = RitimoTheme.shapes.pill
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 52.dp),
        enabled = enabled && !isLoading,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = RitimoTheme.colors.primary,
            contentColor = RitimoTheme.colors.onPrimary,
            disabledContainerColor = RitimoTheme.colors.surfaceVariant,
            disabledContentColor = RitimoTheme.colors.onSurfaceVariant.copy(alpha = 0.5f)
        ),
        contentPadding = PaddingValues(horizontal = RitimoTheme.spacing.lg, vertical = RitimoTheme.spacing.sm)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = RitimoTheme.colors.onPrimary,
                strokeWidth = 2.5.dp
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))
                }
                Text(
                    text = text,
                    style = RitimoTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp
                    )
                )
            }
        }
    }
}

@Composable
fun RitimoSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    shape: Shape = RitimoTheme.shapes.pill
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        enabled = enabled && !isLoading,
        shape = shape,
        border = BorderStroke(1.dp, if (enabled) RitimoTheme.colors.outline else RitimoTheme.colors.outlineVariant),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = RitimoTheme.colors.onSurface,
            disabledContentColor = RitimoTheme.colors.onSurfaceVariant
        ),
        contentPadding = PaddingValues(horizontal = RitimoTheme.spacing.lg, vertical = RitimoTheme.spacing.sm)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = RitimoTheme.colors.primary,
                strokeWidth = 2.dp
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))
                }
                Text(
                    text = text,
                    style = RitimoTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun RitimoTertiaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 44.dp),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = RitimoTheme.colors.primary,
            disabledContentColor = RitimoTheme.colors.onSurfaceVariant
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))
            }
            Text(
                text = text,
                style = RitimoTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun RitimoIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = RitimoTheme.colors.onSurface
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minWidth = 44.dp, minHeight = 44.dp),
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = tint
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(22.dp)
        )
    }
}
