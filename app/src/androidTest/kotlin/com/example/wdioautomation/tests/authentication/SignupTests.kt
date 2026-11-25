package com.example.wdioautomation.tests.authentication

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wdioautomation.screens.LoginScreen
import com.example.wdioautomation.screens.components.AlertComponent
import com.example.wdioautomation.support.DemoAppRule
import com.example.wdioautomation.tests.Smoke
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Smoke
@RunWith(AndroidJUnit4::class)
internal class SignupTests {
    @get:Rule
    val app = DemoAppRule()

    @Test
    fun successfulSignup() {
        val loginScreen = LoginScreen(app.device)
        val alert = AlertComponent(app.device)
        loginScreen.open()
        loginScreen.signUp(TestUsers.EMAIL, TestUsers.PASSWORD)
        alert.assertContains("Signed Up")
        alert.dismiss()
    }
}
