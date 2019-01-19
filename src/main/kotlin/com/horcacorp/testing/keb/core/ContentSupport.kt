package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

interface ContentSupport {

    fun css(selector: String, scope: WebElement? = null, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElement
    fun cssList(selector: String, scope: WebElement? = null, fetch: ContentFetchType? = null, waitParam: Any? = null): List<WebElement>

    fun html(tag: String, scope: WebElement? = null, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElement
    fun htmlList(tag: String, scope: WebElement? = null, fetch: ContentFetchType? = null, waitParam: Any? = null): List<WebElement>

    fun xpath(xpath: String, scope: WebElement? = null, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElement
    fun xpathList(xpath: String, scope: WebElement? = null, fetch: ContentFetchType? = null, waitParam: Any? = null): List<WebElement>

}