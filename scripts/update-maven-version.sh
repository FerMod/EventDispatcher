#!/bin/bash
set -ex # Exit with nonzero exit code if anything fails and show script content
export PROJECT_NUMBER="$(git describe --tags --abbrev=0)"
mvn versions:set -DnewVersion=${PROJECT_NUMBER#"v"}