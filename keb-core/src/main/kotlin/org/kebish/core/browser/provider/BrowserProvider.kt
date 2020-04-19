package org.kebish.core.browser.provider

import org.kebish.core.Browser
import org.kebish.core.Configuration

/**
 * Is responsible for creating, providing and QUITING!!! Browser
 */
interface BrowserProvider {
    /** Called every time when test access browser. */
    fun provideBrowser(config: Configuration): Browser

    /** This method is called by Keb before each test. */
    fun beforeTest()

    /** This method is called by Keb after each test. */
    fun afterTest()
}