package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement
import kotlin.reflect.KClass

interface ModuleSupport {

    fun <T : Module> module(factory: (Browser) -> T): T
    fun <T : Module> module(klass: KClass<T>): T
    fun <T : ScopedModule> scopedModule(factory: (Browser, WebElement) -> T, scope: WebElement): T
    fun <T : ScopedModule> scopedModule(klass: KClass<T>, scope: WebElement): T

}