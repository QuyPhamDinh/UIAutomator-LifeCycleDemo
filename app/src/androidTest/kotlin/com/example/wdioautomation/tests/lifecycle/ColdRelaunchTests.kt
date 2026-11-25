package com.example.wdioautomation.tests.lifecycle

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wdioautomation.screens.FormsScreen
import com.example.wdioautomation.support.DemoAppRule
import com.example.wdioautomation.tests.Lifecycle
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Lifecycle
@RunWith(AndroidJUnit4::class)
internal class ColdRelaunchTests {
    @get:Rule
    val app = DemoAppRule()

    @Test
    fun forceStopClearsTemporaryFormsState() {
        val forms = FormsScreen(app.device)
        forms.open()
        forms.fillDraft("Draft before termination")

        app.lifecycle.forceStop()
        app.lifecycle.activate()
        forms.open()

        forms.assertStateCleared()
    }
}
