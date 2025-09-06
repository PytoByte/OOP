#!/bin/bash

# remove previous build
rm -rf build
mkdir -p build/classes build/docs build/jar

# compile classes
javac -d build/classes src/main/java/ru/nsu/vmarkidonov/*.java

# make docs
javadoc -d build/docs -sourcepath src/main/java -subpackages ru.nsu.vmarkidonov

# make jar archive
jar -cf build/jar/SortApp.jar -C build/classes .

# run
java -cp build/jar/SortApp.jar ru.nsu.vmarkidonov.SortMachine
