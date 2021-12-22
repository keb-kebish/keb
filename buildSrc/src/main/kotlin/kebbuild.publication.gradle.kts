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

signing {          //TODO maybe turn off for snapshots (see geb)
    sign(configurations.archives.get())
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
    publications {
        create<MavenPublication>("keb-publish-artifact") {
            from(components["java"])
            pom {
                //name.set("kebish") // TODO  check if it is set by module name
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
                    url.set("https://github.com/keb-kebish/keb")
                }
                developers {
                    developer {
                        id.set("bugs84")
                        name.set("Jan Vondrouš")
                    }
                    developer {
                        id.set("horca")
                        name.set("Michal Horčičko")
                    }
                }
            }
        }
    }
}


java {
    withSourcesJar()
    //withJavadocJar()
}
