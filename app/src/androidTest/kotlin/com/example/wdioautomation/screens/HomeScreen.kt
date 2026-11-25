package com.example.wdioautomation.screens

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import com.example.wdioautomation.support.DEMO_PACKAGE
import com.example.wdioautomation.support.UI_TIMEOUT_MS
import com.example.wdioautomation.support.waitFor

internal class HomeScreen(private val device: UiDevice) {
    data class ClickableControls(val count: Int, val unnamed: List<String>)

    private val screen = By.desc("Home-screen").pkg(DEMO_PACKAGE)

    fun awaitVisible() {
        device.waitFor(screen)
    }

    fun awaitReadableText(text: String) {
        val home = device.waitFor(screen)
        checkNotNull(home.wait(Until.findObject(By.text(text)), UI_TIMEOUT_MS)) {
            "Home-screen did not expose readable text '$text'"
        }
    }

    fun inspectClickableControls(): ClickableControls {
        awaitVisible()
        val controls = device.findObjects(By.pkg(DEMO_PACKAGE).clickable(true))
        val unnamed = controls.filterNot { it.hasReadableName() }
            .map { "${it.className} (id=${it.resourceName}, bounds=${it.visibleBounds})" }
        return ClickableControls(count = controls.size, unnamed = unnamed)
    }

    private fun UiObject2.hasReadableName(): Boolean =
        !contentDescription.isNullOrBlank() || !text.isNullOrBlank() ||
            children.any { !it.isClickable && it.hasReadableName() }
}
