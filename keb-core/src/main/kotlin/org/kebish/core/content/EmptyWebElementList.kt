package org.kebish.core.content

import org.openqa.selenium.WebElement

public class EmptyWebElementList(override val missingContentSelector: String) : List<WebElement> by emptyList(),
    EmptyContent