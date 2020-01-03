plugins {
    kotlin("jvm") version "1.3.61"
}

group = "com.horca.testing"
version = "0.1 Alpha"

repositories {
    mavenCentral()
}

dependencies {
    api(group = "org.seleniumhq.selenium", name = "selenium-java", version = "3.141.59")

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(project(":keb-junit5"))
    testImplementation(group = "io.github.bonigarcia", name = "webdrivermanager", version = "3.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.1")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation(group = "io.vertx", name = "vertx-web", version = "3.8.4")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")
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
}