.PHONY: test smoke lifecycle

GRADLE ?= ./gradlew
RUN_TESTS := bash scripts/run-device-tests.sh
TEST_TASK := connectedDebugAndroidTest
TEST_PACKAGE := com.example.wdioautomation.tests

test:
	$(RUN_TESTS) $(GRADLE) $(TEST_TASK) -Pandroid.testInstrumentationRunnerArguments.package=$(TEST_PACKAGE)

smoke:
	$(RUN_TESTS) $(GRADLE) $(TEST_TASK) -Pandroid.testInstrumentationRunnerArguments.annotation=$(TEST_PACKAGE).Smoke

lifecycle:
	$(RUN_TESTS) $(GRADLE) $(TEST_TASK) -Pandroid.testInstrumentationRunnerArguments.annotation=$(TEST_PACKAGE).Lifecycle
