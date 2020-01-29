package org.kebish.core

import org.openqa.selenium.WebElement

class EmptyWebElementList(override val missingContentSelector: String) : List<WebElement>, EmptyContent {

    override val size: Int get() = 0
    override fun isEmpty(): Boolean = true
    override fun contains(element: WebElement): Boolean = false
    override fun containsAll(elements: Collection<WebElement>): Boolean = elements.isEmpty()

    override fun get(index: Int): WebElement = throw IndexOutOfBoundsException("Empty list doesn't contain element at index $index.")
    override fun indexOf(element: WebElement): Int = -1
    override fun lastIndexOf(element: WebElement): Int = -1

    override fun iterator(): Iterator<WebElement> = EmptyIterator
    override fun listIterator(): ListIterator<WebElement> = EmptyIterator
    override fun listIterator(index: Int): ListIterator<WebElement> {
        if (index != 0) throw IndexOutOfBoundsException("Index: $index")
        return EmptyIterator
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<WebElement> {
        if (fromIndex == 0 && toIndex == 0) return this
        throw IndexOutOfBoundsException("fromIndex: $fromIndex, toIndex: $toIndex")
    }
}

object EmptyIterator : ListIterator<WebElement> {
    override fun hasNext(): Boolean = false
    override fun hasPrevious(): Boolean = false
    override fun nextIndex(): Int = 0
    override fun previousIndex(): Int = -1
    override fun next(): Nothing = throw NoSuchElementException()
    override fun previous(): Nothing = throw NoSuchElementException()
}