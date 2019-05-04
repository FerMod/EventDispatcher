#!/bin/bash
sudo apt-get -y install graphviz
sudo apt-get -y install doxygen-gui
cd doxygen
bash ./generate_docs.sh
