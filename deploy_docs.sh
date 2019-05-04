#!/bin/bash

apt-get dist-upgrade
apt-get update
apt-get install build-essential
sudo apt-get install doxygen
cd doxygen
bash ./generate_docs.sh
