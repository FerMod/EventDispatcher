# Event Dispatcher

The project struture tree is the following:
```
.
├── docs
│   └── (...)
├── doxygen
│   ├── customdoxygen.css
│   ├── Doxyfile
│   ├── DoxygenLayout.xml
│   ├── extract_template.sh
│   ├── footer.html
│   ├── header.html
│   ├── html -> ../docs
│   └── latex
│       └── (...)
├── LICENSE
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   └── java
│   │       └── com
│   │           └── fermod
│   │               ├── event
│   │               │   ├── EventPublisher.java
│   │               │   └── ValueChangeListener.java
│   │               ├── logger
│   │               │   └── ConsoleLogger.java
│   │               └── observer
│   │                   └── ObservedValue.java
│   └── test
│       └── java
│           └── com
│               └── fermod
│                   ├── contract
│                   │   ├── ComparableContract.java
│                   │   ├── EqualsContract.java
│                   │   └── Testable.java
│                   ├── data
│                   │   └── serializable
│                   │       └── PersonTest.java
│                   ├── EqualsTest.java
│                   ├── extension
│                   │   └── TimingExtension.java
│                   ├── ObservableValueTest.java
│                   └── util
│                       └── TestUtilities.java
└── target
    ├── classes
    │   ├── com
    │   │   └── fermod
    │   │       ├── event
    │   │       │   ├── EventPublisher.class
    │   │       │   └── ValueChangeListener.class
    │   │       ├── logger
    │   │       │   └── ConsoleLogger.class
    │   │       └── observer
    │   │           └── ObservedValue.class
    │   └── META-INF
    │       ├── MANIFEST.MF
    │       └── maven
    │           └── com.fermod
    │               └── event-dispatcher
    │                   ├── pom.properties
    │                   └── pom.xml
    └── test-classes
        └── com
            └── fermod
                ├── contract
                │   ├── ComparableContract.class
                │   ├── EqualsContract.class
                │   └── Testable.class
                ├── data
                │   └── serializable
                │       └── PersonTest.class
                ├── EqualsTest.class
                ├── extension
                │   └── TimingExtension.class
                ├── ObservableValueTest.class
                └── util
                    └── TestUtilities.class
```
