package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CanjeStatus
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberCyanBorder
import com.example.ui.theme.CyberCyanDark
import com.example.ui.theme.CyberCyanLight
import com.example.ui.theme.DiamondBlue
import com.example.ui.theme.EmberGold
import com.example.ui.theme.GamingBorder
import com.example.ui.theme.GamingDarkBg
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.GamingSurfaceElevated
import com.example.ui.theme.GamingSurfaceVariant
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun DiamondPointsBadge(
    puntos: Int,
    modifier: Modifier = Modifier
) {
    val estimatedDiamonds = puntos / 100

    Row(
        modifier = modifier
            .background(
                color = GamingSurfaceVariant.copy(alpha = 0.8f),
                shape = RoundedCornerShape(12.dp)
            )
            .border(1.dp, GamingBorder.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Points
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Puntos",
                tint = EmberGold,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "%,d".format(puntos),
                color = TextWhite,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp
            )
        }

        Box(
            modifier = Modifier
                .width(1.dp)
                .height(12.dp)
                .background(GamingBorder)
        )

        // Estimated Diamonds
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Diamond,
                contentDescription = "Diamantes Estimados",
                tint = DiamondBlue,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "%,d 💎".format(estimatedDiamonds),
                color = DiamondBlue,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun GamingCard(
    modifier: Modifier = Modifier,
    borderColor: Color = GamingBorder.copy(alpha = 0.5f),
    backgroundColor: Color = GamingSurface,
    shape: RoundedCornerShape = RoundedCornerShape(20.dp),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, shape),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        content()
    }
}

@Composable
fun GamingGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    gradientColors: List<Color> = listOf(CyberCyanDark, CyberCyan),
    textColor: Color = TextDark,
    bottomBorderColor: Color = CyberCyanBorder,
    testTag: String = "gaming_button"
) {
    Box(
        modifier = modifier
            .testTag(testTag)
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (enabled) {
                    Modifier
                        .background(Brush.horizontalGradient(gradientColors))
                        .clickable { onClick() }
                } else {
                    Modifier.background(GamingSurfaceElevated.copy(alpha = 0.6f))
                }
            )
            .border(
                width = 1.dp,
                color = if (enabled) gradientColors.first().copy(alpha = 0.7f) else GamingBorder.copy(alpha = 0.4f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (enabled) textColor else TextMuted,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                color = if (enabled) textColor else TextMuted,
                fontWeight = FontWeight.Black,
                fontSize = 13.sp,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun StatusBadge(status: CanjeStatus) {
    val (bg, border, text) = when (status) {
        CanjeStatus.PENDIENTE -> Triple(Color(0x22F59E0B), EmberGold, "PENDIENTE")
        CanjeStatus.EN_PROCESO -> Triple(Color(0x2238BDF8), DiamondBlue, "EN PROCESO")
        CanjeStatus.COMPLETADO -> Triple(Color(0x224ADE80), SuccessGreen, "ENTREGADO")
        CanjeStatus.RECHAZADO -> Triple(Color(0x22F87171), Color(0xFFF87171), "RECHAZADO")
    }

    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(6.dp))
            .border(1.dp, border.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            color = border,
            fontWeight = FontWeight.Black,
            fontSize = 10.sp,
            letterSpacing = 0.5.sp
        )
    }
}
