#!/bin/bash
set -ex # Exit with nonzero exit code if anything fails and show script content

setup_git() {
  git config --global user.name "Travis CI"
  git config --global user.email "travis@travis-ci.org"
}

commit_website_files() {

    git checkout master

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
    cd..

    git add pom.xml docs/*
    git commit --message "Travis build: $TRAVIS_BUILD_NUMBER"

}

upload_files() {
    #git push --quiet --set-upstream origin master
}

setup_git
commit_website_files
upload_files
