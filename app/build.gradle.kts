plugins {
    alias(libs.plugins.android.application)
}

val useTestOrchestrator = providers.gradleProperty("useTestOrchestrator").orNull == "true"

android {
    namespace = "com.example.wdioautomation"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.wdioautomation"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    providers.gradleProperty("android.testInstrumentationRunnerArguments.artifactRunId")
        .orNull
        ?.let { runId ->
            val patternFile = layout.projectDirectory.file("src/androidTest/assets/artifact-run-id-pattern.txt")
            val runIdPattern = providers.fileContents(patternFile).asText.get().trim().toRegex()
            require(runIdPattern.matches(runId)) { "Invalid artifact run ID" }
            val runDirectory = rootProject.layout.buildDirectory.dir("device-test-results/$runId").get().asFile
            testOptions {
                reportDir = runDirectory.resolve("html").path
                resultsDir = runDirectory.resolve("xml").path
            }
        }

    testOptions {
        animationsDisabled = true
        if (useTestOrchestrator) {
            execution = "ANDROIDX_TEST_ORCHESTRATOR"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.uiautomator)
    if (useTestOrchestrator) {
        androidTestUtil(libs.androidx.test.orchestrator)
    }
}