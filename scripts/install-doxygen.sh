#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

echo "Downloading Doxygen $DOXYGEN_VERSION ..."
wget https://github.com/doxygen/doxygen/archive/Release_${DOXYGEN_VERSION//./_}.tar.gz -O /tmp/doxygen.tar.gz

echo "Uncompresing file ..."
tar -xvf /tmp/doxygen.tar.gz

echo "Installing Doxygen $DOXYGEN_VERSION ..."
$PWD/doxygen-$DOXYGEN_VERSION && ./configure --prefix=/usr && cmake -G "Unix Makefiles" .. && make && sudo make install

# echo "Cloning Doxygen repository..."
# git clone https://github.com/doxygen/doxygen.git $DOXYGEN_PATH
# cd $DOXYGEN_PATH

# echo "Create build folder..."
# mkdir build
# cd build

# echo "Build Doxygen..."
# cmake -G "Unix Makefiles" ..
# make

# cd $TRAVIS_BUILD_DIR
echo "Finished installing doxygen."
