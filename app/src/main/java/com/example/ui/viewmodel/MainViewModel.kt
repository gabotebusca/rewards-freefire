package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CanjeRequest
import com.example.data.model.DiamondPackage
import com.example.data.model.FreeFirePlayer
import com.example.data.model.QuizQuestion
import com.example.data.model.RecentPayout
import com.example.data.model.UserData
import com.example.data.repository.FlutterCodeRepository
import com.example.data.repository.RewardRepository
import com.example.data.service.AdMobService
import com.example.data.service.AdState
import com.example.data.service.FreeFireService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

sealed class UiEvent {
    data class ShowToast(val message: String, val isError: Boolean = false) : UiEvent()
    data class CanjeSuccess(val request: CanjeRequest) : UiEvent()
}

class MainViewModel(
    private val rewardRepository: RewardRepository = RewardRepository(),
    private val freeFireService: FreeFireService = FreeFireService(),
    private val adMobService: AdMobService = AdMobService()
) : ViewModel() {

    val currentUser: StateFlow<UserData?> = rewardRepository.currentUser
    val canjeRequests: StateFlow<List<CanjeRequest>> = rewardRepository.canjeRequests
    val recentPayouts: StateFlow<List<RecentPayout>> = rewardRepository.recentPayouts
    val diamondPackages: List<DiamondPackage> = rewardRepository.diamondPackages
    val quizQuestions: List<QuizQuestion> = rewardRepository.quizQuestions

    val adState: StateFlow<AdState> = adMobService.adState
    val dailyAdsCount: StateFlow<Int> = adMobService.dailyAdsCount
    val flutterSnippets = FlutterCodeRepository.snippets

    // ID verification state
    private val _isVerifyingId = MutableStateFlow(false)
    val isVerifyingId: StateFlow<Boolean> = _isVerifyingId.asStateFlow()

    private val _verifiedPlayer = MutableStateFlow<FreeFirePlayer?>(null)
    val verifiedPlayer: StateFlow<FreeFirePlayer?> = _verifiedPlayer.asStateFlow()

    private val _verificationError = MutableStateFlow<String?>(null)
    val verificationError: StateFlow<String?> = _verificationError.asStateFlow()

    // Spin wheel state
    private val _isSpinning = MutableStateFlow(false)
    val isSpinning: StateFlow<Boolean> = _isSpinning.asStateFlow()

    private val _spinRewardPoints = MutableStateFlow<Int?>(null)
    val spinRewardPoints: StateFlow<Int?> = _spinRewardPoints.asStateFlow()

    // Selected tab
    private val _selectedTab = MutableStateFlow(0) // 0: Home, 1: Redeem, 2: Earn/Ads, 3: Referrals, 4: Flutter Code
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents: SharedFlow<UiEvent> = _uiEvents.asSharedFlow()

    init {
        // App starts in unauthenticated state waiting for Google Sign-In
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun verifyFreeFireId(id: String) {
        if (id.isBlank() || id.length < 8) {
            _verificationError.value = "Ingresa un ID válido de Free Fire (8-12 dígitos)."
            return
        }

        viewModelScope.launch {
            _isVerifyingId.value = true
            _verificationError.value = null

            val result = freeFireService.fetchFreeFireNickname(id)
            result.onSuccess { player ->
                _verifiedPlayer.value = player
                _verificationError.value = null
                _uiEvents.emit(UiEvent.ShowToast("ID verificado: ${player.nickname} (${player.region})"))
            }.onFailure { error ->
                _verifiedPlayer.value = null
                _verificationError.value = error.message ?: "No se pudo consultar el ID."
            }

            _isVerifyingId.value = false
        }
    }

    fun login(email: String, freeFireId: String = "", nickname: String = "", region: String = "LATAM") {
        // Creates a new user with 0 points, 0 ads watched, and the provided or blank FF ID & nickname
        rewardRepository.login(email, freeFireId, nickname, region)
        viewModelScope.launch {
            val name = nickname.ifBlank { if (email.contains("@")) email.substringBefore("@") else "Usuario" }
            _uiEvents.emit(UiEvent.ShowToast("¡Bienvenido, $name! Saldo inicial: 0 Puntos"))
        }
    }

    fun logout() {
        rewardRepository.logout()
        _verifiedPlayer.value = null
    }

    fun watchRewardedAd() {
        viewModelScope.launch {
            if (!adMobService.canWatchAd()) {
                _uiEvents.emit(UiEvent.ShowToast("Límite diario de 20 anuncios alcanzado. Protegemos tu cuenta de AdMob.", isError = true))
                return@launch
            }

            adMobService.showRewardedAd(
                onRewardEarned = { points ->
                    rewardRepository.addPoints(points, "Anuncio AdMob")
                    viewModelScope.launch {
                        _uiEvents.emit(UiEvent.ShowToast("¡+$points Puntos acreditados con éxito!"))
                    }
                },
                onAdFinished = {
                    // Ad completed
                }
            )
        }
    }

    fun dismissAd() {
        adMobService.dismissAd()
    }

    fun claimDailyCheckin() {
        val result = rewardRepository.claimDailyCheckin()
        viewModelScope.launch {
            result.onSuccess { points ->
                _uiEvents.emit(UiEvent.ShowToast("¡Check-in diario completado! +$points Puntos ganados"))
            }.onFailure { error ->
                _uiEvents.emit(UiEvent.ShowToast(error.message ?: "Error al reclamar check-in", isError = true))
            }
        }
    }

    fun applyReferralCode(code: String) {
        val result = rewardRepository.applyReferralCode(code)
        viewModelScope.launch {
            result.onSuccess { points ->
                _uiEvents.emit(UiEvent.ShowToast("¡Código aplicado! +$points Puntos bonus"))
            }.onFailure { error ->
                _uiEvents.emit(UiEvent.ShowToast(error.message ?: "Error al canjear código", isError = true))
            }
        }
    }

    fun requestCanje(pkg: DiamondPackage) {
        val result = rewardRepository.requestCanje(pkg)
        viewModelScope.launch {
            result.onSuccess { request ->
                _uiEvents.emit(UiEvent.CanjeSuccess(request))
                _uiEvents.emit(UiEvent.ShowToast("¡Solicitud enviada para ${request.diamondAmount} Diamantes!"))
            }.onFailure { error ->
                _uiEvents.emit(UiEvent.ShowToast(error.message ?: "Error en el canje", isError = true))
            }
        }
    }

    fun answerQuiz(question: QuizQuestion, selectedOptionIndex: Int) {
        viewModelScope.launch {
            if (selectedOptionIndex == question.correctIndex) {
                rewardRepository.addPoints(question.pointsReward, "Quiz Free Fire")
                _uiEvents.emit(UiEvent.ShowToast("¡Respuesta Correcta! +${question.pointsReward} Puntos"))
            } else {
                _uiEvents.emit(UiEvent.ShowToast("Respuesta incorrecta. ¡Sigue intentando!", isError = true))
            }
        }
    }

    fun spinWheel() {
        if (_isSpinning.value) return
        viewModelScope.launch {
            _isSpinning.value = true
            _spinRewardPoints.value = null
            delay(2000)
            val rewards = listOf(5, 10, 15, 25, 50)
            val won = rewards.random()
            _spinRewardPoints.value = won
            rewardRepository.addPoints(won, "Ruleta de Diamantes")
            _isSpinning.value = false
            _uiEvents.emit(UiEvent.ShowToast("¡Ganaste $won Puntos en la Ruleta!"))
        }
    }
}
