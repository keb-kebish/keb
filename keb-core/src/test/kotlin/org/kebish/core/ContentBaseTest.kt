package org.kebish.core

import com.nhaarman.mockito_kotlin.mock

abstract class ContentBaseTest : ContentSupport {

    override val browser: Browser
        get() = Browser(mock())

}