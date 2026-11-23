package com.example.wdioautomation.tests.lifecycle

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wdioautomation.screens.FormsScreen
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import com.example.wdioautomation.support.DemoAppRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class WarmResumeTests {
    @get:Rule
    val app = DemoAppRule()

    @Test
    fun formsStateSurvivesHomeAndResume() {
        val forms = FormsScreen(app.device)
        forms.open()
        forms.enter("")
        forms.setSwitch(false)
        assertTrue(forms.isInputEmpty)
        forms.enter("Draft before switching")
        forms.setSwitch(true)

        app.lifecycle.sendToBackground()
        app.lifecycle.activate()

        forms.awaitVisible()
        assertEquals("Draft before switching", forms.inputText)
        assertTrue(forms.isOn)
        forms.setSwitch(false)
        assertFalse(forms.isOn)
    }

    @Test
    fun formsStateSurvivesOpeningAndroidRecents() {
        val forms = FormsScreen(app.device)
        forms.open()
        forms.enter("Android draft")
        forms.setSwitch(true)

        app.lifecycle.openRecents()
        app.lifecycle.activate()

        forms.awaitVisible()
        assertEquals("Android draft", forms.inputText)
        assertTrue(forms.isOn)
    }
}
