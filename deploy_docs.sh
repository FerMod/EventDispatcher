#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

# Use the current tag version that will be used for doxygen, and to set the maven version
export PROJECT_NUMBER="$(git describe --tags --abbrev=0)"
mvn --file ../pom.xml versions:set -DnewVersion=${PROJECT_NUMBER#"v"}
mvn --file ../pom.xml clean site

# Generate doxygen docs
cd doxygen
doxygen Doxyfile