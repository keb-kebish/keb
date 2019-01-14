import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.11"
}

group = "com.horca.testing"
version = "0.1 Alpha"

repositories {
    mavenCentral()
    maven {
        setUrl("https://dl.bintray.com/s1m0nw1/KtsRunner")
    }
}


dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("de.swirtz:ktsRunner:0.0.7")
    compile(group = "org.seleniumhq.selenium", name = "selenium-java", version = "3.141.59")
    compile(group = "io.github.bonigarcia", name = "webdrivermanager", version = "3.1.1")
    compile(group = "junit", name = "junit", version = "4.12")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}