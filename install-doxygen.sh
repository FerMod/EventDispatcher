#!/bin/sh
set -e # Exit with nonzero exit code if anything fails

echo "Cloning doxygen repository..."
git clone https://github.com/doxygen/doxygen.git $DOXYGEN_PATH
cd $DOXYGEN_PATH

echo "Create build folder..."
mkdir build
cd build

echo "Build doxygen..."
cmake -G "Unix Makefiles" ..
make

export PATH="$PATH:$DOXYGEN_PATH/bin"

cd $TRAVIS_BUILD_DIR
echo "Finished installing doxygen."
