[Story Maps](http://seanh.github.com/storymaps/) is a story planning and
writing application for children. For screenshots and further information, see
the [Story Maps website](http://seanh.github.com/storymaps/).

Compiling and Running from the Command Line
===========================================

To compile run:

    javac -d build/classes/ -classpath lib/piccolo.jar:lib/piccolox.jar:lib/freemarker.jar:src/ src/storymaps/Application.java

from inside this directory. Then to run the application directly from the class
files do:

    java -classpath lib/piccolo.jar:lib/piccolox.jar:lib/freemarker.jar:src:build/classes/ storymaps.Application

To pack the application up into an executable JAR file do:

    TODO

To build the webstart version of the application:

    TODO

Notes on Building with NetBeans
===============================

(With NetBeans 6.5 on Ubuntu 9.04)

As well as class files NetBeans will build an executable JAR file and (with a
little configuration) a Java Web Start version for you.

If the NetBeans project is setup right NetBeans puts class files and some other
files in build/ and puts an executable JAR file and other files for Java Web
Start in dist/.

To create the NetBeans project from existing sources go to File->New Project and choose Java Project with Existing Sources. Set the top-level folder (the one that contains src/ and lib/) as the project folder. Set lib/ as the libraries folder and src/ as the only folder containing sources.

Once the project is created add the JAR files in lib/ to the Libraries tree in NetBeans.

In the project configuration (Run->Set Project Configuration->Customize) under Application->Web Start, the settings to build the Web Start version are:

Check Enable Web Start  
Codebase: User Defined (e.g. HTTP deployment)  
Codebase Preview: http://homepages.inf.ed.ac.uk/s0094060/webstart/  
(or whatever URL your codebase is located at)  
Check Allow offline  
Check Self-signed  

Make sure that the version of Java requested by the Web Start version is Java 5 and not newer, otherwise it won't work on systems that have Java 5 and are locked down so that Java 6 cannot be installed (e.g. school computers). I believe the option Source/Binary Format under Sources in the Project Properties controls this, it should be set to JDK 5. But if you look inside the `launch.jnlp` file you can see what version of Java it demands, it should have a tag:

    <j2se version="1.5+"/>
