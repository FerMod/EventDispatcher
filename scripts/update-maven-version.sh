#!/bin/bash
set -ex # Exit with nonzero exit code if anything fails and show script content
export PROJECT_VERSION="$(git describe --tags --abbrev=0)"
mvn versions:set -DnewVersion=${PROJECT_VERSION#"v"}