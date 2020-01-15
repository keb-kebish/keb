
allprojects {
    group = "org.kebish"
    version = "0.1-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    apply(plugin = "maven-publish")


}
