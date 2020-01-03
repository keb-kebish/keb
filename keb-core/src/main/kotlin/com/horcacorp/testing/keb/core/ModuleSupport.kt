package com.horcacorp.testing.keb.core

interface ModuleSupport {

    val browser: Browser

    fun <T : Module> module(factory: () -> T) = module(factory())

    fun <T : Module> module(module: T) = module.apply { browser = this@ModuleSupport.browser }

}