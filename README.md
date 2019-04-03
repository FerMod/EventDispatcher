# Event Dispatcher

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
