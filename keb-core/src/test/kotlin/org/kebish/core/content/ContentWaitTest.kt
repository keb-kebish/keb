package org.kebish.core.content

import com.nhaarman.mockito_kotlin.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.kebish.core.WaitPreset
import org.kebish.core.config.Configuration

class ContentWaitTest {

    
    @Nested
    inner class WaitingTurnedOffTest : ContentBaseTest() {

        // given
        private val variable by content(wait = false) { 123 }

        @Test
        fun `content is not waiting when waiting is turned off`() {
            // when
            variable

            // then
            verify(browser, never()).waitFor(any<WaitPreset>(), anyOrNull(), any<() -> Any>())
        }

    }

    @Nested
    inner class WaitingTurnedOnTest : ContentBaseTest() {

        private val defaultPreset = WaitPreset(15, 0.5)
        private val quickPreset = WaitPreset(1, 0.1)
        private val config = mock<Configuration> {
            on { getDefaultPreset() } doReturn defaultPreset
            on { getWaitPreset("quick") } doReturn quickPreset
        }

        @BeforeEach
        fun init() {
            given(browser.config).willReturn(config)
        }
        
        // given
        private val boolean by content(wait = true) { 123 }
        private val string by content(wait = "quick") { 123 }
        private val number by content(wait = 5) { 123 }
        private val preset by content(wait = WaitPreset(5, 1)) { 123 }
        private val timeoutAndRetryInterval by content(waitTimeout = 100, waitRetryInterval = 10) { 123 }

        @Test
        fun `content is waiting by the default wait preset when turned on by boolean`() {
            // when
            boolean

            // then
            verifyWaitForInvocation(15, 0.5)
        }

        @Test
        fun `content is waiting by the given preset name`() {
            // when
            string

            // then
            verifyWaitForInvocation(1, 0.1)
        }
        
        @Test
        fun `content is waiting for the given timeout + default retry interval`() {
            // when
            number
            
            // then
            verifyWaitForInvocation(5, 0.5)
        }
        
        @Test
        fun `content is waiting by the custom wait preset`() {
            // when
            preset
            
            // then
            verifyWaitForInvocation(5, 1)
        }

        @Test
        fun `content is waiting by the specified timeout and retry interval`() {
            // when
            timeoutAndRetryInterval

            // then
            verifyWaitForInvocation(100, 10)
        }
        
        private fun verifyWaitForInvocation(expectedTimeout: Number, expectedRetryInterval: Number) {
            verify(browser).waitFor(
                argThat<WaitPreset> { timeout == expectedTimeout && retryInterval == expectedRetryInterval },
                anyOrNull(),
                anyOrNull<() -> Any>()
            )
        }
    }

}