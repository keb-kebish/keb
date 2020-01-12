plugins {
    kotlin("jvm") version "1.3.61"
}

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    implementation(project(":keb-core"))

    implementation(kotlin("stdlib-jdk8"))

    implementation("org.junit.jupiter:junit-jupiter:5.5.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}