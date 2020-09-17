package org.kebish.core.content

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ContentBasicTest {

    @Nested
    inner class SimpleContentTest : ContentBaseTest() {

        // when
        private val variable by content { "AAA" }

        @Test
        fun `simple case works`() {
            // then
            assertThat(variable).isEqualTo("AAA")
        }
    }

    @Nested
    inner class ContentInitializationTest : ContentBaseTest() {

        // given
        private var callCounter = 0

        // when
        private val variable by content { callCounter++ }

        @Test
        fun `initializer is called in right time`() {
            // then - not yet called
            assertThat(callCounter).isEqualTo(0)
            // when - call variable
            variable
            // then - content is initialized
            assertThat(callCounter).isEqualTo(1)
        }
    }
}