# UI Automator Lifecycle Demo

Kotlin UI Automator tests drive the separately installed WDIO Android demo
(`com.wdiodemoapp`). The `app` module hosts AndroidJUnitRunner. The suite covers
successful login and signup, Forms state after Home and Recents, state after
background process death, and state after a force-stop.

## Run the tests

Start an emulator or connect an unlocked Android device. Install the WDIO APK
from the WDIO repository before running the suite.

```sh
adb -s emulator-5554 install /path/to/apps/android.wdio.native.app.v2.2.0.apk
```

### Run all six UI Automator tests

```sh
ANDROID_SERIAL=emulator-5554 make test
```

### Run one test method

```sh
ANDROID_SERIAL=emulator-5554 ./gradlew :app:connectedDebugAndroidTest \
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

## Results and failure artifacts

Gradle writes HTML to `app/build/reports/androidTests/connected/debug/` and XML
to `app/build/outputs/androidTest-results/connected/debug/`. For a failed test,
the rule attempts to save a screenshot and UI hierarchy under the host app's
media directory, grouped by test class and method.

```sh
adb -s emulator-5554 pull /sdcard/Android/media/com.example.wdioautomation/failures
```
