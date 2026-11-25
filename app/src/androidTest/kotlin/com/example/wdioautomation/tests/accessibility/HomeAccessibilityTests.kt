package com.example.wdioautomation.tests.accessibility

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wdioautomation.screens.FormsScreen
import com.example.wdioautomation.screens.HomeScreen
import com.example.wdioautomation.screens.components.TabBar
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
        val (count, unnamed) = HomeScreen(app.device).inspectClickableControls()
        assertTrue("Expected clickable controls on Home", count > 0)
        assertTrue(
            "Clickable Home controls without an accessible name: ${unnamed.joinToString()}",
            unnamed.isEmpty(),
        )
    }

    @Test
    fun primaryTabsHaveUsableTouchTargets() {
        HomeScreen(app.device).awaitVisible()
        val tabs = TabBar(app.device)
        for (label in listOf("Home", "Login", "Forms")) {
            assertUsableTouchTarget(tabs, label)
        }
    }

    @Test
    fun menuHasUsableTouchTarget() {
        HomeScreen(app.device).awaitVisible()
        assertUsableTouchTarget(TabBar(app.device), "Menu")
    }

    private fun assertUsableTouchTarget(tabs: TabBar, label: String) {
        val (focusable, clickable, widthDp, heightDp) = tabs.inspect(label)
        assertTrue("$label should be focusable", focusable)
        assertTrue("$label should be clickable", clickable)
        assertTrue(
            String.format(
                Locale.ROOT,
                "%s visible touch target is %.1f × %.1f dp, expected 48 × 48 dp",
                label, widthDp, heightDp,
            ),
            widthDp >= 47.5f && heightDp >= 47.5f,
        )
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

}
