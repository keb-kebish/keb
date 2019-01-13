package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

fun WebElement.childrenByCss(selector: String) = ScopedCssSelector(selector, this).getWebElements()
fun WebElement.childrenByHtml(tag: String) = ScopedHtmlSelector(tag, this).getWebElements()
fun WebElement.childrenByXpath(xpath: String) = ScopedXpathSelector(xpath, this).getWebElements()