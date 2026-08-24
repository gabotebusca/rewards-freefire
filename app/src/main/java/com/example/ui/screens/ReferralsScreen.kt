package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun ReferralsScreen(
    user: UserData,
    onApplyCode: (String) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    var friendCodeInput by remember { mutableStateOf("") }

    fun shareReferralCode() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "¡Únete a FF Diamond Rewards y canjea diamantes para Free Fire gratis! Usa mi código de referido: ${user.referralCode} para ganar +100 puntos extra al registrarte."
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Compartir código de referido")
        context.startActivity(shareIntent)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingDarkBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Referral Box
        item {
            GamingCard(
                borderColor = SuccessGreen.copy(alpha = 0.6f),
                backgroundColor = GamingSurface
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                Brush.radialGradient(
                                    listOf(SuccessGreen.copy(alpha = 0.3f), GamingDarkBg)
                                ),
                                CircleShape
                            )
                            .border(1.dp, SuccessGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "SISTEMA DE REFERIDOS",
                        color = TextWhite,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = "Invita a tus amigos y ambos ganarán puntos. ¡Tu amigo recibe +100 pts y tú ganas +200 pts por cada invitado!",
                        color = TextGray,
                        fontSize = 12.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Unique Code Display
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GamingSurfaceElevated, RoundedCornerShape(12.dp))
                            .border(1.dp, GamingBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "TU CÓDIGO ÚNICO",
                                    color = TextGray,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = user.referralCode,
                                    color = CyberCyan,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 20.sp,
                                    letterSpacing = 2.sp
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(GamingSurfaceVariant)
                                        .clickable {
                                            clipboardManager.setText(AnnotatedString(user.referralCode))
                                            Toast.makeText(context, "Código copiado al portapapeles", Toast.LENGTH_SHORT).show()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copiar",
                                        tint = TextWhite,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(SuccessGreen)
                                        .clickable { shareReferralCode() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Compartir",
                                        tint = GamingDarkBg,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    GamingGradientButton(
                        text = "COMPARTIR CON AMIGOS",
                        onClick = { shareReferralCode() },
                        gradientColors = listOf(SuccessGreen, Color(0xFF00BFA5)),
                        textColor = GamingDarkBg,
                        icon = Icons.Default.Share,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "share_referral_btn"
                    )
                }
            }
        }

        // Apply Friend's Code Card
        item {
            GamingCard(
                borderColor = GamingBorder,
                backgroundColor = GamingSurface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "¿Tienes el código de un amigo?",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Ingrésalo para recibir +100 Puntos al instante en tu saldo.",
                        color = TextGray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = friendCodeInput,
                        onValueChange = { friendCodeInput = it },
                        placeholder = { Text("Ej. FF-NINJA84", color = TextGray) },
                        leadingIcon = {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, tint = EmberGold)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmberGold,
                            unfocusedBorderColor = GamingBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedContainerColor = GamingSurfaceVariant,
                            unfocusedContainerColor = GamingSurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("friend_code_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    GamingGradientButton(
                        text = "CANJEAR CÓDIGO (+100 PTS)",
                        onClick = {
                            onApplyCode(friendCodeInput)
                            friendCodeInput = ""
                        },
                        enabled = friendCodeInput.isNotBlank(),
                        gradientColors = listOf(EmberOrange, EmberGold),
                        textColor = GamingDarkBg,
                        icon = Icons.Default.Star,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "apply_referral_btn"
                    )
                }
            }
        }

        // Stats Card
        item {
            GamingCard(
                borderColor = GamingBorder,
                backgroundColor = GamingSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${user.totalReferrals}",
                            color = CyberCyan,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Amigos Invitados",
                            color = TextGray,
                            fontSize = 11.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(GamingBorder)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "+${user.totalReferrals * 200}",
                            color = EmberGold,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Puntos Ganados",
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
