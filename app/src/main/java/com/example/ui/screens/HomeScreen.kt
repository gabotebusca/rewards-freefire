package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RecentPayout
import com.example.data.model.UserData
import com.example.ui.components.GamingCard
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberCyanBorder
import com.example.ui.theme.CyberCyanDark
import com.example.ui.theme.CyberCyanLight
import com.example.ui.theme.DiamondBlue
import com.example.ui.theme.EmberGold
import com.example.ui.theme.EmberOrange
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
fun HomeScreen(
    user: UserData,
    dailyAdsCount: Int,
    recentPayouts: List<RecentPayout>,
    onWatchAd: () -> Unit,
    onDailyCheckin: () -> Unit,
    onNavigateTab: (Int) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val displayName = user.nickname.ifBlank { user.displayName.ifBlank { "Nuevo Jugador" } }
    val initialLetter = displayName.firstOrNull { it.isLetterOrDigit() }?.uppercase() ?: "U"
    val progressFraction = (user.puntos / 10000f).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(durationMillis = 800),
        label = "walletProgress"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingDarkBg)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // High Density Header Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Round initial avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(CyberCyan)
                        .border(2.dp, CyberCyanLight.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initialLetter,
                        color = TextDark,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                // Nickname & Official label
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (user.nickname.isNotBlank()) "NICKNAME OFICIAL" else "USUARIO ACTIVO",
                        color = CyberCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp,
                        lineHeight = 12.sp
                    )
                    Text(
                        text = displayName,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // UID & Verified badge
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(GamingSurfaceVariant.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                            .border(1.dp, GamingBorder, RoundedCornerShape(8.dp))
                            .clickable {
                                val textToCopy = user.freeFireId.ifBlank { user.uid.take(10) }
                                clipboardManager.setText(AnnotatedString(textToCopy))
                                Toast.makeText(context, "ID Copiado", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (user.freeFireId.isNotBlank()) "UID: ${user.freeFireId}" else "UID: No vinculado",
                                color = TextGray,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(if (user.freeFireId.isNotBlank()) SuccessGreen else EmberGold, CircleShape)
                        )
                        Text(
                            text = if (user.freeFireId.isNotBlank()) "VERIFICADO" else "CONECTADO",
                            color = if (user.freeFireId.isNotBlank()) SuccessGreen else EmberGold,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        // High Density Hero "Tu Billetera" Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                GamingSurfaceVariant,
                                Color(0xFF030712)
                            )
                        )
                    )
                    .border(1.dp, GamingBorder.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                Column {
                    // Top Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TU BILLETERA",
                            color = TextGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .background(CyberCyan.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                                .border(1.dp, CyberCyan.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "VIP NIVEL 1",
                                color = CyberCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Large Points Row
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "%,d".format(user.puntos),
                            color = TextWhite,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-1).sp,
                            lineHeight = 44.sp
                        )
                        Text(
                            text = "PUNTOS",
                            color = CyberCyan,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Progress to 100 Diamonds meta
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "META: 100 DIAMANTES",
                                color = TextWhite,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "%,d / 10,000".format(user.puntos),
                                color = TextGray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }

                        // Glowing Progress Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, GamingBorder, RoundedCornerShape(6.dp))
                                .padding(1.5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(animatedProgress)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(CyberCyanDark, CyberCyanLight)
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }

        // Action 1: Watch Rewarded Video Button with 3D tactile bottom border
        item {
            val canWatch = dailyAdsCount < 20
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .then(
                        if (canWatch) {
                            Modifier
                                .background(CyberCyanDark)
                                .border(1.dp, CyberCyanLight.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                                .clickable { onWatchAd() }
                        } else {
                            Modifier
                                .background(GamingSurfaceVariant)
                                .border(1.dp, GamingBorder, RoundedCornerShape(18.dp))
                        }
                    )
                    .padding(14.dp)
                    .testTag("watch_ad_button")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (canWatch) Color.White.copy(alpha = 0.15f) else GamingSurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = if (canWatch) TextWhite else TextMuted,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Ver Video Recompensa",
                                color = if (canWatch) TextWhite else TextMuted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "GANA +10 PUNTOS POR CLIP",
                                color = if (canWatch) CyberCyanLight else TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                            .border(1.dp, if (canWatch) CyberCyanLight.copy(alpha = 0.3f) else GamingBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$dailyAdsCount/20",
                            color = if (canWatch) TextWhite else TextMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Action 2: Daily Check-in Slate Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(GamingSurfaceVariant.copy(alpha = 0.8f))
                    .border(1.dp, GamingBorder, RoundedCornerShape(18.dp))
                    .clickable { onDailyCheckin() }
                    .padding(14.dp)
                    .testTag("claim_daily_checkin")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(GamingSurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = null,
                                tint = EmberGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Check-in Diario",
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "+50 PUNTOS DISPONIBLES",
                                color = TextGray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                if (user.streakDays > 0) SuccessGreen.copy(alpha = 0.12f) else EmberGold.copy(alpha = 0.12f),
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                1.dp,
                                if (user.streakDays > 0) SuccessGreen.copy(alpha = 0.4f) else EmberGold.copy(alpha = 0.4f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (user.streakDays > 0) "Racha ${user.streakDays}d" else "RECLAMAR",
                            color = if (user.streakDays > 0) SuccessGreen else EmberGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        // 2-Column Compact High Density Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Referidos
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(GamingSurfaceVariant.copy(alpha = 0.4f))
                        .border(1.dp, GamingBorder.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .clickable { onNavigateTab(3) }
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "REFERIDOS",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "+150/Amigo",
                            color = TextWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Minijuegos / Ruleta
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(GamingSurfaceVariant.copy(alpha = 0.4f))
                        .border(1.dp, GamingBorder.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .clickable { onNavigateTab(2) }
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = null,
                            tint = EmberGold,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "MINIJUEGOS",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Ruleta & Trivia",
                            color = TextWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Secondary 2-Column Compact Grid (Tienda & Código Flutter)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tienda de Diamantes
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(GamingSurfaceVariant.copy(alpha = 0.4f))
                        .border(1.dp, GamingBorder.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .clickable { onNavigateTab(1) }
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Diamond,
                            contentDescription = null,
                            tint = DiamondBlue,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "TIENDA CANJE",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Desde 100 💎",
                            color = TextWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Código Flutter / CI/CD
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(GamingSurfaceVariant.copy(alpha = 0.4f))
                        .border(1.dp, GamingBorder.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .clickable { onNavigateTab(4) }
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "CÓDIGO FLUTTER",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "6 Entregables",
                            color = TextWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Live Community Payouts Ticker in High Density
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(GamingSurface)
                    .border(1.dp, GamingBorder.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(SuccessGreen, CircleShape)
                            )
                            Text(
                                text = "CANJES EN VIVO",
                                color = TextWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = "AUTOMÁTICO",
                            color = CyberCyan,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (recentPayouts.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Historial sincronizado con Firestore. Los canjes procesados aparecerán aquí en tiempo real.",
                                color = TextMuted,
                                fontSize = 11.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else {
                        recentPayouts.take(3).forEach { payout ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = payout.nickname,
                                        color = TextWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "UID: ${payout.freeFireId} • ${payout.timeAgo}",
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Diamond,
                                        contentDescription = null,
                                        tint = DiamondBlue,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "+${payout.diamondAmount} 💎",
                                        color = DiamondBlue,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
