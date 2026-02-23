package com.fitness.controller

import com.fitness.dto.*
import com.fitness.entity.Difficulty
import com.fitness.entity.ExerciseCategory
import com.fitness.service.ExerciseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/exercises")
@Tag(name = "Exercises", description = "운동 API")
class ExerciseController(
    private val exerciseService: ExerciseService
) {

    @GetMapping
    @Operation(summary = "운동 목록 조회")
    fun getExercises(
        @RequestParam(required = false) keyword: String?,
        @RequestParam(required = false) category: ExerciseCategory?,
        @RequestParam(required = false) difficulty: Difficulty?,
        @RequestParam(required = false) muscleGroupId: Long?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<ExerciseListResponse>> {
        val request = ExerciseSearchRequest(
            keyword = keyword,
            category = category,
            difficulty = difficulty,
            muscleGroupId = muscleGroupId,
            page = page,
            size = size
        )
        val response = exerciseService.getExercises(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    @Operation(summary = "운동 상세 조회")
    fun getExercise(@PathVariable id: Long): ResponseEntity<ExerciseResponse> {
        val response = exerciseService.getExercise(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/search")
    @Operation(summary = "운동 검색")
    fun searchExercises(
        @RequestParam keyword: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<ExerciseListResponse>> {
        val response = exerciseService.searchExercises(keyword, page, size)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/muscle-groups")
    @Operation(summary = "근육 그룹 목록 조회")
    fun getMuscleGroups(): ResponseEntity<List<MuscleGroupResponse>> {
        val response = exerciseService.getMuscleGroups()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/equipment")
    @Operation(summary = "운동 장비 목록 조회")
    fun getEquipment(): ResponseEntity<List<EquipmentResponse>> {
        val response = exerciseService.getEquipment()
        return ResponseEntity.ok(response)
    }
}
