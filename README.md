# Event Dispatcher

[![Build Status](https://travis-ci.org/FerMod/EventDispatcher.svg?branch=master)](https://travis-ci.org/FerMod/EventDispatcher)

Event Dispatcher is simple library that offers basic event management.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

This project requires to have **JDK 1.8**, and **Maven** which will manage all the remaining dependencies. **Doxygen** is only required when generating documentation from the annotated sources.

### Installing

To install the library is required to follow a few steps:

1. Go to the project [releases](https://github.com/FerMod/EventDispatcher/releases/latest).
2. Download the `jar` and place it inside your project.
3. Run the following Maven command command replacing `{PATH_TO_FILE}` with the location of the downloaded `jar`:

    ```bash
    mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file -Dfile={PATH_TO_FILE}
    ```

4. Add to your `pom.xml` file the library dependency. Replace `{VERSION}` with the current version:

    ```xml
    <dependency>
        <groupId>com.fermod</groupId>
       <artifactId>event-dispatcher</artifactId>
       <version>{VERSION}</version>
    </dependency>
    ```

    For the version, if the version is `v0.1.5-beta` the value that should be written is `0.1.5beta`.
    Example:

    ```xml
    <dependency>
        <groupId>com.fermod</groupId>
       <artifactId>event-dispatcher</artifactId>
       <version>0.1.5-beta</version>
    </dependency>
    ```

## Running the tests

This project have *JUnit5* tests that are configured to run with maven with the command `mvn test`, the tests can also be run and at the same time generate a report using the maven `mvn site` command.
The repository is configured to use *Travis CI*, that offers continuous integration. It automatically builds and runs tests when pushing to the repository.

## Built With

- [Maven](https://maven.apache.org/) - Dependency Management
- [Doxygen](http://www.doxygen.nl/) - The documentation generation from annotated sources tool
- [JUnit5](https://junit.org/junit5/) - Unit testing framework for the Java
- [TravisCI](https://travis-ci.org/) - Continous integration service

## Contributing

Please read [CONTRIBUTING](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/FerMod/EventDispatcher/tags).

## Authors

* [FerMod](https://github.com/FerMod) **(Ferran Tudela)** - *Initial work*

See also the list of [contributors](https://github.com/FerMod/EventDispatcher/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Changelog

To know more about the changes between versions, you can go to the [CHANGELOG](CHANGELOG.md) file.

The general project structure tree is the following:

```tex
.
├── docs
│   └── (...)
├── doxygen
│   ├── html -> ../docs
│   ├── images
│   │   └── (...)
│   ├── customdoxygen.css
│   ├── Doxyfile
│   ├── DoxygenLayout.xml
│   ├── footer.html
│   └── header.html
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── fermod
│   │   │           └── (...)
│   │   └── resources
│   │       └── log4j2.xml
│   └── test
│       ├── java
│       │   └── com
│       │       └── fermod
│       │           └── (...)
│       └── resources
├── target
│   ├── classes
│   │   ├── com
│   │   │   └── fermod
│   │   │       ├── event
│   │   │       │   └── (...)
│   │   │       └── observer
│   │   │           └── (...)
│   │   ├── META-INF
│   │   │   ├── maven
│   │   │   │   └── com.fermod
│   │   │   │       └── event-dispatcher
│   │   │   │           ├── pom.properties
│   │   │   │           └── pom.xml
│   │   │   └── MANIFEST.MF
│   │   └── log4j2.xml
│   ├── generated-sources
│   │   └── annotations
│   ├── generated-test-sources
│   │   └── test-annotations
│   ├── logs
│   │   └── (...)
│   ├── maven-status
│   │   └── maven-compiler-plugin
│   │       ├── compile
│   │       │   └── default-compile
│   │       │       ├── createdFiles.lst
│   │       │       └── inputFiles.lst
│   │       └── testCompile
│   │           └── default-testCompile
│   │               ├── createdFiles.lst
│   │               └── inputFiles.lst
│   ├── surefire-reports
│   │   ├── com.fermod.ObservableValueTest.txt
│   │   └── TEST-com.fermod.ObservableValueTest.xml
│   └── test-classes
│       └── com
│           └── fermod
│               └── (...)
├── LICENSE
├── pom.xml
└── README.md
```
