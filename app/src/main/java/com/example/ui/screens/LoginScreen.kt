package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.FreeFirePlayer
import com.example.ui.components.GamingCard
import com.example.ui.components.GamingGradientButton
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DiamondBlue
import com.example.ui.theme.EmberGold
import com.example.ui.theme.EmberOrange
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GamingBorder
import com.example.ui.theme.GamingDarkBg
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.GamingSurfaceElevated
import com.example.ui.theme.GamingSurfaceVariant
import com.example.ui.theme.RubyRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite

@Composable
fun LoginScreen(
    isVerifyingId: Boolean,
    verifiedPlayer: FreeFirePlayer?,
    verificationError: String?,
    onVerifyId: (String) -> Unit,
    onLogin: (email: String, freeFireId: String, nickname: String, region: String) -> Unit
) {
    var freeFireIdInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingDarkBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Glowing Esports Logo
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(CyberCyan.copy(alpha = 0.3f), Color.Transparent)
                        ),
                        CircleShape
                    )
                    .border(2.dp, CyberCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Diamond,
                    contentDescription = "FF Logo",
                    tint = CyberCyan,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "FF DIAMOND HUB",
                color = TextWhite,
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                letterSpacing = 2.sp
            )

            Text(
                text = "Recompensas de Diamantes Directas a tu ID",
                color = TextGray,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Step 1: Free Fire ID Input Card
            GamingCard(
                borderColor = if (verifiedPlayer != null) SuccessGreen else GamingBorder,
                backgroundColor = GamingSurface
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(EmberOrange, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "1",
                                color = GamingDarkBg,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "Ingresa tu ID de Free Fire",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = freeFireIdInput,
                        onValueChange = { freeFireIdInput = it },
                        placeholder = { Text("Ej. 1928374650", color = TextGray) },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = CyberCyan)
                        },
                        trailingIcon = {
                            if (isVerifyingId) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = CyberCyan,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                IconButton(
                                    onClick = {
                                        focusManager.clearFocus()
                                        onVerifyId(freeFireIdInput)
                                    },
                                    modifier = Modifier.testTag("verify_id_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Verificar ID",
                                        tint = CyberCyan
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                onVerifyId(freeFireIdInput)
                            }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = GamingBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedContainerColor = GamingSurfaceVariant,
                            unfocusedContainerColor = GamingSurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ff_id_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    GamingGradientButton(
                        text = if (isVerifyingId) "CONSULTANDO PAGOSTORE..." else "VERIFICAR NICKNAME",
                        onClick = {
                            focusManager.clearFocus()
                            onVerifyId(freeFireIdInput)
                        },
                        enabled = !isVerifyingId && freeFireIdInput.length >= 8,
                        icon = Icons.Default.Search,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "verify_nickname_action"
                    )

                    if (verificationError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = verificationError,
                            color = ErrorRed,
                            fontSize = 12.sp
                        )
                    }

                    // Verified Nickname Banner
                    if (verifiedPlayer != null) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SuccessGreen.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                                .border(1.dp, SuccessGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(14.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verificado",
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                                Column {
                                    Text(
                                        text = verifiedPlayer.nickname,
                                        color = TextWhite,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "Nivel: ${verifiedPlayer.level} • Región: ${verifiedPlayer.region} • ${verifiedPlayer.rank}",
                                        color = SuccessGreen,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Step 2: Google Sign-In Card
            GamingCard(
                borderColor = GamingBorder,
                backgroundColor = GamingSurface
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(CyberCyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "2",
                                color = GamingDarkBg,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "Iniciar Sesión con Google",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Vincula tu cuenta de Google para guardar tus puntos en Firebase y asegurar tus canjes.",
                        color = TextGray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Google Sign-In Button
                    GamingGradientButton(
                        text = "CONTINUAR CON GOOGLE",
                        onClick = {
                            val id = verifiedPlayer?.id ?: freeFireIdInput.trim()
                            val nick = verifiedPlayer?.nickname ?: ""
                            val reg = verifiedPlayer?.region ?: "LATAM"
                            val email = if (emailInput.isNotBlank()) emailInput.trim() else "usuario@gmail.com"
                            onLogin(email, id, nick, reg)
                        },
                        gradientColors = listOf(Color(0xFF4285F4), Color(0xFF34A853)),
                        textColor = TextWhite,
                        icon = Icons.Default.Shield,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "google_signin_button"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
