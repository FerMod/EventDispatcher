#!/bin/bash
export PROJECT_NUMBER="$(git describe --tags --abbrev=0)"
mvn --file ../pom.xml versions:set -DnewVersion=${PROJECT_NUMBER#"v"}