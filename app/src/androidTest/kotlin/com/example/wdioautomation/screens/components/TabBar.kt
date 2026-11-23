package com.example.wdioautomation.screens.components

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.example.wdioautomation.support.DEMO_PACKAGE
import com.example.wdioautomation.support.tapWhenReady
import com.example.wdioautomation.support.waitFor

internal class TabBar(private val device: UiDevice) {
    fun awaitReady() {
        device.waitFor(By.desc("Home").pkg(DEMO_PACKAGE))
    }

    fun open(tab: String) = device.tapWhenReady(tab)
}
