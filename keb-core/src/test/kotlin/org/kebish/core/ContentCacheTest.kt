package org.kebish.core

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ContentCacheTest {

    @Test
    fun `initializer is called on each access to variable`() {
        // given
        var callCounter = 0
        val variable by content { callCounter++ }

        // when - get value of variable three times
        @Suppress("UNUSED_VARIABLE")
        val helper = variable + variable + variable

        // then -
        Assertions.assertThat(callCounter).isEqualTo(3)
    }

    @Test
    fun `for cached content is initializer called only once`() {
        // given
        var callCounter = 0
        val variable by content(cache = true) { callCounter++ }

        // when - get value of variable three times
        @Suppress("UNUSED_VARIABLE")
        val helper = variable + variable + variable

        // then -
        Assertions.assertThat(callCounter).isEqualTo(1)
    }
}