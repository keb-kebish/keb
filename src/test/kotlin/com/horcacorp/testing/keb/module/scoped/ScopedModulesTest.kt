package com.horcacorp.testing.keb.module.scoped

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScopedModulesTest : KebTest() {


    @Test
    fun `surname can be cleared`() {
        // given
        val pageWithModulesPage = to(::PageWithModulesPage)
        assertThat(pageWithModulesPage.surname.value).isEqualTo("Doe")

        //when
        pageWithModulesPage.surname.clearButton.click()

        //then
        assertThat(pageWithModulesPage.surname.value).isEmpty()
    }


    @Test
    fun `surname can be cleared with page closure`() {
        // given
        to(::PageWithModulesPage) {
            assertThat(surname.value).isEqualTo("Doe")

            //when
            surname.clearButton.click()

            //then
            assertThat(surname.value).isEmpty()

        }
    }

    @Test
    fun `example of keb at function`() {
        // given
        to(::PageWithModulesPage)   //TODO better example would be have to have html with two pages...

        at(::PageWithModulesPage) {
            assertThat(surname.value).isEqualTo("Doe")

            //when
            surname.value = "My new Surname"

            //then
            assertThat(surname.value).isEqualTo("My new Surname")

        }
    }

}



