package org.kebish.core

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ContentCacheTest {

    @Nested
    inner class NotCachedContentTest : ContentBaseTest() {

        // given
        private var callCounter = 0
        private val variable by content { callCounter++ }

        @Test
        fun `initializer is called on each access to variable`() {
            // when - get value of variable three times
            variable + variable + variable

            // then
            Assertions.assertThat(callCounter).isEqualTo(3)
        }
    }

    @Nested
    inner class CachedContentTest : ContentBaseTest() {

        // given
        private var callCounter = 0
        private val variable by content(cache = true) { callCounter++ }

        @Test
        fun `for cached content is initializer called only once`() {
            // when - get value of variable three times
            variable + variable + variable

            // then
            Assertions.assertThat(callCounter).isEqualTo(1)
        }
    }

    @Nested
    inner class NullCachedContentTest : ContentBaseTest() {

        // given
        var callCounter = 0
        val variable by content(cache = true, required = false) { callCounter++ ; mayReturnNull() }

        @Test
        fun `cached content returning null is called only once`() {
            // when - get value of variable three times
            variable + variable + variable

            // then
            Assertions.assertThat(callCounter).isEqualTo(1)
        }

        private fun mayReturnNull(): String? = null
    }
}