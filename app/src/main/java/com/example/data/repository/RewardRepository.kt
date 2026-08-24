package com.example.data.repository

import com.example.data.model.CanjeRequest
import com.example.data.model.CanjeStatus
import com.example.data.model.DiamondPackage
import com.example.data.model.QuizQuestion
import com.example.data.model.RecentPayout
import com.example.data.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class RewardRepository {

    private val _currentUser = MutableStateFlow<UserData?>(null)
    val currentUser: StateFlow<UserData?> = _currentUser.asStateFlow()

    private val _canjeRequests = MutableStateFlow<List<CanjeRequest>>(emptyList())
    val canjeRequests: StateFlow<List<CanjeRequest>> = _canjeRequests.asStateFlow()

    private val _recentPayouts = MutableStateFlow<List<RecentPayout>>(emptyList())
    val recentPayouts: StateFlow<List<RecentPayout>> = _recentPayouts.asStateFlow()

    val diamondPackages = listOf(
        DiamondPackage(
            id = "pkg_100",
            diamondAmount = 100,
            bonusAmount = 10,
            pointsCost = 10000,
            title = "100 Diamantes",
            tag = "MÁS POPULAR"
        ),
        DiamondPackage(
            id = "pkg_310",
            diamondAmount = 310,
            bonusAmount = 31,
            pointsCost = 30000,
            title = "310 Diamantes",
            tag = "+10% EXTRA"
        ),
        DiamondPackage(
            id = "pkg_520",
            diamondAmount = 520,
            bonusAmount = 52,
            pointsCost = 50000,
            title = "520 Diamantes",
            tag = "MEJOR VALOR"
        ),
        DiamondPackage(
            id = "pkg_1060",
            diamondAmount = 1060,
            bonusAmount = 106,
            pointsCost = 100000,
            title = "1060 Diamantes",
            tag = "PACK ÉLITE"
        ),
        DiamondPackage(
            id = "pkg_pass_week",
            diamondAmount = 450,
            bonusAmount = 0,
            pointsCost = 15000,
            title = "Pase Semanal FF",
            tag = "SUSCRIPCIÓN",
            isPass = true
        ),
        DiamondPackage(
            id = "pkg_pass_month",
            diamondAmount = 1900,
            bonusAmount = 0,
            pointsCost = 60000,
            title = "Pase Mensual VIP",
            tag = "PASE VIP",
            isPass = true
        )
    )

    val quizQuestions = listOf(
        QuizQuestion(
            id = 1,
            question = "¿Cuál es la habilidad especial del personaje 'Alok'?",
            options = listOf("Ritmo Brutal (Cura y Velocidad)", "Escudo de Fuerza", "Ojo de Hacker", "Tiro Certero"),
            correctIndex = 0,
            pointsReward = 15,
            explanation = "Ritmo Brutal genera un aura de 5m que aumenta la velocidad de movimiento y restaura PV."
        ),
        QuizQuestion(
            id = 2,
            question = "¿Qué arma de Free Fire utiliza munición de Escopeta SG?",
            options = listOf("MP40", "M1887", "AWM", "Groza"),
            correctIndex = 1,
            pointsReward = 15,
            explanation = "La M1887 (dos tiros) y la M1014 usan cartuchos de escopeta SG."
        ),
        QuizQuestion(
            id = 3,
            question = "¿Cuál es el mapa clásico y más emblemático de Free Fire?",
            options = listOf("Kalahari", "Purgatorio", "Bermuda", "NeXTerra"),
            correctIndex = 2,
            pointsReward = 15,
            explanation = "Bermuda es el mapa original y más jugado en todos los modos competitivos."
        ),
        QuizQuestion(
            id = 4,
            question = "¿Cuántos puntos se necesitan para canjear el pack básico de 100 Diamantes?",
            options = listOf("1,000", "5,000", "10,000", "50,000"),
            correctIndex = 2,
            pointsReward = 15,
            explanation = "Con 10,000 puntos puedes canjear 100 Diamantes directos a tu ID de Free Fire."
        )
    )

    init {
        // No fake or pre-populated requests/payouts. Real data will appear as user/system interacts.
        _recentPayouts.value = emptyList()
        _canjeRequests.value = emptyList()
    }

    fun login(email: String, freeFireId: String = "", nickname: String = "", region: String = "LATAM") {
        val cleanEmail = email.trim()
        val displayName = if (cleanEmail.contains("@")) cleanEmail.substringBefore("@") else cleanEmail.ifBlank { "Usuario" }
        val generatedReferralCode = if (nickname.isNotBlank()) {
            "FF-" + nickname.replace(Regex("[^a-zA-Z0-9]"), "").take(5).uppercase() + (10..99).random()
        } else {
            "FF-" + (10000..99999).random()
        }

        // Firestore schema: puntos: 0, anuncios_vistos: 0, free_fire_id: "", nickname: ""
        val user = UserData(
            uid = UUID.randomUUID().toString(),
            email = cleanEmail,
            displayName = displayName,
            freeFireId = freeFireId.trim(),
            nickname = nickname.trim(),
            region = region.ifBlank { "LATAM" },
            puntos = 0, // Starts at 0 points
            anunciosVistosHoy = 0, // Starts at 0 ads watched
            ultimoCheckin = 0L,
            streakDays = 0,
            totalReferrals = 0,
            totalDiamantesCanjeados = 0,
            referralCode = generatedReferralCode
        )
        _currentUser.value = user
    }

    fun logout() {
        _currentUser.value = null
    }

    fun addPoints(amount: Int, reason: String = "") {
        _currentUser.update { user ->
            user?.copy(puntos = user.puntos + amount)
        }
    }

    fun incrementAdsWatched(): Boolean {
        var success = false
        _currentUser.update { user ->
            if (user != null && user.anunciosVistosHoy < user.maxAnunciosDiarios) {
                success = true
                user.copy(
                    puntos = user.puntos + 10,
                    anunciosVistosHoy = user.anunciosVistosHoy + 1
                )
            } else {
                user
            }
        }
        return success
    }

    fun claimDailyCheckin(): Result<Int> {
        val user = _currentUser.value ?: return Result.failure(IllegalStateException("No autenticado"))
        
        val currentTime = System.currentTimeMillis()
        val oneDayMillis = 86400000L
        
        // Base reward 50 points, streak bonus on day 7 (+100)
        val newStreak = if (currentTime - user.ultimoCheckin > oneDayMillis * 2) 1 else (user.streakDays % 7) + 1
        val pointsToAward = if (newStreak == 7) 150 else 50

        _currentUser.update {
            it?.copy(
                puntos = it.puntos + pointsToAward,
                ultimoCheckin = currentTime,
                streakDays = newStreak
            )
        }

        return Result.success(pointsToAward)
    }

    fun applyReferralCode(code: String): Result<Int> {
        val user = _currentUser.value ?: return Result.failure(IllegalStateException("No autenticado"))
        
        if (code.trim().equals(user.referralCode, ignoreCase = true)) {
            return Result.failure(IllegalArgumentException("No puedes usar tu propio código de referido."))
        }
        if (user.referredBy != null) {
            return Result.failure(IllegalArgumentException("Ya has canjeado un código de referido anteriormente."))
        }
        if (!code.trim().startsWith("FF-", ignoreCase = true) && code.length < 5) {
            return Result.failure(IllegalArgumentException("Código de referido no válido."))
        }

        val bonus = 100
        _currentUser.update {
            it?.copy(
                puntos = it.puntos + bonus,
                referredBy = code.uppercase()
            )
        }
        return Result.success(bonus)
    }

    fun requestCanje(pkg: DiamondPackage): Result<CanjeRequest> {
        val user = _currentUser.value ?: return Result.failure(IllegalStateException("Inicia sesión primero"))

        if (user.puntos < pkg.pointsCost) {
            return Result.failure(
                IllegalArgumentException("Puntos insuficientes. Necesitas ${pkg.pointsCost} puntos (Tienes ${user.puntos}).")
            )
        }

        val request = CanjeRequest(
            uid = user.uid,
            freeFireId = user.freeFireId,
            nickname = user.nickname,
            diamondAmount = pkg.diamondAmount + pkg.bonusAmount,
            pointsCost = pkg.pointsCost,
            packageName = pkg.title,
            status = CanjeStatus.PENDIENTE,
            timestamp = System.currentTimeMillis()
        )

        // Deduct points and register request
        _currentUser.update {
            it?.copy(
                puntos = it.puntos - pkg.pointsCost,
                totalDiamantesCanjeados = it.totalDiamantesCanjeados + pkg.diamondAmount + pkg.bonusAmount
            )
        }

        _canjeRequests.update { listOf(request) + it }

        // Add to recent community payouts
        _recentPayouts.update {
            listOf(
                RecentPayout(
                    id = request.id,
                    nickname = user.nickname,
                    freeFireId = user.freeFireId.take(4) + "***" + user.freeFireId.takeLast(3),
                    diamondAmount = request.diamondAmount,
                    timeAgo = "Hace un momento"
                )
            ) + it
        }

        return Result.success(request)
    }
}
