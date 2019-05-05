#!/bin/bash
set -e # Exit with nonzero exit code if anything fails and show script content

cd ..

# Use the current tag version that will be used for doxygen, and to set the maven version
echo "Updating Maven version from git tag ..."
export PROJECT_NUMBER="$(git describe --tags --abbrev=0)"
mvn versions:set -DnewVersion=${PROJECT_NUMBER#"v"}

echo "Generating Maven site ..."
mvn clean site

#tree --noreport --dirsfirst -I "docs|latex|target|*.sh" -C -H "https://github.com/FerMod/EventDispatcher/tree/master" -T "${PWD##*/} Directory Tree" -o docs/tree.html

# Generate doxygen docs
echo "Generating Doxygen docs ..."
cd doxygen
doxygen
# cd ..

# git add pom.xml docs/*
# git commit --message "Update docs"
# git push

echo "Deployed master docs ..."
