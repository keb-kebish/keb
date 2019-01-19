import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.11"
}

group = "com.horca.testing"
version = "0.1 Alpha"

repositories {
    mavenCentral()
}


dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(group = "org.seleniumhq.selenium", name = "selenium-java", version = "3.141.59")
    testCompile(group = "io.github.bonigarcia", name = "webdrivermanager", version = "3.1.1")
    testCompile(group = "junit", name = "junit", version = "4.12")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.test {
    useJUnit()
}