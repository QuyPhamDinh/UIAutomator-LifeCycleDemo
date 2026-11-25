package com.example.wdioautomation.screens

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import com.example.wdioautomation.support.DEMO_PACKAGE
import com.example.wdioautomation.support.UI_TIMEOUT_MS
import com.example.wdioautomation.support.waitFor

internal class HomeScreen(private val device: UiDevice) {
    private val screen = By.desc("Home-screen").pkg(DEMO_PACKAGE)

    fun awaitVisible(): UiObject2 = device.waitFor(screen)

    fun awaitReadableText(text: String): UiObject2 =
        checkNotNull(
            awaitVisible().wait(Until.findObject(By.text(text)), UI_TIMEOUT_MS),
        ) { "Home-screen did not expose readable text '$text'" }
}
