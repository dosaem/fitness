package com.fitness.service

import com.fitness.dto.*
import com.fitness.entity.Exercise
import com.fitness.repository.ExerciseRepository
import com.fitness.repository.MuscleGroupRepository
import com.fitness.repository.EquipmentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExerciseService(
    private val exerciseRepository: ExerciseRepository,
    private val muscleGroupRepository: MuscleGroupRepository,
    private val equipmentRepository: EquipmentRepository
) {

    @Transactional(readOnly = true)
    fun getExercises(request: ExerciseSearchRequest): Page<ExerciseListResponse> {
        val pageable = PageRequest.of(request.page, request.size, Sort.by("nameKo"))

        val exercises = if (request.keyword != null) {
            exerciseRepository.search(request.keyword, pageable)
        } else {
            exerciseRepository.findByFilters(
                category = request.category,
                difficulty = request.difficulty,
                muscleGroupId = request.muscleGroupId,
                pageable = pageable
            )
        }

        return exercises.map { it.toListResponse() }
    }

    @Transactional(readOnly = true)
    fun getExercise(id: Long): ExerciseResponse {
        val exercise = exerciseRepository.findById(id)
            .orElseThrow { IllegalArgumentException("운동을 찾을 수 없습니다") }
        return exercise.toResponse()
    }

    @Transactional(readOnly = true)
    fun searchExercises(keyword: String, page: Int, size: Int): Page<ExerciseListResponse> {
        val pageable = PageRequest.of(page, size)
        return exerciseRepository.search(keyword, pageable).map { it.toListResponse() }
    }

    @Transactional(readOnly = true)
    fun getMuscleGroups(): List<MuscleGroupResponse> {
        return muscleGroupRepository.findAll().map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun getEquipment(): List<EquipmentResponse> {
        return equipmentRepository.findAll().map { it.toResponse() }
    }

    private fun Exercise.toListResponse() = ExerciseListResponse(
        id = id,
        name = name,
        nameKo = nameKo,
        category = category,
        difficulty = difficulty,
        primaryImageUrl = primaryImageUrl,
        muscleGroups = muscleGroups.map { it.nameKo }
    )

    private fun Exercise.toResponse() = ExerciseResponse(
        id = id,
        name = name,
        nameKo = nameKo,
        description = description,
        instructions = instructions,
        category = category,
        difficulty = difficulty,
        primaryImageUrl = primaryImageUrl,
        videoUrl = videoUrl,
        caloriesPerMinute = caloriesPerMinute?.toDouble(),
        muscleGroups = muscleGroups.map { it.toResponse() },
        equipment = equipment.map { it.toResponse() }
    )

    private fun com.fitness.entity.MuscleGroup.toResponse() = MuscleGroupResponse(
        id = id,
        name = name,
        nameKo = nameKo,
        bodyPart = bodyPart
    )

    private fun com.fitness.entity.Equipment.toResponse() = EquipmentResponse(
        id = id,
        name = name,
        nameKo = nameKo,
        category = category
    )
}
