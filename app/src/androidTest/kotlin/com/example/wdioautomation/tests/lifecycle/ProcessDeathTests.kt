package com.example.wdioautomation.tests.lifecycle

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wdioautomation.screens.FormsScreen
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import com.example.wdioautomation.support.DemoAppRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class ProcessDeathTests {
    @get:Rule
    val app = DemoAppRule()

    @Test
    fun processRecreationClearsTemporaryFormsState() {
        val forms = FormsScreen(app.device)
        forms.open()
        forms.enter("Draft before kill")
        forms.setSwitch(true)
        assertTrue("Draft must be present before relaunch", forms.inputText == "Draft before kill")

        app.lifecycle.sendToBackground()
        app.lifecycle.killBackgroundProcess()
        app.lifecycle.activate()
        forms.open()

        assertTrue("Expected empty input, found '${forms.inputText}'", forms.isInputEmpty)
        assertFalse(forms.isOn)
    }
}
