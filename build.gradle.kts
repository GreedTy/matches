import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)

    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.kotlin.compiler.plugin.allOpen)
    alias(libs.plugins.kotlin.compiler.plugin.noArg)
    alias(libs.plugins.kotlin.compiler.plugin.jpa)
    alias(libs.plugins.kotlin.compiler.plugin.kapt) // https://kotlinlang.org/docs/kapt.html

    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependency)

    `java-test-fixtures`
    jacoco
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

jacoco {
    toolVersion = "0.8.11"
    reportsDirectory = layout.buildDirectory.dir("reports/jacoco")
}

noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.springBoot.starterWeb)
    implementation(libs.springBoot.starterDataJPA)
    implementation(libs.springBoot.starterDataMongodb)
    implementation(libs.springBoot.staterThymeleaf)
    implementation(libs.springBoot.starterLog4j2)
    implementation(libs.springBoot.starterCache)
    implementation(libs.springBoot.starterActuator)
    implementation(libs.ehcache)
    implementation(libs.jackson.module.kotlin)
    kapt(libs.springBoot.configurationProcessor)

    /** kotlin **/
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.coroutines.core)

    developmentOnly(libs.springBoot.devtools)
    runtimeOnly(libs.postgresql)

    testImplementation(libs.springBoot.staterTest) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "org.mockito:mockito-core")
    }

    testImplementation(libs.springBoot.testcontainers)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.mongodb)

    /** kotest **/
    testImplementation(libs.kotest.runnerJunit5)
    testImplementation(libs.kotest.extensionsJVM)
    testImplementation(libs.kotest.assertionsCore)
    testImplementation(libs.kotest.assertionsJson)
    testImplementation(libs.kotest.framework.datatest)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.extensions.spring)
    testImplementation(libs.io.mockk)
//    testImplementation("io.kotest:kotest-assertions-collections:$kotestVersion")
//    testImplementation("io.kotest.extensions:kotest-extensions-mockserver:$kotestVersion")
//    testImplementation("io.kotest.extensions:kotest-extensions-wiremock:$kotestVersion")
//    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:$kotestVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks {
    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }
    // https://docs.gradle.org/current/userguide/jacoco_plugin.html
    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required = false
            csv.required = false
            html.required = true
        }
    }
    jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    minimum = "0.5".toBigDecimal()
                }
            }
            rule {
                isEnabled = false
                element = "CLASS"
                includes = listOf("org.gradle.*")

                limit {
                    counter = "LINE"
                    value = "TOTALCOUNT"
                    maximum = "0.3".toBigDecimal()
                }
            }
        }
    }
}
