<<<<<<< HEAD
#!/bin/bash

# Use the current tag version that will be used for doxygen, and to set the maven version
export PROJECT_NUMBER="$(git describe --tags --abbrev=0)"
mvn --file ../pom.xml versions:set -DnewVersion=${PROJECT_NUMBER#"v"}
mvn --file ../pom.xml clean site

# Generate doxygen docs
cd doxygen
doxygen Doxyfile
=======
#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

# Use the current tag version that will be used for doxygen, and to set the maven version
export PROJECT_NUMBER="$(git describe --tags --abbrev=0)"
mvn versions:set -DnewVersion=${PROJECT_NUMBER#"v"}
mvn clean site

# Generate doxygen docs
cd doxygen
doxygen Doxyfile
>>>>>>> 8934a7001cc33cf80c69b264cd992cbf70ac8296
