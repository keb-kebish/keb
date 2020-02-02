import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.kebish.Versions

plugins {
    kotlin("jvm") version "1.3.61"
}

java {
    withSourcesJar()
    //withJavadocJar()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(kotlin("stdlib-jdk8"))

}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = FULL
        events = setOf(PASSED, STARTED, FAILED, SKIPPED)
    }
}

dependencies {
    implementation(project(":keb-core"))
}