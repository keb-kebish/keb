plugins {
    // add task "dependencyUpdates" - which writes possible updates of all dependencies
    id("com.github.ben-manes.versions") version "0.28.0"
}

allprojects {
    group = "org.kebish"
    version = "0.4"
    description = "Library for browser tests implementing Page Object pattern and using Selenium"

    repositories {
        jcenter()
    }

}
