#!/bin/bash
set -e # Exit with nonzero exit code if anything fails and show script content
pushd $(git rev-parse --show-toplevel)
tree --noreport --dirsfirst -I "docs|latex|target|*.sh" -C -H "https://github.com/FerMod/EventDispatcher/tree/master" -T "${PWD##*/} Directory Tree" -o docs/tree.html
popd