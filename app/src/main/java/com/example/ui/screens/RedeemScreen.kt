package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CanjeRequest
import com.example.data.model.DiamondPackage
import com.example.data.model.UserData
import com.example.ui.components.DiamondPointsBadge
import com.example.ui.components.GamingCard
import com.example.ui.components.GamingGradientButton
import com.example.ui.components.StatusBadge
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
fun RedeemScreen(
    user: UserData,
    packages: List<DiamondPackage>,
    requests: List<CanjeRequest>,
    onRequestCanje: (DiamondPackage) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedPackageToCanje by remember { mutableStateOf<DiamondPackage?>(null) }

    // Canje Confirmation Dialog
    if (selectedPackageToCanje != null) {
        val pkg = selectedPackageToCanje!!
        AlertDialog(
            onDismissRequest = { selectedPackageToCanje = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = null,
                        tint = DiamondBlue
                    )
                    Text("Confirmar Canje", color = TextWhite, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "¿Deseas canjear ${pkg.diamondAmount + pkg.bonusAmount} Diamantes por %,d puntos?".format(pkg.pointsCost),
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GamingSurfaceElevated, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("ID de Destino: ${user.freeFireId}", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Jugador: ${user.nickname}", color = TextWhite, fontSize = 12.sp)
                            Text("Región: ${user.region}", color = TextGray, fontSize = 11.sp)
                        }
                    }
                }
            },
            confirmButton = {
                GamingGradientButton(
                    text = "CONFIRMAR CANJE",
                    onClick = {
                        onRequestCanje(pkg)
                        selectedPackageToCanje = null
                    },
                    enabled = user.puntos >= pkg.pointsCost,
                    gradientColors = listOf(CyberCyan, DiamondBlue),
                    modifier = Modifier.height(42.dp)
                )
            },
            dismissButton = {
                TextButton(onClick = { selectedPackageToCanje = null }) {
                    Text("Cancelar", color = TextGray)
                }
            },
            containerColor = GamingSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingDarkBg)
    ) {
        // Tab Row: Tienda vs Mis Canjes
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = GamingSurface,
            contentColor = CyberCyan,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = CyberCyan
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Tienda de Diamantes", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Diamond, contentDescription = null) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Mis Solicitudes (${requests.size})", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.ReceiptLong, contentDescription = null) }
            )
        }

        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    // Header Banner with balance
                    GamingCard(
                        borderColor = GamingBorder,
                        backgroundColor = GamingSurface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "CANJE DIRECTO A TU ID",
                                    color = TextWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Entrega en cuenta: ${user.freeFireId}",
                                    color = CyberCyan,
                                    fontSize = 12.sp
                                )
                            }
                            DiamondPointsBadge(puntos = user.puntos)
                        }
                    }
                }

                items(packages) { pkg ->
                    val canAfford = user.puntos >= pkg.pointsCost
                    val totalDiamonds = pkg.diamondAmount + pkg.bonusAmount

                    GamingCard(
                        borderColor = if (canAfford) DiamondBlue.copy(alpha = 0.6f) else GamingBorder,
                        backgroundColor = GamingSurface
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .background(
                                                Brush.radialGradient(
                                                    listOf(DiamondBlue.copy(alpha = 0.3f), Color.Transparent)
                                                ),
                                                CircleShape
                                            )
                                            .border(1.dp, DiamondBlue, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Diamond,
                                            contentDescription = null,
                                            tint = DiamondBlue,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = pkg.title,
                                            color = TextWhite,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            text = if (pkg.bonusAmount > 0) "${pkg.diamondAmount} + ${pkg.bonusAmount} Bonus 💎" else "$totalDiamonds Diamantes",
                                            color = DiamondBlue,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }

                                if (pkg.tag != null) {
                                    Box(
                                        modifier = Modifier
                                            .background(EmberOrange.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                            .border(1.dp, EmberOrange, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = pkg.tag,
                                            color = EmberOrange,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = EmberGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "%,d Puntos".format(pkg.pointsCost),
                                        color = EmberGold,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp
                                    )
                                }

                                GamingGradientButton(
                                    text = if (canAfford) "CANJEAR" else "${user.puntos}/%,d".format(pkg.pointsCost),
                                    onClick = { selectedPackageToCanje = pkg },
                                    enabled = canAfford,
                                    gradientColors = listOf(DiamondBlue, Color(0xFF0077B6)),
                                    textColor = TextWhite,
                                    modifier = Modifier.height(42.dp),
                                    testTag = "redeem_${pkg.id}"
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        } else {
            // Mis Solicitudes de Canje Tab
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (requests.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = TextGray,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Aún no tienes solicitudes de canje",
                                color = TextGray,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(requests) { req ->
                        GamingCard(
                            borderColor = GamingBorder,
                            backgroundColor = GamingSurface
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = req.packageName,
                                        color = TextWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    StatusBadge(status = req.status)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "ID Free Fire: ${req.freeFireId} (${req.nickname})",
                                    color = CyberCyan,
                                    fontSize = 12.sp
                                )

                                Text(
                                    text = "Ref: ${req.transactionId} • Costo: %,d pts".format(req.pointsCost),
                                    color = TextGray,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
