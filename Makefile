.PHONY: test smoke lifecycle

GRADLE ?= ./gradlew
TEST_TASK := connectedDebugAndroidTest
TEST_PACKAGE := com.example.wdioautomation.tests

test:
	$(GRADLE) $(TEST_TASK) -Pandroid.testInstrumentationRunnerArguments.package=$(TEST_PACKAGE)

smoke:
	$(GRADLE) $(TEST_TASK) -Pandroid.testInstrumentationRunnerArguments.package=$(TEST_PACKAGE).authentication

lifecycle:
	$(GRADLE) $(TEST_TASK) -Pandroid.testInstrumentationRunnerArguments.package=$(TEST_PACKAGE).lifecycle
