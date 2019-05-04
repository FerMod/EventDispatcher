#!/bin/bash

sudo apt-get dist-upgrade
sudo apt-get update
sudo apt-get install build-essential
sudo apt-get install doxygen
cd doxygen
bash ./generate_docs.sh
