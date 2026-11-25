package com.example.wdioautomation.tests.accessibility

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wdioautomation.screens.FormsScreen
import com.example.wdioautomation.support.DemoAppRule
import com.example.wdioautomation.support.FontScaleRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class FormsFontScaleTests {
    private val app = DemoAppRule()

    @get:Rule
    val rules: RuleChain = RuleChain.outerRule(FontScaleRule(app.device, 2f)).around(app)

    @Test
    fun formsInputAndSwitchRemainUsableAtDoubleFontScale() {
        val forms = FormsScreen(app.device)
        forms.open()
        forms.setSwitch(true)
        assertTrue(forms.isOn)
        forms.focusInput()
        forms.enter("Large text draft")
        assertEquals("Large text draft", forms.inputText)
    }
}
