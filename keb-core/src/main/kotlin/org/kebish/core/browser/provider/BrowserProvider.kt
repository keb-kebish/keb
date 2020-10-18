package org.kebish.core.browser.provider

import org.kebish.core.browser.Browser
import org.kebish.core.config.Configuration

/**
 * Is responsible for creating, providing and QUITING!!! Browser
 */
public interface BrowserProvider {
    /** Called every time when test access browser. */
    public fun provideBrowser(config: Configuration): Browser

    /** This method is called by Keb before each test. */
    public fun beforeTest()

    /** This method is called by Keb after each test. */
    public fun afterTest()
}