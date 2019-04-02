#!/bin/bash
tree --noreport --dirsfirst -I "docs|latex|target" -C -H "https://github.com/FerMod/EventDispatcher/tree/master" -T "${PWD##*/} Directory Tree" -o tree.html