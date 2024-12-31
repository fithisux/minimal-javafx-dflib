# minimal-javafx-dflib
This is a simple project to demonstrate the inter-operability of JavaFX and Echarts through DFlib.

It displays a bar chart (the official example).
To build you just need to

```
mvn clean package
```

and to run

```
mvn exec:java "-Dexec.mainClass=org.ntua.trapezoid.TULauncher"
```

Simple as that.

Tested on Windows 10 x64 with the Libera JDK 

```
openjdk version "23.0.1" 2024-10-15
OpenJDK Runtime Environment (build 23.0.1+13)
OpenJDK 64-Bit Server VM (build 23.0.1+13, mixed mode, sharing)
```