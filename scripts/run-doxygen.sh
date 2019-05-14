#!/bin/bash
set -ex # Exit with nonzero exit code if anything fails and show script content
pushd $(git rev-parse --show-toplevel)/doxygen
export PROJECT_VERSION="$(git describe --tags --abbrev=0)"
doxygen Doxyfile
popd