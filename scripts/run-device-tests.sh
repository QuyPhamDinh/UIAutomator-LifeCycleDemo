#!/usr/bin/env bash
set -eu

mkdir -p build/device-test-results
run_dir=$(mktemp -d "build/device-test-results/$(date -u +%Y%m%dT%H%M%SZ)-XXXXXX")
run_id=${run_dir##*/}
printf 'Device test results: %s\n' "$run_dir"

gradle_status=0
"$@" -Pandroid.enableAdditionalTestOutput=true \
    "-Pandroid.testInstrumentationRunnerArguments.artifactRunId=$run_id" || gradle_status=$?

collection_status=0
for artifacts in app/build/outputs/connected_android_test_additional_output/debugAndroidTest/connected/*/failures/"$run_id"; do
    [ -d "$artifacts" ] || continue
    device_dir=${artifacts%/failures/*}
    destination=$run_dir/devices/${device_dir##*/}
    if ! mkdir -p "$destination" || ! cp -R "$artifacts/." "$destination"; then
        printf 'Could not archive failure artifacts from %s\n' "$device_dir" >&2
        collection_status=1
    fi
done

if [ "$gradle_status" -ne 0 ]; then
    exit "$gradle_status"
fi
exit "$collection_status"
