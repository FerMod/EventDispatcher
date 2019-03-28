# Event Dispatcher

The project struture tree is the following:
```
.
├── docs
│   └── (...)
├── doxygen
│   ├── customdoxygen.css
│   ├── Doxyfile
│   ├── DoxygenLayout.xml
│   ├── extract_template.sh
│   ├── footer.html
│   ├── header.html
│   ├── html -> ../docs
│   ├── images
│   └── latex
│       └── (...)
├── LICENSE
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── fermod
│   │   │           ├── event
│   │   │           │   ├── EventPublisher.java
│   │   │           │   └── ValueChangeListener.java
│   │   │           └── observer
│   │   │               └── ObservedValue.java
│   │   └── resources
│   │       └── log4j2.xml
│   └── test
│       ├── java
│       │   └── com
│       │       └── fermod
│       │           ├── contract
│       │           │   ├── ComparableContract.java
│       │           │   ├── EqualsContract.java
│       │           │   └── Testable.java
│       │           ├── data
│       │           │   └── serializable
│       │           │       └── PersonTest.java
│       │           ├── EqualsTest.java
│       │           ├── extension
│       │           │   └── TimingExtension.java
│       │           ├── Log4jTest.java
│       │           ├── ObservableValueTest.java
│       │           └── util
│       │               └── TestUtilities.java
│       └── resources
└── target
    ├── classes
    │   ├── com
    │   │   └── fermod
    │   │       ├── event
    │   │       │   ├── EventPublisher.class
    │   │       │   └── ValueChangeListener.class
    │   │       └── observer
    │   │           └── ObservedValue.class
    │   ├── log4j2.xml
    │   └── META-INF
    │       ├── MANIFEST.MF
    │       └── maven
    │           └── com.fermod
    │               └── event-dispatcher
    │                   ├── pom.properties
    │                   └── pom.xml
    ├── logs
    └── test-classes
        └── com
            └── fermod
                ├── contract
                │   ├── ComparableContract.class
                │   ├── EqualsContract.class
                │   └── Testable.class
                ├── data
                │   └── serializable
                │       └── PersonTest.class
                ├── EqualsTest.class
                ├── extension
                │   └── TimingExtension.class
                ├── Log4jTest.class
                ├── ObservableValueTest.class
                └── util
                    └── TestUtilities.class
```
