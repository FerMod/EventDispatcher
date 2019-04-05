#!/bin/bash
export PROJECT_NUMBER="$(git describe --tags --abbrev=0)"
exec doxygen $@