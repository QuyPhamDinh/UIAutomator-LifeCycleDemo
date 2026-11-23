package com.example.wdioautomation.support

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.example.wdioautomation.screens.components.TabBar
import java.io.File
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement

internal class DemoAppRule : TestWatcher() {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    val device: UiDevice = UiDevice.getInstance(instrumentation)
    val lifecycle = LifecycleActions(device, instrumentation.targetContext)

    override fun apply(base: Statement, description: Description): Statement =
        super.apply(object : Statement() {
            override fun evaluate() {
                lifecycle.forceStop()
                lifecycle.activate()
                TabBar(device).awaitReady()
                base.evaluate()
            }
        }, description)

    override fun failed(error: Throwable, description: Description) {
        runCatching {
            val context = instrumentation.targetContext
            val root = context.externalMediaDirs.firstOrNull() ?: context.filesDir
            val directory = File(root, "failures/${description.className}/${description.methodName}")
            check(directory.isDirectory || directory.mkdirs())
            runCatching {
                check(device.takeScreenshot(File(directory, "screen.png")))
            }.onFailure { Log.w("DemoAppRule", "Could not capture screenshot", it) }
            device.dumpWindowHierarchy(File(directory, "hierarchy.xml"))
        }.onFailure { Log.w("DemoAppRule", "Could not capture failure artifacts", it) }
    }
}
