# Event Dispatcher

The general project structure tree is the following, for a more detalled tree go [here](tree.html):
```
.
├── docs
│   └── (...)
├── doxygen
│   ├── Doxyfile
│   ├── html -> ../docs
│   ├── images
│   │   └── (...)
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
│   │   │           └── (...)
│   │   └── resources
│   │       └── log4j2.xml
│   └── test
│       ├── java
│       │   └── com
│       │       └── fermod
│       │           └── (...)
│       └── resources
└── target
    ├── classes
    │   ├── com
    │   │   └── fermod
    │   │       └── (...)
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
                └── (...)
```
