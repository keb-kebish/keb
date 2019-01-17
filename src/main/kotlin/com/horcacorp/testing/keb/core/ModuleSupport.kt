package com.horcacorp.testing.keb.core

import kotlin.reflect.KClass

interface ModuleSupport {

    fun <T : Module> module(factory: (Browser) -> T): T
    fun <T : Module> module(klass: KClass<T>): T
    fun <T : ScopedModule> scopedModule(factory: (Browser, WebElementDelegate) -> T, scope: WebElementDelegate): T
    fun <T : ScopedModule> scopedModule(klass: KClass<T>, scope: WebElementDelegate): T

}