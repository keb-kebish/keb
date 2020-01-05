package keb.test.util.extendable

import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle

/**
 * Be Aware! Implementing this Interface make the test @TestInstance(Lifecycle.PER_CLASS)
 * It means one test instance is used to run all tests!
 */
@TestInstance(Lifecycle.PER_CLASS)
interface Extendable {

    val extensions: MutableList<Extension>

    fun <T : Extension> register(extension: T): T {
        extensions.add(extension)
        return extension
    }

    @BeforeAll
    fun extendableBeforeAll() {
        extensions.forEach(Extension::extBeforeAll)
    }

    @BeforeEach
    fun extendableBeforeEach() {
        extensions.forEach(Extension::extBeforeEach)
    }

    @AfterEach
    fun extendableAfterEach() {
        extensions.asReversed().forEach(Extension::extAfterEach)
    }

    @AfterAll
    fun extendableAfterAll() {
        extensions.asReversed().forEach(Extension::extAfterAll)
    }
}

open class ExtendableImpl : Extendable {
    override val extensions = mutableListOf<Extension>()
}

interface Extension {
    fun extBeforeAll() {}
    fun extBeforeEach() {}
    fun extAfterEach() {}
    fun extAfterAll() {}
}



