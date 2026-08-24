package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.model.UserData
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun homeScreen_screenshot() {
    val sampleUser = UserData(
      uid = "test-uid",
      email = "gamer@freefire.gg",
      freeFireId = "1928374650",
      nickname = "꧁༒SHADOW_FF༒꧂",
      region = "LATAM",
      puntos = 4820,
      referralCode = "FF-SHADOW84"
    )

    composeTestRule.setContent {
      MyApplicationTheme {
        HomeScreen(
          user = sampleUser,
          dailyAdsCount = 12,
          recentPayouts = emptyList(),
          onWatchAd = {},
          onDailyCheckin = {},
          onNavigateTab = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/home_screen.png")
  }

  @Test
  fun newUser_zeroPoints_screenshot() {
    val cleanNewUser = UserData(
      uid = "new-uid-123",
      email = "nuevousuario@gmail.com",
      displayName = "NuevoUsuario",
      freeFireId = "",
      nickname = "",
      region = "LATAM",
      puntos = 0,
      anunciosVistosHoy = 0,
      streakDays = 0,
      totalReferrals = 0,
      referralCode = "FF-USER01"
    )

    composeTestRule.setContent {
      MyApplicationTheme {
        HomeScreen(
          user = cleanNewUser,
          dailyAdsCount = 0,
          recentPayouts = emptyList(),
          onWatchAd = {},
          onDailyCheckin = {},
          onNavigateTab = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/new_user_zero_points.png")
  }
}
