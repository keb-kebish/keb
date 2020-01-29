package org.kebish.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ContentTest {

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


}