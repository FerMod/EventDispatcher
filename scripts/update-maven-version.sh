#!/bin/bash
set -e # Exit with nonzero exit code if anything fails and show script content
pushd "$(git rev-parse --show-toplevel)"
export PROJECT_VERSION="$(git describe --tags --abbrev=0)"
mvn versions:set -DnewVersion=${PROJECT_VERSION#"v"}
popd