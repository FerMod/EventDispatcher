#!/bin/bash
set -ex # Exit with nonzero exit code if anything fails and show script content

DOXYGEN_VERSION=1.8.15

echo "Downloading Doxygen $DOXYGEN_VERSION ..."
wget https://github.com/doxygen/doxygen/archive/Release_${DOXYGEN_VERSION//./_}.tar.gz -O /tmp/doxygen-$DOXYGEN_VERSION.tar.gz

echo "Uncompresing file ..."
tar -xvf /tmp/doxygen-$DOXYGEN_VERSION.tar.gz

# echo "Cloning Doxygen repository ..."
# git clone https://github.com/doxygen/doxygen.git doxygen-$DOXYGEN_VERSION
# pushd $DOXYGEN_PATH

echo "Installing Doxygen $DOXYGEN_VERSION ..."
pushd doxygen-$DOXYGEN_VERSION && ./configure --prefix=/usr && cmake -G "Unix Makefiles" .. && make && sudo make install

# echo "Building Doxygen ..."
# mkdir build
# cd build && ./configure --prefix=/usr && cmake -G "Unix Makefiles" .. && make && sudo make install

popd
echo "Finished installing doxygen."
