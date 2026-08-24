package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuizQuestion
import com.example.data.model.UserData
import com.example.ui.components.GamingCard
import com.example.ui.components.GamingGradientButton
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DiamondBlue
import com.example.ui.theme.EmberGold
import com.example.ui.theme.EmberOrange
import com.example.ui.theme.GamingBorder
import com.example.ui.theme.GamingDarkBg
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.GamingSurfaceElevated
import com.example.ui.theme.GamingSurfaceVariant
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite

@Composable
fun EarnScreen(
    user: UserData,
    dailyAdsCount: Int,
    quizQuestions: List<QuizQuestion>,
    isSpinning: Boolean,
    spinRewardPoints: Int?,
    onWatchAd: () -> Unit,
    onSpinWheel: () -> Unit,
    onAnswerQuiz: (QuizQuestion, Int) -> Unit
) {
    var answeredQuestions by remember { mutableStateOf(setOf<Int>()) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isSpinning) 1800f else 0f,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        label = "wheelRotation"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingDarkBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section 1: AdMob Station
        item {
            GamingCard(
                borderColor = CyberCyan.copy(alpha = 0.5f),
                backgroundColor = GamingSurface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "Estación de Anuncios AdMob",
                                color = TextWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            )
                        }

                        Text(
                            text = "$dailyAdsCount / 20",
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { dailyAdsCount / 20f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = CyberCyan,
                        trackColor = GamingDarkBg,
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Sistema de seguridad: Límite diario de 20 reproducciones para prevenir tráfico no válido y proteger la cuenta AdMob.",
                        color = TextGray,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    GamingGradientButton(
                        text = if (dailyAdsCount < 20) "REPRODUCIR ANUNCIO (+10 PTS)" else "LÍMITE DIARIO COMPLETADO",
                        onClick = onWatchAd,
                        enabled = dailyAdsCount < 20,
                        icon = Icons.Default.PlayCircle,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "earn_screen_ad_btn"
                    )
                }
            }
        }

        // Section 2: Ruleta de Diamantes
        item {
            GamingCard(
                borderColor = EmberGold.copy(alpha = 0.5f),
                backgroundColor = GamingSurface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Casino,
                                contentDescription = null,
                                tint = EmberGold,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "Ruleta de Diamantes",
                                color = TextWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            )
                        }

                        Text(
                            text = "Hasta +50 Pts",
                            color = EmberGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Wheel Graphical Circle
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .rotate(rotationAngle)
                            .clip(CircleShape)
                            .background(
                                Brush.sweepGradient(
                                    listOf(
                                        CyberCyan,
                                        EmberOrange,
                                        DiamondBlue,
                                        EmberGold,
                                        SuccessGreen,
                                        CyberCyan
                                    )
                                )
                            )
                            .border(3.dp, GamingDarkBg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(GamingDarkBg, CircleShape)
                                .border(2.dp, EmberGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Diamond,
                                contentDescription = null,
                                tint = EmberGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    if (spinRewardPoints != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "¡Ganaste +$spinRewardPoints Puntos!",
                            color = SuccessGreen,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    GamingGradientButton(
                        text = if (isSpinning) "GIRANDO RULETA..." else "GIRAR RULETA",
                        onClick = onSpinWheel,
                        enabled = !isSpinning,
                        gradientColors = listOf(EmberOrange, EmberGold),
                        textColor = GamingDarkBg,
                        icon = Icons.Default.Casino,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "spin_wheel_btn"
                    )
                }
            }
        }

        // Section 3: Free Fire Trivia Quiz Challenge
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = CyberCyan,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "TRIVIA & QUIZ FREE FIRE (+15 PTS)",
                    color = TextWhite,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        items(quizQuestions) { question ->
            val isAnswered = answeredQuestions.contains(question.id)

            GamingCard(
                borderColor = if (isAnswered) SuccessGreen.copy(alpha = 0.5f) else GamingBorder,
                backgroundColor = GamingSurface
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = question.question,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    question.options.forEachIndexed { index, option ->
                        val isSelected = isAnswered && index == question.correctIndex
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) SuccessGreen.copy(alpha = 0.2f) else GamingSurfaceElevated
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) SuccessGreen else GamingBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable(enabled = !isAnswered) {
                                    answeredQuestions = answeredQuestions + question.id
                                    onAnswerQuiz(question, index)
                                }
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${('A' + index)}. $option",
                                    color = if (isSelected) SuccessGreen else TextWhite,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isSelected) {
                                    Text(
                                        text = "+${question.pointsReward} pts",
                                        color = SuccessGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    if (isAnswered) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = question.explanation,
                            color = TextGray,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
