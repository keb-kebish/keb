package org.kebish.core

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.openqa.selenium.NoSuchElementException

class ContentRequiredTest {

    @Nested
    inner class NotRequiredNullContentTest : ContentBaseTest() {

        // given
        private val variable by content(required = false) { mayReturnNull() }

        @Test
        fun `content can be null`() {
            // when - get value of variable three times
            val helper = variable

            // then
            assertThat(helper).isNull()
        }
    }

    @Nested
    inner class RequiredNullContentTest : ContentBaseTest() {

        // given
        private val variable by content { mayReturnNull() }

        @Test
        fun `for required content is thrown an exception when null content provided`() {
            // when - get value of variable three times
            val exception = catchThrowable { variable }

            // then -
            assertThat(exception)
                .isInstanceOf(NoSuchElementException::class.java)
                .hasMessageContaining("'null'")
        }
    }

    @Nested
    inner class NotRequiredEmptyContentTest : ContentBaseTest() {

        // given
        private val empty by content(required = false) { EmptyWebElement("") }

        @Test
        fun `for not required content exception is not thrown when empty content provided`() {
            // when
            val result = empty

            // then
            assertThat(result).isInstanceOf(EmptyWebElement::class.java)
        }
    }

    @Nested
    inner class RequiredEmptyContentTest : ContentBaseTest() {

        // given
        private val empty by content(required = true) { EmptyWebElement("") }

        @Test
        fun `for required content is thrown an exception when empty content provided`() {
            // when
            val result = catchThrowable { empty }

            // then
            assertThat(result).isInstanceOf(NoSuchElementException::class.java)
        }
    }

    private fun mayReturnNull(): String? = null
}