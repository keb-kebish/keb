package org.kebish.junit5

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.RegisterExtension
import org.kebish.core.config.Configuration
import org.kebish.core.config.TestInfo
import org.kebish.core.test.KebTestBase

abstract class KebTest(config: Configuration) : KebTestBase(config) {

    @RegisterExtension
    @JvmField
    val kebAfterTest = KebAfterTest(this)

    @BeforeEach
    fun beforeEachTestJunit5() {
        super.beforeEachTest()
    }

}

class KebAfterTest(val kebTest: KebTest) : AfterTestExecutionCallback {

    override fun afterTestExecution(context: ExtensionContext) {
        val failed = context.executionException.isPresent

        val reportPath = constructReportPath(context)
        val testInfo = TestInfo(reportPath)
        if (failed) {
            kebTest.afterTestFail(testInfo)
        } else {
            kebTest.afterTestSuccess(testInfo)
        }
        kebTest.afterTest(testInfo)

        kebTest.finalizeTest()
    }

    private fun constructReportPath(context: ExtensionContext): String {
        val testClass = context.testClass.get()
        val packageDirs = testClass.`package`.name.replace('.', '/')
        val className = testClass.simpleName
        val methodName = context.testMethod.get().name

        // This is not strictly unique (only package, className and methodName) - but well readable
        // method parameters can be obtained like this context.testMethod.get().parameters[0].type.canonicalName
        return "$packageDirs/$className.$methodName()"
    }
}
