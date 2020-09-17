@file:Suppress("UNCHECKED_CAST")

package org.kebish.core.content

import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import org.junit.jupiter.api.BeforeEach
import org.kebish.core.ContentSupport
import org.kebish.core.WaitPreset
import org.kebish.core.browser.Browser

abstract class ContentBaseTest : ContentSupport {

    override val browser: Browser = mock()

    @BeforeEach
    fun prepare() {
        given(browser.waitFor(anyOrNull<WaitPreset>(), anyOrNull(), anyOrNull<() -> Any>()))
            .willAnswer { (it.arguments[2] as () -> Any)() }
        given(browser.config).willReturn(mock())
    }
}