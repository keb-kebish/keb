package org.kebish.usage.content

import kotlinx.html.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kebish.core.content.EmptyWebElement
import org.kebish.core.page.Page
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig
import org.kebish.usage.test.util.HtmlContent
import org.kebish.usage.test.util.HttpBuilderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl

/** Look at class EventPage there are shown different kinds of content parameters */
class ContentUsageSampleTest : KebTest(commonTestKebConfig()), Extendable by ExtendableImpl() {

    @Suppress("unused")
    private val serverExtension = register(
        HttpBuilderServerExtension(
            browser,
            HtmlContent {
                head {
                    title = "Event Page"
                }
                body {
                    h1 {
                    id = "EventName"
                    text("Football event")
                }
                p {
                    id = "Countdown"
                    text("20000")
                }


            }
        }
    ))

    @Test
    fun `content is selected each time`() {
        // given
        to(::EventPage) {
            // when
            val first = countdownSelectEachTime
            val second = countdownSelectEachTime

            // then
            assertThat(first).isNotSameAs(second)
        }
    }

    @Test
    fun `content is selected only first time`() {
        // given
        to(::EventPage) {
            // when
            val first = countdownSelectFirstTime
            val second = countdownSelectFirstTime

            // then
            assertThat(first).isSameAs(second)
        }
    }

    @Test
    fun `content which is not present do not throw exception, but returns EmptyWebElement`() {
        // given
        to(::EventPage) {
            // when
            val optional = countdownOptional
            // then
            assertThat(optional).isInstanceOf(EmptyWebElement::class.java)
        }
    }

    @Test
    fun `wait until content appears at page content`() {
        // given
        to(::EventPage) {
            // when
            val wait = countdownWaitFor
            // then
            assertThat(wait.isDisplayed).isEqualTo(true)
        }
    }

    class EventPage : Page() {
        override fun url() = ""
        override fun at() = { eventName }

        val eventName by content { css("#EventName") }

        // Standard content is selected each time you access it
        val countdownSelectEachTime by content { css("#Countdown") }

        // Cached content is selected only first time you access it
        val countdownSelectFirstTime by content(cache = true) { css("#Countdown") }

        // If element is not present on page. This will return "EmptyWebElement" instead of throwing exception
        val countdownOptional by content(required = false) { css("#NonexistentCountdown") }

        // If element is not present on page at the time when you ask for it. This will wait until it appear
        // (see Keb Waiting for more details and more waiting parameters)
        val countdownWaitFor by content(wait = true) { css("#Countdown") }


    }


}