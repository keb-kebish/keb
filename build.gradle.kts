plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.61"
    `java-library`
}

group = "com.horca.testing"
version = "0.1 Alpha"

repositories {
    mavenCentral()
}


dependencies {
    api(group = "org.seleniumhq.selenium", name = "selenium-java", version = "3.141.59")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation(group = "io.github.bonigarcia", name = "webdrivermanager", version = "3.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")

}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}