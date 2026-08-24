package com.example.data.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class AdState {
    object Idle : AdState()
    object Loading : AdState()
    object Ready : AdState()
    data class Playing(val secondsRemaining: Int, val canSkip: Boolean = false) : AdState()
    data class RewardEarned(val pointsGained: Int = 10) : AdState()
    data class Error(val message: String) : AdState()
}

class AdMobService {

    // Test Ad Unit ID for Rewarded Ads (Google AdMob official test ID)
    val testRewardedAdUnitId = "ca-app-pub-3940256099942544/5224354917"

    private val _adState = MutableStateFlow<AdState>(AdState.Ready)
    val adState: StateFlow<AdState> = _adState.asStateFlow()

    private val _dailyAdsCount = MutableStateFlow(0)
    val dailyAdsCount: StateFlow<Int> = _dailyAdsCount.asStateFlow()

    val maxDailyAds = 20
    val rewardPointsPerAd = 10

    fun canWatchAd(): Boolean {
        return _dailyAdsCount.value < maxDailyAds
    }

    suspend fun loadRewardedAd() {
        _adState.value = AdState.Loading
        delay(1000)
        _adState.value = AdState.Ready
    }

    suspend fun showRewardedAd(onRewardEarned: (points: Int) -> Unit, onAdFinished: () -> Unit) {
        if (!canWatchAd()) {
            _adState.value = AdState.Error("Límite diario de 20 anuncios alcanzado. Protegemos tu cuenta contra ban de AdMob.")
            return
        }

        // Play 5-second simulated video ad
        for (i in 5 downTo 1) {
            _adState.value = AdState.Playing(secondsRemaining = i)
            delay(1000)
        }

        // Reward earned
        _dailyAdsCount.value += 1
        onRewardEarned(rewardPointsPerAd)
        _adState.value = AdState.RewardEarned(rewardPointsPerAd)
        
        delay(1200)
        _adState.value = AdState.Idle
        onAdFinished()
        
        // Auto-preload next ad
        loadRewardedAd()
    }

    fun dismissAd() {
        _adState.value = AdState.Ready
    }

    fun resetDailyCountForTesting() {
        _dailyAdsCount.value = 0
    }
}
