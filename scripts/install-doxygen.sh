#!/bin/bash
set -ex # Exit with nonzero exit code if anything fails and show script content

DOXYGEN_FOLDER_NAME=Release_${DOXYGEN_VERSION//./_}

echo "Downloading Doxygen $DOXYGEN_VERSION ..."
wget https://github.com/doxygen/doxygen/archive/$DOXYGEN_FOLDER_NAME.tar.gz -O /tmp/doxygen-$DOXYGEN_FOLDER_NAME.tar.gz

echo "Uncompresing file ..."
tar -xvf /tmp/doxygen-$DOXYGEN_FOLDER_NAME.tar.gz

echo "Installing Doxygen $DOXYGEN_VERSION ..."
pushd doxygen-$DOXYGEN_FOLDER_NAME && cmake -G "Unix Makefiles" && make && sudo make install

# echo "Cloning Doxygen $DOXYGEN_VERSION repository ..."
# git clone https://github.com/doxygen/doxygen.git doxygen-$DOXYGEN_FOLDER_NAME
# pushd doxygen-$DOXYGEN_FOLDER_NAME

# echo "Installing Doxygen $DOXYGEN_VERSION ..."
# mkdir build
# cd build && cmake -G "Unix Makefiles" && make && sudo make install

echo "Removing instalation files ..."
cd ..
rm /tmp/doxygen-$DOXYGEN_FOLDER_NAME.tar.gz
rm -r doxygen-$DOXYGEN_FOLDER_NAME

popd
echo "Finished installing doxygen."
