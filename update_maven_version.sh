#!/bin/bash
version="$(git describe --tags --abbrev=0)"
exec mvn versions:set -DnewVersion=${version#"v"} clean site $@