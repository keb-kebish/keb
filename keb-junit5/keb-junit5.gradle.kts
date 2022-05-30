plugins {
    id("kebbuild.keb-published-kotlin-module")
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation(project(":keb-core"))
}