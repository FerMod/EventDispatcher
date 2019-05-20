#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

pushd "$(git rev-parse --show-toplevel)"
mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent org.jacoco:jacoco-maven-plugin:report package sonar:sonar -Dsonar.projectKey=com.fermod:event-dispatcher -Dsonar.organization=fermod-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=c84df693aea16826cba562f2e2da63be4d7ca036
popd
