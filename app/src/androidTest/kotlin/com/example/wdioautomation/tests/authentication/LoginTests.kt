package com.example.wdioautomation.tests.authentication

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wdioautomation.screens.LoginScreen
import com.example.wdioautomation.screens.components.AlertComponent
import com.example.wdioautomation.support.DemoAppRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class LoginTests {
    @get:Rule
    val app = DemoAppRule()

    @Test
    fun successfulLogin() {
        val login = LoginScreen(app.device)
        val alert = AlertComponent(app.device)
        login.open()
        login.submit("test@webdriver.io", "Test1234!")
        alert.assertContains("Success")
        alert.dismiss()
    }
}
