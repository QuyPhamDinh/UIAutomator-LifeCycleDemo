package com.example.wdioautomation.tests.accessibility

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject2
import com.example.wdioautomation.screens.FormsScreen
import com.example.wdioautomation.screens.HomeScreen
import com.example.wdioautomation.screens.components.TabBar
import com.example.wdioautomation.support.DEMO_PACKAGE
import com.example.wdioautomation.support.DemoAppRule
import java.util.Locale
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class HomeAccessibilityTests {
    @get:Rule
    val app = DemoAppRule()

    @Test
    fun homeExposesReadableContent() {
        val home = HomeScreen(app.device)
        home.awaitReadableText("WEBDRIVER")
        home.awaitReadableText("Demo app for the appium-boilerplate")
    }

    @Test
    fun clickableHomeControlsExposeNames() {
        HomeScreen(app.device).awaitVisible()
        val controls = app.device.findObjects(By.pkg(DEMO_PACKAGE).clickable(true))
        assertTrue("Expected clickable controls on Home", controls.isNotEmpty())

        val unnamed = controls.filterNot { it.hasReadableName() }
            .joinToString { "${it.className} (id=${it.resourceName}, bounds=${it.visibleBounds})" }
        assertTrue("Clickable Home controls without an accessible name: $unnamed", unnamed.isEmpty())
    }

    @Test
    fun primaryTabsHaveUsableTouchTargets() {
        HomeScreen(app.device).awaitVisible()
        val tabs = TabBar(app.device)
        val density = InstrumentationRegistry.getInstrumentation().targetContext
            .resources.displayMetrics.density

        val labels = listOf("Home", "Login", "Forms")
        val primaryTabs = labels.map(tabs::awaitTab) + listOfNotNull(tabs.findTab("Menu"))
        for (tab in primaryTabs) {
            val label = tab.contentDescription
            assertTrue("$label should be focusable", tab.isFocusable)
            assertTrue("$label should be clickable", tab.isClickable)
            val bounds = tab.visibleBounds
            val widthDp = bounds.width() / density
            val heightDp = bounds.height() / density
            assertTrue(
                String.format(Locale.ROOT, "%s visible touch target is %.1f × %.1f dp, expected 48 × 48 dp",
                    label, widthDp, heightDp),
                widthDp >= 47.5f && heightDp >= 47.5f,
            )
        }
    }

    @Test
    fun tabSelectionFollowsTheActiveScreen() {
        val home = HomeScreen(app.device)
        val tabs = TabBar(app.device)
        home.awaitVisible()
        tabs.awaitSelected("Home")

        tabs.open("Forms")
        FormsScreen(app.device).awaitVisible()
        tabs.awaitSelected("Forms")

        tabs.open("Home")
        home.awaitVisible()
        tabs.awaitSelected("Home")
    }

    private fun UiObject2.hasReadableName(): Boolean =
        !contentDescription.isNullOrBlank() || !text.isNullOrBlank() ||
            children.any { !it.isClickable && it.hasReadableName() }
}
