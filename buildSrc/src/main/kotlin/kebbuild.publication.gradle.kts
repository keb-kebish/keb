import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import java.util.*

plugins {
    `java-library`
    `maven-publish`
    id("com.jfrog.bintray")
}

fun buildParam(s: String) = project.findProperty(s) as String?

bintray {
    user = org.kebish.Bintray.user
    key = buildParam("bintrayApiKey")

    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = org.kebish.Bintray.repo
        name = project.name
        // userOrg = "Kebish"
        setLicenses(*org.kebish.Bintray.licenses)
        vcsUrl = org.kebish.Bintray.vcsUrl
        websiteUrl = org.kebish.Bintray.websiteUrl
        description = org.kebish.Bintray.description
        desc = description
        setLabels(*org.kebish.Bintray.labels)

        version(delegateClosureOf<BintrayExtension.VersionConfig> {
            name = project.version.toString()
            desc = org.kebish.Bintray.version.desc
            released = Date().toString()
            vcsTag = project.version.toString()
        })
        setPublications("keb-publish-artifact")
        publish = true
    })

}

publishing {
    publications {
        create<MavenPublication>("keb-publish-artifact") {
            from(components["java"])
        }
    }
}


java {
    withSourcesJar()
    //withJavadocJar()
}
