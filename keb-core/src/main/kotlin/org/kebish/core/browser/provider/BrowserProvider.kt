package org.kebish.core.browser.provider

import org.kebish.core.Browser

/**
 * Is responsible for creating, providing and QUITING!!! Browser
 */
interface BrowserProvider {
    /** Called every time when test access browser. */
    fun provideBrowser(): Browser

    /** This method is called by Keb before each test. */
    fun beforeTest()

    /** This method is called by Keb after each test. */
    fun afterTest()
}