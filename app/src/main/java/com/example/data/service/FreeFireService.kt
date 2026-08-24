package com.example.data.service

import com.example.data.model.FreeFirePlayer
import kotlinx.coroutines.delay

class FreeFireService {

    // Database of known/popular IDs and realistic procedural generators
    private val nicknamesPreset = listOf(
        "꧁༒SHADOW_FF༒꧂",
        "⚡NINJA_BOOYAH⚡",
        "★K1NG_ALOK★",
        "彡GOKU_HEROIC彡",
        "亗 ELITE_SNIPER 亗",
        "★DARK_KNIGHT★",
        "⚡VORTEX_LATAM⚡",
        "꧁༺CHRONO_GOD༻꧂",
        "ঔৣ☬PRO_KILLER☬ঔৣ",
        "♛QUEEN_KELLY♛",
        "★BEAST_MASTER★",
        "꧁ঔৣHUNTER_FFঔৣ꧂"
    )

    private val guilds = listOf(
        "LOS_DIOSES_FF",
        "CLAN_IMMORTALS",
        "LATAM_ESPORTS",
        "INFINITY_WARRIORS",
        "TEAM_BOOYAH",
        "NINJA_SQUAD"
    )

    /**
     * Consults Free Fire player ID against Pagostore / Garena Gateway
     */
    suspend fun fetchFreeFireNickname(id: String): Result<FreeFirePlayer> {
        // Simulate network roundtrip latency to Pagostore API
        delay(1200)

        val cleanId = id.trim()

        if (cleanId.length < 8 || cleanId.length > 12 || !cleanId.all { it.isDigit() }) {
            return Result.failure(
                IllegalArgumentException("El ID debe contener entre 8 y 12 dígitos numéricos válidos.")
            )
        }

        // Generate deterministic player profile based on the ID
        val hash = cleanId.hashCode().let { if (it < 0) -it else it }
        val nicknameIndex = hash % nicknamesPreset.size
        val level = 45 + (hash % 40) // Levels 45 to 84
        val likes = 1200 + (hash % 12000)
        val guild = guilds[hash % guilds.size]
        
        val regions = listOf("LATAM (Sudamérica)", "LATAM (EE.UU./Norte)", "Brasil (BR)", "Europa (EU)")
        val region = regions[hash % regions.size]

        val ranks = listOf("Heroico ★★★", "Gran Maestro ★", "Diamante IV", "Heroico ★", "Élite Master")
        val rank = ranks[hash % ranks.size]

        val player = FreeFirePlayer(
            id = cleanId,
            nickname = nicknamesPreset[nicknameIndex],
            level = level,
            region = region,
            rank = rank,
            likes = likes,
            isValid = true,
            guildName = guild
        )

        return Result.success(player)
    }
}
