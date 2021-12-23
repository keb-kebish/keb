import java.net.URI

//import com.jfrog.bintray.gradle.BintrayExtension
//import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
//import org.gradle.api.tasks.testing.logging.TestLogEvent.*
//import java.util.*

plugins {
    `java-library`
    `maven-publish`
//    id("com.jfrog.bintray")
    signing
}

//TODO TODO TODO "!!!!!!!!!!!!!!!!!
// Write, where is the key stored

//TODO write readme - what has to be set into gradle properties (without passwords)

signing {          //TODO maybe turn off for snapshots (see geb)
    sign(configurations.archives.get())
    // TODO write hint where key and connected stuff is stored
    publishing.publications.configureEach {
        sign(this)
    }
}


fun buildParam(s: String) = project.findProperty(s) as String?

//bintray {
//    user = org.kebish.Bintray.user
//    key = buildParam("bintrayApiKey")
//
//    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
//        repo = org.kebish.Bintray.repo
//        name = project.name
//        // userOrg = "Kebish"
//        setLicenses(*org.kebish.Bintray.licenses)
//        vcsUrl = org.kebish.Bintray.vcsUrl
//        websiteUrl = org.kebish.Bintray.websiteUrl
//        description = org.kebish.Bintray.description
//        desc = description
//        setLabels(*org.kebish.Bintray.labels)
//
//        version(delegateClosureOf<BintrayExtension.VersionConfig> {
//            name = project.version.toString()
//            desc = org.kebish.Bintray.version.desc
//            released = Date().toString()
//            vcsTag = project.version.toString()
//        })
//        setPublications("keb-publish-artifact")
//        publish = true
//    })
//
//}
//
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
                    url.set("https://github.comhttps://github.com/keb-kebish/keb.git/keb-kebish/keb")
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
