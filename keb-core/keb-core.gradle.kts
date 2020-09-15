
import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.kebish.Bintray
import java.util.*

fun buildParam(s: String) = project.findProperty(s) as String?

plugins {
    kotlin("jvm") version "1.4.10"

    // Publish plugins - start
    `java-library`
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.4"
    // Publish plugins - end
}

// Publish - start
bintray {
    user = Bintray.user
    key = buildParam("bintrayApiKey")

    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = Bintray.repo
        name = "keb-core"
        // userOrg = "Kebish"
        setLicenses(*Bintray.licenses)
        vcsUrl = Bintray.vcsUrl
        websiteUrl = Bintray.websiteUrl
        description = Bintray.description
        desc = description
        setLabels(*Bintray.labels)

        version(delegateClosureOf<BintrayExtension.VersionConfig> {
            name = project.version.toString()
            desc = Bintray.version.desc
            released = Date().toString()
            vcsTag = project.version.toString()
        })
        setPublications("keb-publish-artifact")
        publish = true
    })

}

publishing {
    publications {
        create<MavenPublication>("keb-publish-artifact") {
            from(components["java"])
        }
    }
}
// Publish - end

repositories {
    mavenCentral()
    jcenter()
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(kotlin("stdlib-jdk8"))

}

java {
    withSourcesJar()
    //withJavadocJar() - no correct javadoc is generated needs to be setup for Kotlin
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = FULL
        events = setOf(PASSED, STARTED, FAILED, SKIPPED)
    }
}

dependencies {
    api(group = "org.seleniumhq.selenium", name = "selenium-java", version = "3.141.59")

    testImplementation(project(":keb-junit5"))
    testImplementation(group = "io.github.bonigarcia", name = "webdrivermanager", version = "4.2.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation("org.assertj:assertj-core:3.17.2")
    testImplementation(group = "io.ktor", name = "ktor-server-netty", version = "1.4.0")
    testImplementation(group = "io.ktor", name = "ktor-html-builder", version = "1.4.0")
    testImplementation(group = "com.nhaarman", name = "mockito-kotlin", version = "1.6.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}
