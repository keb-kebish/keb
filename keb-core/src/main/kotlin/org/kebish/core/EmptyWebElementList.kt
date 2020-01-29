package org.kebish.core

import org.openqa.selenium.WebElement

class EmptyWebElementList(override val missingContentSelector: String) : ArrayList<WebElement>(), EmptyContent