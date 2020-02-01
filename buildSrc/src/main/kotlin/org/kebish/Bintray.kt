package org.kebish

object Bintray {
    const val user = "vondrous"

    const val repo = "kebish"
    val licenses = arrayOf("WTFPL")
    const val vcsUrl = "https://gitlab.com/horca23/keb.git"
    const val websiteUrl = "https://gitlab.com/horca23/keb"
    const val description = "Library for browser tests implementing Page Object pattern and using Selenium"
    val labels = arrayOf("kotlin", "selenium", "pageobject")

    val version = Version()

    class Version {
        val desc = "Kebish - Kotlin Page Object pattern implementation"
    }
}