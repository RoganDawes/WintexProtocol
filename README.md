Introduction
============

This is a very preliminary implementation of the Wintex protocol for Texecom Premier alarm panels. This is NOT based on any NDA-provided documentation, but is purely the result of my personal reverse engineering efforts, starting with an Android app, and exploring further as I went.

Building
========

This is a Java/Maven project, so build and run using:

mvn -q compile exec:java -Dexec.args="hostname port pin" -Djava.util.logging.config.file=${basedir}/logging.properties

To enable verbose protocol logging, you can remove the last -D command line option.

mvn -q compile exec:java -Dexec.args="hostname port pin"

