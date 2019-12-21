package com.horcacorp.testing.keb.module.scoped

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScopedModulesTest : KebTest() {


    @Test
    fun `surname can be cleared`() {
        // given
        val pageWithModulesPage = to(::PageWithModulesPage)
        assertThat(pageWithModulesPage.surname.textInput.getAttribute("value")).isEqualTo("Doe")

        //when
        pageWithModulesPage.surname.clearButton.click()

        //then
        assertThat(pageWithModulesPage.surname.textInput.getAttribute("value")).isEmpty()
    }

}



