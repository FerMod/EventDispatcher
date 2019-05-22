#!/bin/bash
set -ex # Exit with nonzero exit code if anything fails and show script content

echo "Generating release ..."
pushd "$(git rev-parse --show-toplevel)"

echo "Updating Maven version from git tag ..."
export PROJECT_VERSION="$(git describe --tags --abbrev=0)"
mvn versions:set -DnewVersion=${PROJECT_VERSION#"v"}

echo "Generating jar binaries ..."
mvn clean install

export RELEASE_NAME="$PROJECT_VERSION"
export RELEASE_BODY=""
printf "Name: %s\nBody: %s\n" $RELEASE_NAME $RELEASE_BODY

popd
echo "Finished generating release."

