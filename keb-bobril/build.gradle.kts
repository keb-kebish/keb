plugins {
    kotlin("jvm") version "1.3.61"
}


java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":keb-core"))
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