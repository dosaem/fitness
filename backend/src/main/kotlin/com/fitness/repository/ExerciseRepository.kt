package com.fitness.repository

import com.fitness.entity.Exercise
import com.fitness.entity.ExerciseCategory
import com.fitness.entity.Difficulty
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ExerciseRepository : JpaRepository<Exercise, Long> {

    fun findByCategory(category: ExerciseCategory, pageable: Pageable): Page<Exercise>

    fun findByDifficulty(difficulty: Difficulty, pageable: Pageable): Page<Exercise>

    fun findByCategoryAndDifficulty(
        category: ExerciseCategory,
        difficulty: Difficulty,
        pageable: Pageable
    ): Page<Exercise>

    @Query("""
        SELECT e FROM Exercise e
        WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(e.nameKo) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    fun search(@Param("keyword") keyword: String, pageable: Pageable): Page<Exercise>

    @Query("""
        SELECT DISTINCT e FROM Exercise e
        LEFT JOIN e.muscleGroups mg
        WHERE (:category IS NULL OR e.category = :category)
          AND (:difficulty IS NULL OR e.difficulty = :difficulty)
          AND (:muscleGroupId IS NULL OR mg.id = :muscleGroupId)
    """)
    fun findByFilters(
        @Param("category") category: ExerciseCategory?,
        @Param("difficulty") difficulty: Difficulty?,
        @Param("muscleGroupId") muscleGroupId: Long?,
        pageable: Pageable
    ): Page<Exercise>
}
