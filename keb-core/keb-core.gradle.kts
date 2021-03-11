
import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.kebish.Bintray
import java.util.*

plugins {
    id("kebbuild.keb-published-kotlin-module")
}

dependencies {
    api(group = "org.seleniumhq.selenium", name = "selenium-java", version = "3.141.59")

    testImplementation(project(":keb-junit5"))
    testImplementation(group = "io.github.bonigarcia", name = "webdrivermanager", version = "4.2.0")
    testImplementation(group = "io.ktor", name = "ktor-server-netty", version = "1.4.0")
    testImplementation(group = "io.ktor", name = "ktor-html-builder", version = "1.4.0")
}
