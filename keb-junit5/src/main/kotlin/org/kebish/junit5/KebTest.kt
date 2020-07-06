package org.kebish.junit5

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.RegisterExtension
import org.kebish.core.Configuration
import org.kebish.core.config.TestInfo
import org.kebish.core.test.KebTestBase

abstract class KebTest(config: Configuration) : KebTestBase(config) {

    @RegisterExtension
    @JvmField
    val afterTest = AfterTest(this)

    @BeforeEach
    fun beforeEachTestJunit5() {
        super.beforeEachTest()
    }

    //TODO clean this up
//    @AfterEach
//    fun afterEachTestJunit5() {
//        val i = 0
////        super.finalizeTest()
//    }

}

class AfterTest(val kebTest: KebTest) : AfterTestExecutionCallback {


    //TODO clean this up
//    override fun testFailed(context: ExtensionContext, cause: Throwable) {
//        testFailedCallback(context, cause)
//    }
//
//    override fun testSuccessful(context: ExtensionContext?) {
//        super.testSuccessful(context)
//    }
//
//    override fun testDisabled(context: ExtensionContext?, reason: Optional<String>?) {
//        super.testDisabled(context, reason)
//    }
//
//    override fun testAborted(context: ExtensionContext?, cause: Throwable?) {
//        super.testAborted(context, cause)
//    }

    override fun afterTestExecution(context: ExtensionContext) {
        val failed = context.executionException.isPresent

        if (failed) {
            val testInfo = TestInfo(context.displayName)
            kebTest.afterTestFail(testInfo)
        } else {
            val i = 0
        }

        kebTest.finalizeTest()

    }
}
