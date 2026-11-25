package com.example.wdioautomation.support

import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until

private const val INITIAL_CONTROL_WAIT_MS = 750L
private const val MAX_SCROLLS_PER_DIRECTION = 5

internal fun UiObject2.scrollTo(selector: BySelector): UiObject2 {
    wait(Until.findObject(selector), INITIAL_CONTROL_WAIT_MS)?.let { return it }

    for (direction in listOf(Direction.DOWN, Direction.UP)) {
        var remaining = MAX_SCROLLS_PER_DIRECTION
        while (remaining > 0) {
            remaining--
            val canScrollFurther = scroll(direction, 0.55f)
            findObject(selector)?.let { return it }
            if (!canScrollFurther) break
        }
    }

    error("Could not reveal $selector within ${contentDescription ?: className}")
}
