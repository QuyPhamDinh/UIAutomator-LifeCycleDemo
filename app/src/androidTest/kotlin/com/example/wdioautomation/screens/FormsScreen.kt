package com.example.wdioautomation.screens

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.example.wdioautomation.screens.components.TabBar
import com.example.wdioautomation.support.DEMO_PACKAGE
import com.example.wdioautomation.support.UI_TIMEOUT_MS
import com.example.wdioautomation.support.waitFor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull

private const val INPUT_HINT = "Type something"

internal class FormsScreen(private val device: UiDevice) {
    private val screen = By.desc("Forms-screen").pkg(DEMO_PACKAGE)
    private val textInput get() = By.desc("text-input").pkg(DEMO_PACKAGE)
    private val formSwitch get() = By.desc("switch").pkg(DEMO_PACKAGE)
    val inputText: String get() = device.waitFor(textInput).text.orEmpty()
    val isInputEmpty: Boolean get() = inputText.let { it.isEmpty() || it == INPUT_HINT }
    val isOn: Boolean get() = device.waitFor(formSwitch).isChecked

    fun open() {
        TabBar(device).open("Forms")
        awaitVisible()
    }

    fun awaitVisible() {
        device.waitFor(screen)
    }

    fun focusInput() {
        device.waitFor(textInput.clickable(true).enabled(true)).click()
        device.waitFor(textInput.focused(true))
    }

    fun enter(text: String) {
        device.waitFor(textInput).text = text
    }

    fun fillDraft(text: String) {
        enter(text)
        setSwitch(true)
        assertEquals("Draft must be present before relaunch", text, inputText)
    }

    fun assertStateCleared() {
        val draft = inputText
        assertEquals("Forms draft after relaunch", "", if (draft == INPUT_HINT) "" else draft)
        assertFalse("Forms switch should be off after relaunch", isOn)
    }

    fun setSwitch(on: Boolean) {
        val toggle = device.waitFor(formSwitch)
        if (toggle.isChecked != on) toggle.click()
        assertNotNull(
            "The Forms switch did not reach checked=$on",
            device.wait(Until.findObject(formSwitch.checked(on)), UI_TIMEOUT_MS),
        )
    }
}
