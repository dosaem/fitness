package com.fitness.repository

import com.fitness.entity.Equipment
import com.fitness.entity.EquipmentCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EquipmentRepository : JpaRepository<Equipment, Long> {
    fun findByCategory(category: EquipmentCategory): List<Equipment>
    fun findByName(name: String): Equipment?
}
