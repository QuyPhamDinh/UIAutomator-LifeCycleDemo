package com.example.wdioautomation.support

import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until

internal fun UiDevice.waitFor(selector: BySelector): UiObject2 =
    checkNotNull(wait(Until.findObject(selector), UI_TIMEOUT_MS)) {
        "Timed out waiting for $selector"
    }

internal fun UiDevice.tapWhenReady(description: String) {
    waitFor(By.desc(description).pkg(DEMO_PACKAGE).enabled(true)).click()
}
