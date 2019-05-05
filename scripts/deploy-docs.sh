#!/bin/bash
set -ex # Exit with nonzero exit code if anything fails and show script content

# Use the current tag version that will be used for doxygen, and to set the maven version
export PROJECT_NUMBER="$(git describe --tags --abbrev=0)"
mvn --file ../pom.xml versions:set -DnewVersion=${PROJECT_NUMBER#"v"}
mvn --file ../pom.xml clean site

#cd $TRAVIS_BUILD_DIR
#tree --noreport --dirsfirst -I "docs|latex|target|*.sh" -C -H "https://github.com/FerMod/EventDispatcher/tree/master" -T "${PWD##*/} Directory Tree" -o docs/tree.html

# Generate doxygen docs
cd $TRAVIS_BUILD_DIR/doxygen
$DOXYGEN_PATH/build/bin/doxygen Doxyfile

cd $TRAVIS_BUILD_DIR
