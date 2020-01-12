package keb.junit5

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.test.KebTestBase
import org.junit.jupiter.api.AfterEach

 abstract class KebTest(browser: Browser) : KebTestBase(browser) {

    @AfterEach
    fun afterEachTestJunit5() {
        super.afterEachTest()
    }
}