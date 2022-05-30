plugins {
    id("kebbuild.keb-published-kotlin-module")
}

dependencies {
    api(group = "org.seleniumhq.selenium", name = "selenium-java", version = "3.141.59")

    testImplementation(project(":keb-junit5"))
    testImplementation(group = "io.github.bonigarcia", name = "webdrivermanager", version = "5.0.1")
    testImplementation(group = "io.ktor", name = "ktor-server-netty", version = "1.5.4")
    testImplementation(group = "io.ktor", name = "ktor-html-builder", version = "1.5.4")
}
