package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AdMobVideoOverlay
import com.example.ui.components.DiamondPointsBadge
import com.example.ui.screens.EarnScreen
import com.example.ui.screens.FlutterCodeScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.RedeemScreen
import com.example.ui.screens.ReferralsScreen
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DiamondBlue
import com.example.ui.theme.EmberGold
import com.example.ui.theme.EmberOrange
import com.example.ui.theme.GamingBorder
import com.example.ui.theme.GamingDarkBg
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.GamingSurfaceElevated
import com.example.ui.theme.GamingSurfaceVariant
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.UiEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContainer()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer(viewModel: MainViewModel = viewModel()) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val isVerifyingId by viewModel.isVerifyingId.collectAsStateWithLifecycle()
    val verifiedPlayer by viewModel.verifiedPlayer.collectAsStateWithLifecycle()
    val verificationError by viewModel.verificationError.collectAsStateWithLifecycle()
    val adState by viewModel.adState.collectAsStateWithLifecycle()
    val dailyAdsCount by viewModel.dailyAdsCount.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val canjeRequests by viewModel.canjeRequests.collectAsStateWithLifecycle()
    val recentPayouts by viewModel.recentPayouts.collectAsStateWithLifecycle()
    val isSpinning by viewModel.isSpinning.collectAsStateWithLifecycle()
    val spinRewardPoints by viewModel.spinRewardPoints.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Observe single-shot events
    LaunchedEffect(key1 = true) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is UiEvent.CanjeSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = "Solicitud #${event.request.id} generada. 100 Diamantes en proceso a ID: ${event.request.freeFireId}."
                    )
                }
            }
        }
    }

    // AdMob Fullscreen Video Overlay
    AdMobVideoOverlay(
        adState = adState,
        onDismiss = { viewModel.dismissAd() }
    )

    if (currentUser == null) {
        LoginScreen(
            isVerifyingId = isVerifyingId,
            verifiedPlayer = verifiedPlayer,
            verificationError = verificationError,
            onVerifyId = { viewModel.verifyFreeFireId(it) },
            onLogin = { email, id, nick, reg ->
                viewModel.login(email, id, nick, reg)
            }
        )
    } else {
        val user = currentUser!!

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(GamingDarkBg),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(CyberCyan, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Diamond,
                                    contentDescription = null,
                                    tint = GamingDarkBg,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "FF DIAMONDS",
                                    color = TextWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = user.nickname.ifBlank { user.displayName.ifBlank { "Usuario" } },
                                    color = CyberCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    },
                    actions = {
                        DiamondPointsBadge(puntos = user.puntos)
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = { viewModel.logout() },
                            modifier = Modifier.testTag("logout_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Cerrar Sesión",
                                tint = TextGray
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = GamingSurface,
                        titleContentColor = TextWhite
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF06090F),
                    contentColor = CyberCyan,
                    modifier = Modifier.border(1.dp, GamingBorder.copy(alpha = 0.5f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                ) {
                    val tabs = listOf(
                        Triple("INICIO", Icons.Default.Home, 0),
                        Triple("CANJE", Icons.Default.Diamond, 1),
                        Triple("PREMIOS", Icons.Default.Casino, 2),
                        Triple("REFERIDOS", Icons.Default.Share, 3),
                        Triple("FLUTTER", Icons.Default.Code, 4)
                    )

                    tabs.forEach { (title, icon, index) ->
                        val isSelected = selectedTab == index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.selectTab(index) },
                            icon = {
                                if (index == 1 && canjeRequests.isNotEmpty()) {
                                    BadgedBox(badge = {
                                        Badge(containerColor = CyberCyan) {
                                            Text(
                                                text = "${canjeRequests.size}",
                                                color = Color(0xFF0F172A),
                                                fontWeight = FontWeight.Black,
                                                fontSize = 9.sp
                                            )
                                        }
                                    }) {
                                        Icon(icon, contentDescription = title)
                                    }
                                } else {
                                    Icon(icon, contentDescription = title)
                                }
                            },
                            label = {
                                Text(
                                    text = title,
                                    fontSize = 9.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CyberCyan,
                                selectedTextColor = CyberCyan,
                                indicatorColor = CyberCyan.copy(alpha = 0.15f),
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(GamingDarkBg)
            ) {
                when (selectedTab) {
                    0 -> HomeScreen(
                        user = user,
                        dailyAdsCount = dailyAdsCount,
                        recentPayouts = recentPayouts,
                        onWatchAd = { viewModel.watchRewardedAd() },
                        onDailyCheckin = { viewModel.claimDailyCheckin() },
                        onNavigateTab = { viewModel.selectTab(it) }
                    )
                    1 -> RedeemScreen(
                        user = user,
                        packages = viewModel.diamondPackages,
                        requests = canjeRequests,
                        onRequestCanje = { viewModel.requestCanje(it) }
                    )
                    2 -> EarnScreen(
                        user = user,
                        dailyAdsCount = dailyAdsCount,
                        quizQuestions = viewModel.quizQuestions,
                        isSpinning = isSpinning,
                        spinRewardPoints = spinRewardPoints,
                        onWatchAd = { viewModel.watchRewardedAd() },
                        onSpinWheel = { viewModel.spinWheel() },
                        onAnswerQuiz = { question, index ->
                            viewModel.answerQuiz(question, index)
                        }
                    )
                    3 -> ReferralsScreen(
                        user = user,
                        onApplyCode = { viewModel.applyReferralCode(it) }
                    )
                    4 -> FlutterCodeScreen(
                        snippets = viewModel.flutterSnippets
                    )
                }
            }
        }
    }
}
