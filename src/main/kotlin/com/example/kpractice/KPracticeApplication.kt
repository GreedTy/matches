package com.example.kpractice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KpracticeApplication

fun main(args: Array<String>) {
    runApplication<KpracticeApplication>(*args)
}

class Test : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        logger.debug("hello: {}", "matchers!!!")
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
