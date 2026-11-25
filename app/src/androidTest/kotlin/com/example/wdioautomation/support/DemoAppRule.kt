package com.example.wdioautomation.support

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.example.wdioautomation.screens.components.TabBar
import java.io.File
import java.util.UUID
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
            val outputDirectory = InstrumentationRegistry.getArguments().getString("additionalTestOutputDir")
            val root = outputDirectory?.let(::File)
                ?: context.externalMediaDirs.firstOrNull()
                ?: context.filesDir
            val directory = File(root, "failures/$artifactRunId/${description.className}/${description.methodName}")
            check(directory.isDirectory || directory.mkdirs())
            runCatching {
                check(device.takeScreenshot(File(directory, "screen.png")))
            }.onFailure { Log.w("DemoAppRule", "Could not capture screenshot", it) }
            runCatching {
                device.dumpWindowHierarchy(File(directory, "hierarchy.xml"))
            }.onFailure { Log.w("DemoAppRule", "Could not capture UI hierarchy", it) }
            runCatching {
                File(directory, "logcat.txt").writeText(device.executeShellCommand("logcat -d -t 500"))
            }.onFailure { Log.w("DemoAppRule", "Could not capture logcat", it) }
        }.onFailure { Log.w("DemoAppRule", "Could not capture failure artifacts", it) }
    }

    private companion object {
        val artifactRunId: String by lazy {
            InstrumentationRegistry.getArguments().getString("artifactRunId")
                ?.takeIf { runId ->
                    val assets = InstrumentationRegistry.getInstrumentation().context.assets
                    val pattern = assets.open("artifact-run-id-pattern.txt").bufferedReader()
                        .use { it.readText().trim().toRegex() }
                    pattern.matches(runId)
                }
                ?: UUID.randomUUID().toString()
        }
    }
}
