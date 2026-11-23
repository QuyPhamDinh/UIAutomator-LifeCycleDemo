# UI Automator Lifecycle Demo

Kotlin UI Automator tests drive the separately installed WDIO Android demo
(`com.wdiodemoapp`). The `app` module hosts AndroidJUnitRunner. The suite covers
successful login and signup, Forms state after Home and Recents, state after
background process death, and state after a force-stop.

## Install the WDIO demo app

Start an emulator or connect an unlocked Android device with USB debugging enabled.
Download `android.wdio.native.app.v2.2.0.apk` from the **Assets** section of
release **v2.2.0** on the [WDIO Native Demo App releases page](https://github.com/webdriverio/native-demo-app/releases).

Run the following from the directory containing the downloaded APK. Replace
`emulator-5554` with the serial shown by `adb devices`.

```sh
adb devices
adb -s emulator-5554 install -r ./android.wdio.native.app.v2.2.0.apk
adb -s emulator-5554 shell pm path com.wdiodemoapp
```

The last command prints the APK path when the demo app is installed. Gradle
installs the test harness and instrumentation APK when you run the suite.

## Run the tests

### Run all six UI Automator tests

```sh
ANDROID_SERIAL=emulator-5554 make test
```

### Run one test method

```sh
ANDROID_SERIAL=emulator-5554 bash scripts/run-device-tests.sh ./gradlew :app:connectedDebugAndroidTest \
  '-Pandroid.testInstrumentationRunnerArguments.class=com.example.wdioautomation.tests.lifecycle.WarmResumeTests#formsStateSurvivesHomeAndResume'
```

Replace the value after `class=` with the fully qualified test class and method
in `package.ClassName#methodName` format. Omit `#methodName` to run the entire
class. Replace `emulator-5554` with your device serial from `adb devices`.

### Run a test group

Use `make smoke` for login and signup or `make lifecycle` for the Forms lifecycle
tests. Both targets accept the same `ANDROID_SERIAL` environment variable.
Recents resumes through the app's launcher intent so the test does not depend on
the launcher card's layout. The process-death test requires the system to kill
the background process with `am kill`, then checks that the temporary state
clears. Force-stop is exercised in a separate test.

## GitHub Actions

The Android UI Automator workflow runs the full suite on pull requests and
pushes to `main`. You can also start it from the repository’s **Actions** tab.
It installs the WDIO APK on an Android 16 emulator and uploads the reports and
any captured failure artifacts as `android-ui-automator-results`.

## Results and failure artifacts

The runner prints a unique directory under `build/device-test-results/` for each
invocation. Gradle writes HTML under `html/` and XML under `xml/`. After Gradle
finishes, the runner collects screenshots and UI hierarchies for failed tests
under `devices/<gradle-device-name>/<class>/<method>/` within the same run directory.

Set `ANDROID_SERIAL` to select one device or leave it unset to collect from all
connected devices. In CI, archive `build/device-test-results/` even when the test
command fails. Runs launched directly from Android Studio or Gradle also get
unique on-device artifact paths. Use the runner commands above for automatic
collection.
