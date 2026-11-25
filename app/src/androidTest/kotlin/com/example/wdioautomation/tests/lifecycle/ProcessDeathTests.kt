package com.example.wdioautomation.tests.lifecycle

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wdioautomation.screens.FormsScreen
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
        forms.fillDraft("Draft before kill")

        app.lifecycle.sendToBackground()
        app.lifecycle.killBackgroundProcess()
        app.lifecycle.activate()
        forms.open()

        forms.assertStateCleared()
    }
}
