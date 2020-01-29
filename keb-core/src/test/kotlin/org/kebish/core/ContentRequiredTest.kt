package org.kebish.core

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.openqa.selenium.NoSuchElementException

class ContentRequiredTest {

    @Test
    fun `content can be null`() {
        // given
        val variable by content(required = false) { mayReturnNull() }

        // when - get value of variable three times
        @Suppress("UNUSED_VARIABLE")
        val helper = variable

        // then -
        assertThat(helper).isNull()
    }

    @Test
    fun `for required content is thrown an exception when null content provided`() {
        // given
        val variable by content { mayReturnNull() }

        // when - get value of variable three times
        val exception = catchThrowable { variable }

        // then -
        assertThat(exception)
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessageContaining("'null'")
    }

    private fun mayReturnNull(): String? = null

    @Test
    fun `for required content is thrown an exception when empty content provided`() {
        // given
        val empty by content(required = true) { EmptyWebElement("") }

        // when
        val result = catchThrowable { empty }

        // then
        assertThat(result).isInstanceOf(NoSuchElementException::class.java)
    }

    @Test
    fun `for not-required content exception is not thrown when empty content provided`() {
        // given
        val empty by content(required = false) { EmptyWebElement("") }

        // when
        val result = empty

        // then
        assertThat(result).isInstanceOf(EmptyWebElement::class.java)
    }


}