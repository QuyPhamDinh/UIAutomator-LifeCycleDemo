package com.example.wdioautomation.screens.components

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.example.wdioautomation.support.DEMO_PACKAGE
import com.example.wdioautomation.support.tapWhenReady
import com.example.wdioautomation.support.waitFor

internal class TabBar(private val device: UiDevice) {
    data class TabInfo(
        val focusable: Boolean,
        val clickable: Boolean,
        val widthDp: Float,
        val heightDp: Float,
    )

    fun awaitReady() {
        awaitTab("Home")
    }

    fun inspect(tab: String): TabInfo {
        val node = awaitTab(tab)
        val bounds = node.visibleBounds
        val density = InstrumentationRegistry.getInstrumentation().targetContext
            .resources.displayMetrics.density
        return TabInfo(
            focusable = node.isFocusable,
            clickable = node.isClickable,
            widthDp = bounds.width() / density,
            heightDp = bounds.height() / density,
        )
    }

    fun awaitSelected(tab: String) {
        device.waitFor(tabSelector(tab).selected(true))
    }

    fun open(tab: String) = device.tapWhenReady(tab)

    private fun awaitTab(tab: String) = device.waitFor(tabSelector(tab))

    private fun tabSelector(tab: String) = By.desc(tab).pkg(DEMO_PACKAGE)
}
