package org.kebish.core

import org.openqa.selenium.WebElement

class EmptyWebElementList(override val missingContentSelector: String) : List<WebElement> by emptyList(), EmptyContent