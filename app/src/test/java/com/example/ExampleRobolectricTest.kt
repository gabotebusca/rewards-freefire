package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("FF Diamond Rewards", appName)
  }

  @Test
  fun `verify reward repository initial state and daily checkin`() {
    val repo = com.example.data.repository.RewardRepository()
    repo.login("test@gmail.com", "1928374650", "꧁༒TEST༒꧂", "LATAM")
    val user = repo.currentUser.value
    org.junit.Assert.assertNotNull(user)
    assertEquals("1928374650", user?.freeFireId)
    assertEquals(0, user?.puntos) // Starts with 0 points
    assertEquals(0, user?.anunciosVistosHoy) // Starts with 0 ads
    
    val claimResult = repo.claimDailyCheckin()
    org.junit.Assert.assertTrue(claimResult.isSuccess)
    assertEquals(50, repo.currentUser.value?.puntos) // Receives 50 points upon daily checkin
  }
}
