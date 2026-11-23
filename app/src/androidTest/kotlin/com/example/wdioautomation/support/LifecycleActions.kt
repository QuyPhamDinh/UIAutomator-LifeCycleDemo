package com.example.wdioautomation.support

import android.content.Context
import android.os.SystemClock
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

internal class LifecycleActions(private val device: UiDevice, private val context: Context) {
    private val demoWindow = By.pkg(DEMO_PACKAGE).depth(0)

    fun activate() {
        val intent = checkNotNull(context.packageManager.getLaunchIntentForPackage(DEMO_PACKAGE)) {
            "Install the WDIO demo APK ($DEMO_PACKAGE) on the test device."
        }
        context.startActivity(intent)
        device.waitFor(demoWindow)
    }

    fun mainProcessId(): String =
        checkNotNull(processIds(includeSecondary = false).singleOrNull()) {
            "Expected one running main process for $DEMO_PACKAGE"
        }

    fun sendToBackground() {
        check(device.pressHome()) { "Could not press Home" }
        awaitDemoWindowGone()
    }

    fun openRecents() {
        check(device.pressRecentApps()) { "Could not open Android Recents" }
        awaitDemoWindowGone()
    }

    fun forceStop() {
        device.executeShellCommand("am force-stop $DEMO_PACKAGE")
        awaitDemoWindowGone()
        check(processIds().isEmpty()) { "$DEMO_PACKAGE is still running after force-stop" }
    }

    fun killBackgroundProcess() {
        awaitDemoWindowGone()
        val initialPids = processIds()
        check(initialPids.isNotEmpty()) { "$DEMO_PACKAGE exited before am kill" }

        val deadline = SystemClock.elapsedRealtime() + 12_000L
        do {
            device.executeShellCommand("am kill $DEMO_PACKAGE")
            val remainingPids = processIds()
            if (remainingPids.isEmpty()) return
            check(SystemClock.elapsedRealtime() < deadline) {
                "am kill did not stop $DEMO_PACKAGE (initial PIDs: $initialPids, remaining: $remainingPids)"
            }
            SystemClock.sleep(1_000)
        } while (true)
    }

    private fun awaitDemoWindowGone() {
        check(device.wait(Until.gone(demoWindow), UI_TIMEOUT_MS) == true) {
            "$DEMO_PACKAGE window remained visible"
        }
    }

    private fun processIds(includeSecondary: Boolean = true): List<String> =
        device.executeShellCommand("ps -A -o PID,NAME").lineSequence()
            .map { it.trim().split(Regex("\\s+"), limit = 2) }
            .filter { parts ->
                parts.size == 2 && (parts[1] == DEMO_PACKAGE ||
                    (includeSecondary && parts[1].startsWith("$DEMO_PACKAGE:")))
            }
            .map { it[0] }
            .toList()
}
