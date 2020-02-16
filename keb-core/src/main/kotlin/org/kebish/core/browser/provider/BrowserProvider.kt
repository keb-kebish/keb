package org.kebish.core.browser.provider

import org.kebish.core.Browser

interface BrowserProvider {
    fun provideBrowser(): Browser
}