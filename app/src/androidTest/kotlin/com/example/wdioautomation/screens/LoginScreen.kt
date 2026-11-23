package com.example.wdioautomation.screens

import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import com.example.wdioautomation.screens.components.TabBar
import com.example.wdioautomation.support.DEMO_PACKAGE
import com.example.wdioautomation.support.waitFor

internal class LoginScreen(private val device: UiDevice) {
    private val screen = By.desc("Login-screen").pkg(DEMO_PACKAGE)

    fun open() {
        TabBar(device).open("Login")
        device.waitFor(screen)
    }

    fun submit(email: String, password: String, signUp: Boolean = false) {
        revealControl(if (signUp) "button-sign-up-container" else "button-login-container").click()
        revealControl("input-email").text = email
        revealControl("input-password").text = password
        if (signUp) revealControl("input-repeat-password").text = password
        revealControl(if (signUp) "button-SIGN UP" else "button-LOGIN").click()
    }

    private fun revealControl(description: String): UiObject2 {
        val loginScreen = device.waitFor(screen)
        val control = By.desc(description).enabled(true)
        loginScreen.wait(Until.findObject(control), 750L)?.let { return it }

        for (direction in listOf(Direction.DOWN, Direction.UP)) {
            for (attempt in 1..MAX_SCROLLS_PER_DIRECTION) {
                val canScrollFurther = loginScreen.scroll(direction, 0.55f)
                loginScreen.findObject(control)?.let { return it }
                if (!canScrollFurther) break
            }
        }

        error("Could not reveal '$description' within Login-screen")
    }

    private companion object {
        const val MAX_SCROLLS_PER_DIRECTION = 5
    }
}
