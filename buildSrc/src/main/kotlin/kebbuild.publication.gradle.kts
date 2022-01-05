import java.net.URI

plugins {
    `java-library`
    `maven-publish`
    signing
}

// Link to instructions about private/public key are written in HOWTO.md
signing {          //TODO maybe turn off for snapshots (see geb)
    sign(configurations.archives.get())
    publishing.publications.configureEach {
        sign(this)
    }
}


fun buildParam(s: String) = project.findProperty(s) as String?

publishing {
    repositories {
        maven {
            name = "OSSRH-Release"
            url = URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.properties["ossrhUsername"].toString()
                password = project.properties["ossrhPassword"].toString()
            }
        }
//        maven {
//            name = "OSSRH-Snapshot"
//            url = URI("https://s01.oss.sonatype.org/content/repositories/snapshots/")
//            credentials {
//                username = project.properties["ossrhUsername"].toString()
//                password = project.properties["ossrhPassword"].toString()
////                username = System.getenv("MAVEN_USERNAME")
////                password = System.getenv("MAVEN_PASSWORD")
//            }
//        }
    }

    publications {
        create<MavenPublication>("keb-publish-artifact") {
            from(components["java"])
            pom {
                name.set("Keb - Kebish")
                url.set("http://kebish.org/")
                description.set("Keb is a https://gebish.org inspired Selenium wrapper written in Kotlin that allows you to modularize pages of your web application into logic units represented by Kotlin classes.")
                inceptionYear.set("2018")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }
                scm {
                    url.set("https://github.com/keb-kebish/keb.git")
                }
                developers {
                    developer {
//                        id.set("bugs84")
                        name.set("Jan Vondrouš")
                    }
                    developer {
//                        id.set("horca")
                        name.set("Michal Horčičko")
                    }
                }
            }
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}
