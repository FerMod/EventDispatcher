# Event Dispatcher

[![Build Status](https://travis-ci.org/FerMod/EventDispatcher.svg?branch=master)](https://travis-ci.org/FerMod/EventDispatcher) [![Sonar Quality Gate](https://img.shields.io/sonar/https/sonarcloud.io/com.fermod:event-dispatcher/quality_gate.svg)](https://sonarcloud.io/dashboard?id=com.fermod%3Aevent-dispatcher) [![Sonar Coverage](https://img.shields.io/sonar/https/sonarcloud.io/com.fermod:event-dispatcher/coverage.svg)](https://sonarcloud.io/dashboard?id=com.fermod%3Aevent-dispatcher) [![GitHub release](https://img.shields.io/github/release-pre/FerMod/EventDispatcher.svg)](https://github.com/FerMod/EventDispatcher/releases)

Event Dispatcher is simple library that offers basic event management.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See the section [Using the Library](#using-the-library) for notes on how to use the library and how to add custom events.

### Prerequisites

This project requires to have **JDK 1.8**, and **Maven** which will manage all the remaining dependencies. **Doxygen** is only required when generating documentation from the annotated sources.

### Installing



To install the library is required to follow a few steps:

1. Go to the project [releases](https://github.com/FerMod/EventDispatcher/releases/latest).
2. Download the `jar` and place it inside your project.
3. Run the following Maven command replacing `PATH_TO_JAR` with the location of the downloaded `jar`:

    ```bash
    mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file -Dfile=PATH_TO_JAR
    ```

4. Add to your `pom.xml` file the library dependency, it should look similar to the example below. Replace `VERSION` with the current version. If the version is `v0.1.5-beta` the value that should be replaced with is `0.1.5-beta`.

    ```xml
    <dependency>
        <groupId>com.fermod</groupId>
        <artifactId>event-dispatcher</artifactId>
        <version>VERSION</version>
    </dependency>
    ```

## Using the Library

> **WIP** section

The events can be added using the class `EventPublisher`, this class needs to be extended in order to use the methods that allow to register and fire the events. The class `ObservedValue` shows a simple implementation and is a class ready to use. 

The class `ObservedValue` extends `EventPublisher` and uses the methods declared in the interface `ValueChangeListener`.

```java
public class ObservedValue<T> extends EventPublisher<ValueChangeListener<T>> implements Serializable {
    (...)
}
```

![diagram](https://raw.githubusercontent.com/FerMod/EventDispatcher/FosterGun/src/main/resources/EventDisp_Diagram.png)

When the events need to be fired the class only need to notify the listeners. For example, in the `set` method is notified to the listeners that the value have changed, and the old and new values are passed as parameters, that will obtain the registered listeners.

```java
public void set(T value) {

    T oldValue = this.value;
    this.value = value;

    // Notify the list of registered listeners
    this.notifyListeners(listener -> listener.onValueChanged(oldValue, value));

}
```

To use the `ObservedValue` class first, it needs to be imported.

```java
import com.fermod.observer.ObservedValue;
```

When registering the listeners, it can be done inline or by passing a reference to the method.

```java
ObservedValue<Integer> observedValue = new ObservedValue<>();

// Inline listener registration using lambda
observedValue.registerListener((oldVAlue, newValue) -> {
    System.out.println("Value changed, executed lamda inline code. Old value: " + oldVAlue + " New value: " + newValue);
});

// Equivalent inline listener registration of the lambda
observedValue.registerListener(new ValueChangeListener<Integer>() {
    @Override
    public void onValueChanged(Integer oldValue, Integer value) {
        System.out.println("Value changed, executed inline code. Old value: " + oldValue + " New value: " + value);
    }
});
```

A method reference can be used to register as listener.

```java
// Register a mothod using method references
ObservedValue<Integer> observedValue = new ObservedValue<>(0);
observedValue.registerListener(this::printValueChange);
```

The method that is referenced is the following:

```java
public void printValueChange(int oldValue, int newValue) {
    System.out.println("Value changed, executed printValueChange method. Old value: " + oldValue + " New value: " + newValue);
}
```

All the last methods would be called when using the `set` method, and each of them would execute its content.

```java
import com.fermod.event.ValueChangeListener;
import com.fermod.observer.ObservedValue;

public class Examples {

    public static void main(String[] args) {

        ObservedValue<Integer> observedValue = new ObservedValue<>(0);

        // Inline listener registration using lambda
        observedValue.registerListener((oldVAlue, newValue) -> {
            System.out.println("Executed lambda inline code. Old value: " + oldVAlue + " New value: " + newValue);
        });

        // Equivalent inline listener registration of the lambda
        observedValue.registerListener(new ValueChangeListener<Integer>() {
            @Override
            public void onValueChanged(Integer oldValue, Integer value) {
                System.out.println("Executed inline code. Old value: " + oldValue + " New value: " + value);
            }
        });

        // Register a mothod using method references
        observedValue.registerListener(Examples::printValueChange);

        System.out.println("Change value to 10.");
        observedValue.set(10);

    }

    public static void printValueChange(int oldValue, int newValue) {
        System.out.println("Executed printValueChange method. Old value: " + oldValue + " New value: " + newValue);
    }

}
```

The console output generated by this example would be:

```text
Change value to 10
Executed lambda inline code. Old value: 0 New value: 10
Executed inline code. Old value: 0 New value: 10
Executed printValueChange method. Old value: 0 New value: 10
```

The full example class can be found [here](https://github.com/FerMod/EventDispatcher/blob/master/src/test/java/com/fermod/example/Examples.java).

## Running the tests

This project have *JUnit5* tests that are configured to run with maven with the command `mvn test`, the tests can also be run and at the same time generate a report using the maven `mvn site` command.
The repository is configured to use *Travis CI*, that offers continuous integration. It automatically builds and runs tests when pushing to the repository.

## Built With

- [Maven](https://maven.apache.org/) - Dependency management
- [JUnit5](https://junit.org/junit5/) - Unit testing framework for Java
- [TravisCI](https://travis-ci.org/) - Continous integration service
- [Doxygen](http://www.doxygen.nl/) - The documentation generation from annotated sources tool

## Contributing

Please read [CONTRIBUTING](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/FerMod/EventDispatcher/tags).

## Authors

- [FerMod](https://github.com/FerMod) **(Ferran Tudela)** - *Initial work*

See also the list of [contributors](https://github.com/FerMod/EventDispatcher/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Changelog

To know more about the changes between versions, you can go to the [CHANGELOG](CHANGELOG.md) file.

---

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
