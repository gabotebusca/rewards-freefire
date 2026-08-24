package com.example.data.model

import java.util.UUID

enum class CanjeStatus(val label: String, val colorHex: Long) {
    PENDIENTE("Pendiente", 0xFFFFBE0B),
    EN_PROCESO("En Proceso", 0xFF38B6FF),
    COMPLETADO("Entregado", 0xFF06D6A0),
    RECHAZADO("Rechazado", 0xFFEF476F)
}

data class UserData(
    val uid: String = UUID.randomUUID().toString(),
    val email: String = "",
    val displayName: String = "",
    val freeFireId: String = "",
    val nickname: String = "",
    val region: String = "LATAM",
    val puntos: Int = 0,
    val anunciosVistosHoy: Int = 0,
    val maxAnunciosDiarios: Int = 20,
    val ultimoCheckin: Long = 0L,
    val streakDays: Int = 0,
    val referralCode: String = "",
    val referredBy: String? = null,
    val totalReferrals: Int = 0,
    val totalDiamantesCanjeados: Int = 0,
    val avatarId: Int = 1
)

data class FreeFirePlayer(
    val id: String,
    val nickname: String,
    val level: Int = 68,
    val region: String = "LATAM",
    val rank: String = "Heroico ★★★",
    val likes: Int = 3450,
    val isValid: Boolean = true,
    val guildName: String = "LOS_DIOSES_FF"
)

data class DiamondPackage(
    val id: String,
    val diamondAmount: Int,
    val bonusAmount: Int = 0,
    val pointsCost: Int,
    val title: String,
    val tag: String? = null,
    val isPass: Boolean = false,
    val iconRes: String = "diamond"
)

data class CanjeRequest(
    val id: String = "REQ-" + (10000..99999).random(),
    val uid: String,
    val freeFireId: String,
    val nickname: String,
    val diamondAmount: Int,
    val pointsCost: Int,
    val packageName: String,
    val status: CanjeStatus = CanjeStatus.PENDIENTE,
    val timestamp: Long = System.currentTimeMillis(),
    val transactionId: String = "GFF-" + UUID.randomUUID().toString().take(8).uppercase()
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val pointsReward: Int = 15,
    val explanation: String
)

data class RecentPayout(
    val id: String,
    val nickname: String,
    val freeFireId: String,
    val diamondAmount: Int,
    val timeAgo: String
)

data class FlutterCodeSnippet(
    val fileName: String,
    val category: String,
    val description: String,
    val code: String
)
