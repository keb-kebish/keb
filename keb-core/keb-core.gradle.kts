plugins {
    id("kebbuild.keb-published-kotlin-module")
}

dependencies {
    api(group = "org.seleniumhq.selenium", name = "selenium-java", version = "4.2.0")

    testImplementation(project(":keb-junit5"))
    testImplementation(group = "io.github.bonigarcia", name = "webdrivermanager", version = "5.2.0")
    testImplementation(group = "io.ktor", name = "ktor-server-netty", version = "1.5.4")
    testImplementation(group = "io.ktor", name = "ktor-html-builder", version = "1.5.4")
}
