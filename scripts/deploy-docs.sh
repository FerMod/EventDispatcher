#!/bin/bash
set -ex # Exit with nonzero exit code if anything fails and show script content

echo "Deploying docs ..."
pushd $TRAVIS_BUILD_DIR

# Use the current tag version that will be used for doxygen, and to set the maven version
echo "Updating Maven version from git tag ..."
export PROJECT_VERSION="$(git describe --tags --abbrev=0)"
mvn versions:set -DnewVersion=${PROJECT_VERSION#"v"}

echo "Generating Maven site ..."
mvn clean site

#tree --noreport --dirsfirst -I "docs|latex|target|*.sh" -C -H "https://github.com/FerMod/EventDispatcher/tree/master" -T "${PWD##*/} Directory Tree" -o docs/tree.html

# Generate doxygen docs
echo "Generating Doxygen docs ..."
cd doxygen
doxygen

popd
echo "Finished deploying docs."

