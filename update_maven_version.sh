#!/bin/bash
version="$(git describe --tags --abbrev=0)"
mvn versions:set -DnewVersion=${version#"v"}