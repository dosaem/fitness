package com.fitness

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class FitnessApplication

fun main(args: Array<String>) {
    runApplication<FitnessApplication>(*args)
}
