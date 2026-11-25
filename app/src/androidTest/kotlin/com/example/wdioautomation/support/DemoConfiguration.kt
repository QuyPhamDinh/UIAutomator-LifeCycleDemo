package com.example.wdioautomation.support

import androidx.test.platform.app.InstrumentationRegistry

private const val DEFAULT_DEMO_PACKAGE = "com.wdiodemoapp"
private const val DEFAULT_UI_TIMEOUT_MS = 10_000L
private val PACKAGE_ID = Regex("[A-Za-z][A-Za-z0-9_]*(?:\\.[A-Za-z][A-Za-z0-9_]*)+")

internal val DEMO_PACKAGE: String by lazy {
    val value = InstrumentationRegistry.getArguments().getString("demoPackage") ?: DEFAULT_DEMO_PACKAGE
    require(PACKAGE_ID.matches(value)) { "demoPackage must be an Android application ID" }
    value
}

internal val UI_TIMEOUT_MS: Long by lazy {
    val value = InstrumentationRegistry.getArguments().getString("uiTimeoutMs")
    if (value == null) {
        DEFAULT_UI_TIMEOUT_MS
    } else {
        requireNotNull(value.toLongOrNull()?.takeIf { it > 0 }) {
            "uiTimeoutMs must be a positive integer in milliseconds"
        }
    }
}
