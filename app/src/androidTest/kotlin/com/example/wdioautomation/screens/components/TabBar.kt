package com.example.wdioautomation.screens.components

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import com.example.wdioautomation.support.DEMO_PACKAGE
import com.example.wdioautomation.support.tapWhenReady
import com.example.wdioautomation.support.waitFor

internal class TabBar(private val device: UiDevice) {
    fun awaitReady() {
        awaitTab("Home")
    }

    fun awaitTab(tab: String): UiObject2 = device.waitFor(tabSelector(tab))

    fun findTab(tab: String): UiObject2? = device.findObject(tabSelector(tab))

    fun awaitSelected(tab: String) {
        device.waitFor(tabSelector(tab).selected(true))
    }

    fun open(tab: String) = device.tapWhenReady(tab)

    private fun tabSelector(tab: String) = By.desc(tab).pkg(DEMO_PACKAGE)
}
