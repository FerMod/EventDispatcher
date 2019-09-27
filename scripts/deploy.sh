#!/bin/bash
set -ex # Exit with nonzero exit code if anything fails and show script content

echo "Deploying ..."
mvn deploy -Dregistry=https://maven.pkg.github.com/FerMod -Dtoken=$GITHUB_TOKEN
echo "Finished deploying."
