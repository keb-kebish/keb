
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    kotlin("jvm") version "1.3.61"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

java {
    withSourcesJar()
    //withJavadocJar()
}


tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"                                
    }
}

// jUnit setup --------- start
dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.5.2")
}
val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = FULL
        events = setOf(PASSED, STARTED, FAILED, SKIPPED)
    }
}
// jUnit setup --------- end

dependencies {
    implementation(project(":keb-core"))

}