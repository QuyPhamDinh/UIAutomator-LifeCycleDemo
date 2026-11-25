package com.example.wdioautomation.screens

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.example.wdioautomation.screens.components.TabBar
import com.example.wdioautomation.support.DEMO_PACKAGE
import com.example.wdioautomation.support.UI_TIMEOUT_MS
import com.example.wdioautomation.support.waitFor
import org.junit.Assert.assertNotNull

internal class FormsScreen(private val device: UiDevice) {
    private val textInput = By.desc("text-input").pkg(DEMO_PACKAGE)
    private val formSwitch = By.desc("switch").pkg(DEMO_PACKAGE)
    val inputText: String get() = device.waitFor(textInput).text.orEmpty()
    val isInputEmpty: Boolean get() = inputText.let { it.isEmpty() || it == "Type something" }
    val isOn: Boolean get() = device.waitFor(formSwitch).isChecked

    fun open() {
        TabBar(device).open("Forms")
        awaitVisible()
    }

    fun awaitVisible() {
        device.waitFor(By.desc("Forms-screen").pkg(DEMO_PACKAGE))
    }

    fun focusInput() {
        device.waitFor(By.desc("text-input").pkg(DEMO_PACKAGE).clickable(true).enabled(true)).click()
        device.waitFor(By.desc("text-input").pkg(DEMO_PACKAGE).focused(true))
    }

    fun enter(text: String) {
        device.waitFor(textInput).text = text
    }

    fun setSwitch(on: Boolean) {
        val toggle = device.waitFor(formSwitch)
        if (toggle.isChecked != on) toggle.click()
        assertNotNull(
            "The Forms switch did not reach checked=$on",
            device.wait(Until.findObject(By.desc("switch").pkg(DEMO_PACKAGE).checked(on)), UI_TIMEOUT_MS),
        )
    }
}
