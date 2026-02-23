package com.fitness.controller

import com.fitness.dto.*
import com.fitness.service.StatsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/stats")
@Tag(name = "Stats", description = "통계 API")
class StatsController(
    private val statsService: StatsService
) {

    @GetMapping("/overview")
    @Operation(summary = "대시보드 통계")
    fun getOverviewStats(authentication: Authentication): ResponseEntity<OverviewStatsResponse> {
        val userId = authentication.principal as Long
        val response = statsService.getOverviewStats(userId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/streak")
    @Operation(summary = "연속 기록")
    fun getStreak(authentication: Authentication): ResponseEntity<StreakResponse> {
        val userId = authentication.principal as Long
        val response = statsService.getStreak(userId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/weight-history")
    @Operation(summary = "체중 기록")
    fun getWeightHistory(
        authentication: Authentication,
        @RequestParam(defaultValue = "30") days: Int
    ): ResponseEntity<List<WeightHistoryResponse>> {
        val userId = authentication.principal as Long
        val response = statsService.getWeightHistory(userId, days)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/workout-frequency")
    @Operation(summary = "운동 빈도")
    fun getWorkoutFrequency(
        authentication: Authentication,
        @RequestParam(defaultValue = "30") days: Int
    ): ResponseEntity<List<WorkoutFrequencyResponse>> {
        val userId = authentication.principal as Long
        val response = statsService.getWorkoutFrequency(userId, days)
        return ResponseEntity.ok(response)
    }
}
