package com.fitness.dto

import com.fitness.entity.*

data class ExerciseResponse(
    val id: Long,
    val name: String,
    val nameKo: String,
    val description: String?,
    val instructions: String?,
    val category: ExerciseCategory,
    val difficulty: Difficulty,
    val primaryImageUrl: String?,
    val videoUrl: String?,
    val caloriesPerMinute: Double?,
    val muscleGroups: List<MuscleGroupResponse>,
    val equipment: List<EquipmentResponse>
)

data class ExerciseListResponse(
    val id: Long,
    val name: String,
    val nameKo: String,
    val category: ExerciseCategory,
    val difficulty: Difficulty,
    val primaryImageUrl: String?,
    val muscleGroups: List<String>
)

data class MuscleGroupResponse(
    val id: Long,
    val name: String,
    val nameKo: String,
    val bodyPart: BodyPart
)

data class EquipmentResponse(
    val id: Long,
    val name: String,
    val nameKo: String,
    val category: EquipmentCategory
)

data class ExerciseSearchRequest(
    val keyword: String? = null,
    val category: ExerciseCategory? = null,
    val difficulty: Difficulty? = null,
    val muscleGroupId: Long? = null,
    val page: Int = 0,
    val size: Int = 20
)
