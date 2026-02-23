package com.fitness.controller

import com.fitness.dto.*
import com.fitness.service.CheckinService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/checkins")
@Tag(name = "Checkins", description = "일일 체크인 API")
class CheckinController(
    private val checkinService: CheckinService
) {

    @GetMapping("/today")
    @Operation(summary = "오늘 체크인 조회")
    fun getTodayCheckin(authentication: Authentication): ResponseEntity<DailyCheckinResponse?> {
        val userId = authentication.principal as Long
        val response = checkinService.getTodayCheckin(userId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "특정 날짜 체크인 조회")
    fun getCheckin(
        authentication: Authentication,
        @PathVariable date: LocalDate
    ): ResponseEntity<DailyCheckinResponse?> {
        val userId = authentication.principal as Long
        val response = checkinService.getCheckin(userId, date)
        return ResponseEntity.ok(response)
    }

    @PostMapping
    @Operation(summary = "체크인 생성/수정")
    fun createOrUpdateCheckin(
        authentication: Authentication,
        @Valid @RequestBody request: DailyCheckinRequest
    ): ResponseEntity<DailyCheckinResponse> {
        val userId = authentication.principal as Long
        val response = checkinService.createOrUpdateCheckin(userId, request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/calendar/{year}/{month}")
    @Operation(summary = "월별 캘린더 조회")
    fun getMonthlyCalendar(
        authentication: Authentication,
        @PathVariable year: Int,
        @PathVariable month: Int
    ): ResponseEntity<MonthlyCalendarResponse> {
        val userId = authentication.principal as Long
        val response = checkinService.getMonthlyCalendar(userId, year, month)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/recent")
    @Operation(summary = "최근 체크인 조회")
    fun getRecentCheckins(
        authentication: Authentication,
        @RequestParam(defaultValue = "7") limit: Int
    ): ResponseEntity<List<DailyCheckinResponse>> {
        val userId = authentication.principal as Long
        val response = checkinService.getRecentCheckins(userId, limit)
        return ResponseEntity.ok(response)
    }
}
