#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

tagname="$(git describe --tags --abbrev=0)"
git push --delete origin $tagname
git tag --delete $tagname
git tag $tagname
