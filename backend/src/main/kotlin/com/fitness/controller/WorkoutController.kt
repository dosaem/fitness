package com.fitness.controller

import com.fitness.dto.*
import com.fitness.service.WorkoutService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/workouts")
@Tag(name = "Workouts", description = "운동 기록 API")
class WorkoutController(
    private val workoutService: WorkoutService
) {

    @GetMapping
    @Operation(summary = "운동 기록 목록 조회")
    fun getWorkouts(
        authentication: Authentication,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<WorkoutLogResponse>> {
        val userId = authentication.principal as Long
        val response = workoutService.getWorkouts(userId, page, size)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "특정 날짜 운동 기록 조회")
    fun getWorkoutsByDate(
        authentication: Authentication,
        @PathVariable date: LocalDate
    ): ResponseEntity<List<WorkoutLogResponse>> {
        val userId = authentication.principal as Long
        val response = workoutService.getWorkoutsByDate(userId, date)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/range")
    @Operation(summary = "기간별 운동 기록 조회")
    fun getWorkoutsByDateRange(
        authentication: Authentication,
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate
    ): ResponseEntity<List<DailyWorkoutSummary>> {
        val userId = authentication.principal as Long
        val response = workoutService.getWorkoutsByDateRange(userId, startDate, endDate)
        return ResponseEntity.ok(response)
    }

    @PostMapping
    @Operation(summary = "운동 기록 추가")
    fun createWorkout(
        authentication: Authentication,
        @Valid @RequestBody request: WorkoutLogRequest
    ): ResponseEntity<WorkoutLogResponse> {
        val userId = authentication.principal as Long
        val response = workoutService.createWorkout(userId, request)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    @Operation(summary = "운동 기록 수정")
    fun updateWorkout(
        authentication: Authentication,
        @PathVariable id: Long,
        @Valid @RequestBody request: WorkoutLogRequest
    ): ResponseEntity<WorkoutLogResponse> {
        val userId = authentication.principal as Long
        val response = workoutService.updateWorkout(userId, id, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "운동 기록 삭제")
    fun deleteWorkout(
        authentication: Authentication,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        val userId = authentication.principal as Long
        workoutService.deleteWorkout(userId, id)
        return ResponseEntity.noContent().build()
    }
}
