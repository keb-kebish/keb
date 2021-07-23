plugins {
    id("kebbuild.keb-published-kotlin-module")
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
    implementation(project(":keb-core"))
}