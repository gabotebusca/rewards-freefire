package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.service.AdState
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DiamondBlue
import com.example.ui.theme.EmberGold
import com.example.ui.theme.EmberOrange
import com.example.ui.theme.GamingBorder
import com.example.ui.theme.GamingDarkBg
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.GamingSurfaceElevated
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite

@Composable
fun AdMobVideoOverlay(
    adState: AdState,
    onDismiss: () -> Unit
) {
    if (adState is AdState.Playing || adState is AdState.RewardEarned || adState is AdState.Loading) {
        Dialog(
            onDismissRequest = {
                if (adState is AdState.RewardEarned) onDismiss()
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.95f))
                    .testTag("admob_video_dialog"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top Bar: AdMob badge and countdown
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(Color(0xFF1E293B), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Google AdMob • Rewarded Video",
                                color = TextWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (adState is AdState.Playing) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(Color(0xFF0F172A), CircleShape)
                                    .border(2.dp, CyberCyan, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${adState.secondsRemaining}s",
                                    color = CyberCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        } else if (adState is AdState.RewardEarned) {
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .background(Color(0xFF1E293B), CircleShape)
                                    .size(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cerrar",
                                    tint = TextWhite
                                )
                            }
                        }
                    }

                    // Main Ad Stage Creative
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(GamingSurfaceElevated, GamingSurface)
                                )
                            )
                            .border(1.dp, GamingBorder, RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        when (adState) {
                            is AdState.Loading -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator(color = CyberCyan, modifier = Modifier.size(44.dp))
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Cargando anuncio bonificado...",
                                        color = TextGray,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            is AdState.Playing -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(88.dp)
                                            .background(
                                                Brush.radialGradient(
                                                    listOf(CyberCyan.copy(alpha = 0.3f), Color.Transparent)
                                                ),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Diamond,
                                            contentDescription = null,
                                            tint = DiamondBlue,
                                            modifier = Modifier.size(54.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "¡CONSIGUE PASES ÉLITE & DIAMANTES!",
                                        color = TextWhite,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Visualiza el video completo para desbloquear tu recompensa de +10 puntos.",
                                        color = TextGray,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    LinearProgressIndicator(
                                        progress = { (6f - adState.secondsRemaining) / 5f },
                                        modifier = Modifier
                                            .fillMaxWidth(0.8f)
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = CyberCyan,
                                        trackColor = GamingDarkBg,
                                    )
                                }
                            }
                            is AdState.RewardEarned -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(68.dp)
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text(
                                        text = "¡RECOMPENSA GANADA!",
                                        color = SuccessGreen,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 20.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "+10 Puntos sumados a tu saldo",
                                        color = EmberGold,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                            else -> {}
                        }
                    }

                    // Bottom info: anti-fraud badge & action
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        if (adState is AdState.RewardEarned) {
                            GamingGradientButton(
                                text = "RECLAMAR Y CONTINUAR",
                                onClick = onDismiss,
                                modifier = Modifier.fillMaxWidth(),
                                gradientColors = listOf(SuccessGreen, Color(0xFF00BFA5))
                            )
                        } else {
                            Text(
                                text = "Protegido por AdMob Anti-Ban System • Límite 20 ads/día",
                                color = TextGray.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
