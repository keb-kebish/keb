package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

interface ContentSupport {

    fun css(selector: String, scope: WebElement? = null, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElementDelegate
    fun cssList(selector: String, scope: WebElement? = null, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElementsListDelegate

    fun html(tag: String, scope: WebElement? = null, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElementDelegate
    fun htmlList(tag: String, scope: WebElement? = null, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElementsListDelegate

    fun xpath(xpath: String, scope: WebElement? = null, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElementDelegate
    fun xpathList(xpath: String, scope: WebElement? = null, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElementsListDelegate

}