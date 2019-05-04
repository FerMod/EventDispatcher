#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

# Use the current tag version that will be used for doxygen, and to set the maven version
export PROJECT_NUMBER="$(git describe --tags --abbrev=0)"
mvn versions:set -DnewVersion=${PROJECT_NUMBER#"v"}
mvn clean site

tree --noreport --dirsfirst -I "docs|latex|target|*.sh" -C -H "https://github.com/FerMod/EventDispatcher/tree/master" -T "${PWD##*/} Directory Tree" -o docs/tree.html

# Generate doxygen docs
cd $TRAVIS_BUILD_DIR/doxygen
$DOXYGEN_PATH/build/bin/doxygen Doxyfile

cd $TRAVIS_BUILD_DIR
