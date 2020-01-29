package org.kebish.core

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.openqa.selenium.NoSuchElementException

class ContentBasicTest {

    @Test
    fun `simple case works`() {
        // when
        val variable by content { "AAA" }

        // then
        assertThat(variable).isEqualTo("AAA")
    }

    @Test
    fun `initializer is called in right time`() {
        // given
        var callCounter = 0

        // when
        val variable by content { callCounter++ }

        //then - not yeat called
        assertThat(callCounter).isEqualTo(0)

        // when - call variable
        @Suppress("UNUSED_VARIABLE")
        val helper = variable

        //then - closure is called
        assertThat(callCounter).isEqualTo(1)
    }


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