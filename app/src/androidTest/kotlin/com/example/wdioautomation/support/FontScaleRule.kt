package com.example.wdioautomation.support

import android.os.SystemClock
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

private const val FONT_SCALE_APPLY_TIMEOUT_MS = 5_000L

internal class FontScaleRule(private val device: UiDevice, private val scale: Float) : TestRule {
    override fun apply(base: Statement, description: Description): Statement = object : Statement() {
        override fun evaluate() {
            val originalSetting = readSetting()
            val originalScale = resources.configuration.fontScale
            var testFailure: Throwable? = null
            try {
                applyScale(scale.toString(), scale)
                base.evaluate()
            } catch (failure: Throwable) {
                testFailure = failure
                throw failure
            } finally {
                try {
                    applyScale(originalSetting, originalScale)
                } catch (cleanupFailure: Throwable) {
                    if (testFailure == null) throw cleanupFailure
                    testFailure.addSuppressed(cleanupFailure)
                }
            }
        }
    }

    private val resources
        get() = InstrumentationRegistry.getInstrumentation().targetContext.resources

    private fun readSetting(): String =
        device.executeShellCommand("settings get system font_scale").trim()

    private fun applyScale(setting: String, expected: Float) {
        val command = if (setting == "null") {
            "settings delete system font_scale"
        } else {
            require(setting.matches(Regex("[0-9]+(?:\\.[0-9]+)?"))) { "Invalid font scale: $setting" }
            "settings put system font_scale $setting"
        }
        device.executeShellCommand(command)

        val deadline = SystemClock.elapsedRealtime() + FONT_SCALE_APPLY_TIMEOUT_MS
        while (SystemClock.elapsedRealtime() < deadline) {
            if (resources.configuration.fontScale == expected) {
                val currentSetting = readSetting()
                // Android can write the effective default back after a setting is deleted.
                if (currentSetting == setting || (setting == "null" && currentSetting.toFloatOrNull() == expected)) {
                    return
                }
            }
            SystemClock.sleep(100)
        }
        error("Expected font scale $expected (setting $setting), found " +
            "${resources.configuration.fontScale} (setting ${readSetting()})")
    }
}
