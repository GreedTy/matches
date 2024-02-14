package com.example.kpractice

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KPracticeApplicationTests: AbstractProjectConfig() {
    override fun extensions() = listOf(SpringExtension)
}
