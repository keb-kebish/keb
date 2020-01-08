package com.horcacorp.testing.keb.core

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.openqa.selenium.Point
import org.openqa.selenium.WebElement

class WaitSupportTest {

    private val objectUnderTest = object : WaitSupport {
        override val browser: Browser = mock()
    }

    @Nested
    inner class TruthinessTest {

        @Nested
        inner class NumberTruthinessTest {

            @Test
            fun `waitFor returns number value when such value is non-zero integer number`() {
                // given
                val number = 1

                // when
                val result = objectUnderTest.waitFor(1, 1) { number }

                // then
                assertThat(result).isEqualTo(number)
            }

            @Test
            fun `waitFor returns number value when such value is a non-zero real number`() {
                // given
                val number = 1.23

                // when
                val result = objectUnderTest.waitFor(1, 1) { number }

                // then
                assertThat(result).isEqualTo(number)
            }

            @Test
            fun `waitFor throws an exception when return value is zero`() {
                // given
                val number = 0

                // when
                val result = catchThrowable { objectUnderTest.waitFor(1, 1) { number } }

                // then
                assertThat(result).isInstanceOf(WaitTimeoutException::class.java)
            }
        }

        @Nested
        inner class StringTruthinessTest {

            @Test
            fun `waitFor returns string value when such value is at least one character long`() {
                // given
                val string = "abc"

                // when
                val result = objectUnderTest.waitFor(1, 1) { string }

                // then
                assertThat(result).isEqualTo(string)
            }

            @Test
            fun `waitFor throws an exception when return value is empty string`() {
                // given
                val string = ""

                // when
                val result = catchThrowable { objectUnderTest.waitFor(1, 1) { string } }

                // then
                assertThat(result).isInstanceOf(WaitTimeoutException::class.java)
            }
        }

        @Nested
        inner class BooleanTruthinessTest {

            @Test
            fun `waitFor returns boolean when such boolean is true`() {
                // given
                val bool = true

                // when
                val result = objectUnderTest.waitFor(1, 1) { bool }

                // then
                assertThat(result).isEqualTo(bool)
            }

            @Test
            fun `waitFor throws an exception when return value is false`() {
                // given
                val bool = false

                // when
                val result = catchThrowable { objectUnderTest.waitFor(1, 1) { bool } }

                // then
                assertThat(result).isInstanceOf(WaitTimeoutException::class.java)
            }
        }

        @Nested
        inner class CollectionTruthinessTest {

            @Test
            fun `waitFor returns collection when all collection elements recursively are resolved as truthy`() {
                // given
                val collection = listOf(listOf(true, true), listOf(true))

                // when
                val result = objectUnderTest.waitFor(1, 1) { collection }

                // then
                assertThat(result).isEqualTo(collection)
            }

            @Test
            fun `waitFor throws an exception when any collection element is not resolved as truthy`() {
                // given
                val collection = listOf(true, false)

                // when
                val result = catchThrowable { objectUnderTest.waitFor(1, 1) { collection } }

                // then
                assertThat(result).isInstanceOf(WaitTimeoutException::class.java)
            }

            @Test
            fun `waitFor throws an exception when any collection is empty`() {
                // given
                val collection = emptyList<Boolean>()

                // when
                val result = catchThrowable { objectUnderTest.waitFor(1, 1) { collection } }

                // then
                assertThat(result).isInstanceOf(WaitTimeoutException::class.java)
            }
        }

        @Nested
        inner class WebElementTruthinessTest {

            @Test
            fun `waitFor returns WebElement when its location is defined`() {
                // given
                val webElement = mock<WebElement> { on { location } doReturn mock<Point>() }

                // when
                val result = objectUnderTest.waitFor(1, 1) { webElement }

                // then
                assertThat(result).isEqualTo(webElement)
            }

            @Test
            fun `waitFor throws an exception when WebElement's location is not defined`() {
                // given
                val webElement = mock<WebElement> { on { location }.doReturn<Point?>(null) }

                // when
                val result = catchThrowable { objectUnderTest.waitFor(1, 1) { webElement } }

                // then
                assertThat(result).isInstanceOf(WaitTimeoutException::class.java)
            }
        }

        @Nested
        inner class ModuleTruthinessTest {

            private inner class TestModule(scope: WebElement?) : Module(scope)

            @Test
            fun `waitFor returns Module when it has no scope defined`() {
                // given
                val module = TestModule(null)

                // when
                val result = objectUnderTest.waitFor(1, 1) { module }

                // then
                assertThat(result).isEqualTo(module)
            }

            @Test
            fun `waitFor returns Module whose scope location is defined`() {
                // given
                val module = TestModule(mock { on { location } doReturn mock<Point>() })

                // when
                val result = objectUnderTest.waitFor(1, 1) { module }

                // then
                assertThat(result).isEqualTo(module)
            }

            @Test
            fun `waitFor throws an exception when Module's scope location is not defined`() {
                // given
                val module = TestModule(mock { on { location }.doReturn<Point?>(null) })

                // when
                val result = catchThrowable { objectUnderTest.waitFor(1, 1) { module } }

                // then
                assertThat(result).isInstanceOf(WaitTimeoutException::class.java)
            }
        }

        @Nested
        inner class ObjectReferenceTruthinessTest {

            @Test
            fun `waitFor returns object when such object is not a null reference`() {
                // given
                val obj = Any()

                // when
                val result = objectUnderTest.waitFor(1, 1) { obj }

                // then
                assertThat(result).isEqualTo(obj)
            }

            @Test
            fun `waitFor throws an exception when return value is null reference`() {
                // given
                val obj: Any? = null

                // when
                val result = catchThrowable { objectUnderTest.waitFor(1, 1) { obj } }

                // then
                assertThat(result).isInstanceOf(WaitTimeoutException::class.java)
            }
        }
    }
}