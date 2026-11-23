package com.example.wdioautomation.screens.components

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.example.wdioautomation.support.DEMO_PACKAGE
import com.example.wdioautomation.support.UI_TIMEOUT_MS
import com.example.wdioautomation.support.waitFor
import org.junit.Assert.assertTrue

internal class AlertComponent(private val device: UiDevice) {
    private val title = By.res(DEMO_PACKAGE, "alert_title")

    fun assertContains(expected: String) {
        val alertTitle = device.waitFor(title).text
        val message = device.waitFor(By.res("android", "message").pkg(DEMO_PACKAGE)).text
        val alertText = "$alertTitle\n$message"
        assertTrue("Expected alert to contain '$expected', was '$alertText'", expected in alertText)
    }

    fun dismiss() {
        device.waitFor(By.clazz("android.widget.Button").text("OK").pkg(DEMO_PACKAGE)).click()
        assertTrue("Alert is still visible", device.wait(Until.gone(title), UI_TIMEOUT_MS) == true)
    }
}
