package com.example.wdioautomation.screens

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import com.example.wdioautomation.screens.components.TabBar
import com.example.wdioautomation.support.DEMO_PACKAGE
import com.example.wdioautomation.support.scrollTo
import com.example.wdioautomation.support.waitFor

internal class LoginScreen(private val device: UiDevice) {
    private val screen = By.desc("Login-screen").pkg(DEMO_PACKAGE)

    fun open() {
        TabBar(device).open("Login")
        device.waitFor(screen)
    }

    fun login(email: String, password: String) {
        control("button-login-container").click()
        enterCredentials(email, password)
        control("button-LOGIN").click()
    }

    fun signUp(email: String, password: String) {
        control("button-sign-up-container").click()
        enterCredentials(email, password)
        control("input-repeat-password").text = password
        control("button-SIGN UP").click()
    }

    private fun enterCredentials(email: String, password: String) {
        control("input-email").text = email
        control("input-password").text = password
    }

    private fun control(description: String): UiObject2 =
        device.waitFor(screen).scrollTo(By.desc(description).enabled(true))
}
