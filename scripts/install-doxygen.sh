#!/bin/bash
set -ex # Exit with nonzero exit code if anything fails and show script content

DOXYGEN_FOLDER_NAME=Release_${DOXYGEN_VERSION//./_}

pushd /tmp

echo "Downloading Doxygen $DOXYGEN_VERSION ..."
wget https://github.com/doxygen/doxygen/archive/$DOXYGEN_FOLDER_NAME.tar.gz -O doxygen-$DOXYGEN_FOLDER_NAME.tar.gz

echo "Uncompresing file ..."
tar -xvf doxygen-$DOXYGEN_FOLDER_NAME.tar.gz

# echo "Cloning Doxygen $DOXYGEN_VERSION repository ..."
# git clone https://github.com/doxygen/doxygen.git doxygen-$DOXYGEN_FOLDER_NAME

echo "Installing Doxygen $DOXYGEN_VERSION ..."
cd doxygen-$DOXYGEN_FOLDER_NAME && cmake -G "Unix Makefiles" && make && sudo make install

# echo "Removing instalation files ..."
# rm -f /tmp/doxygen-$DOXYGEN_FOLDER_NAME.tar.gz
# rm -f -r /tmp/doxygen-$DOXYGEN_FOLDER_NAME

popd
echo "Finished installing doxygen."
