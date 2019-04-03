#!/bin/bash
exec doxygen -l
exec doxygen -w html header.html footer.html customdoxygen.css