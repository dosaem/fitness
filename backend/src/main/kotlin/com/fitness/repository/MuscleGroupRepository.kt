package com.fitness.repository

import com.fitness.entity.BodyPart
import com.fitness.entity.MuscleGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MuscleGroupRepository : JpaRepository<MuscleGroup, Long> {
    fun findByBodyPart(bodyPart: BodyPart): List<MuscleGroup>
    fun findByName(name: String): MuscleGroup?
}
