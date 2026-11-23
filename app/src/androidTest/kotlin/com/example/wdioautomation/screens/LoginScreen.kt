package com.example.wdioautomation.screens

import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import com.example.wdioautomation.screens.components.TabBar
import com.example.wdioautomation.support.DEMO_PACKAGE
import com.example.wdioautomation.support.tapWhenReady
import com.example.wdioautomation.support.waitFor

internal class LoginScreen(private val device: UiDevice) {
    private val screen = By.desc("Login-screen").pkg(DEMO_PACKAGE)

    fun open() {
        TabBar(device).open("Login")
        device.waitFor(screen)
    }

    fun submit(email: String, password: String, signUp: Boolean = false) {
        device.tapWhenReady(if (signUp) "button-sign-up-container" else "button-login-container")
        device.waitFor(By.desc("input-email").pkg(DEMO_PACKAGE)).text = email
        device.waitFor(By.desc("input-password").pkg(DEMO_PACKAGE)).text = password
        if (signUp) device.waitFor(By.desc("input-repeat-password").pkg(DEMO_PACKAGE)).text = password

        val button = if (signUp) "button-SIGN UP" else "button-LOGIN"
        val selector = By.desc(button).pkg(DEMO_PACKAGE)
        if (!device.hasObject(selector)) {
            device.findObject(By.scrollable(true).pkg(DEMO_PACKAGE))?.scroll(Direction.DOWN, 0.8f)
        }
        device.tapWhenReady(button)
    }
}
