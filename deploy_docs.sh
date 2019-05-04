#!/bin/bash
apt-get -y install graphviz
apt-get -y install doxygen-gui
cd doxygen
bash ./generate_docs.sh
