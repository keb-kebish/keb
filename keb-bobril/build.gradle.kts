
import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import java.util.*

fun buildParam(s: String) = project.findProperty(s) as String?

plugins {
    kotlin("jvm") version "1.3.61"

    // Publish plugins - start
    `java-library`
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.4"
    // Publish plugins - end
}

// Publish - start
bintray {
    user = org.kebish.Bintray.user
    key = buildParam("bintrayApiKey")

    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = org.kebish.Bintray.repo
        name = "keb-bobril"
        // userOrg = "Kebish"
        setLicenses(*org.kebish.Bintray.licenses)
        vcsUrl = org.kebish.Bintray.vcsUrl
        websiteUrl = org.kebish.Bintray.websiteUrl
        description = org.kebish.Bintray.description
        desc = description
        setLabels(*org.kebish.Bintray.labels)

        version(delegateClosureOf<BintrayExtension.VersionConfig> {
            name = project.version.toString()
            desc = org.kebish.Bintray.version.desc
            released = Date().toString()
            vcsTag = project.version.toString()
        })
        setPublications("keb-publish-artifact")
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