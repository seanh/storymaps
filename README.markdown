Story Maps is a [constructionist][] story planning and writing tool for children. The idea is to construct a map of a story that you want to write by combining story cards (or 'Propp cards'). You then use the map as an aid to write your story. It is based on ideas from [Vladimir Propp][]'s _Morphology of the Folktale_ and [Gianni Rodari][]'s _The Grammar of Fantasy_. We hope that the application will be a powerful tool for learning about stories and their construction, and will enable children to learn to write logically and artistically satisfying stories of their own. The current application is a prototype that I have created as a research tool for my PhD.

[constructionist]: http://en.wikipedia.org/wiki/Constructionist_learning
[Vladimir Propp]: http://en.wikipedia.org/wiki/Vladimir_Propp
[Gianni Rodari]: http://en.wikipedia.org/wiki/Gianni_Rodari

Click on the screenshot to launch the application with Java Web Start (requires Java version 1.5 or later).

<a href="http://homepages.inf.ed.ac.uk/s0094060/webstart/launch.jnlp"><img src="http://homepages.inf.ed.ac.uk/s0094060/webstart/screenshot.png"  alt="StoryMaps prototype screenshot" title="Go on, click!" style="border:None;" /></a>

For more info see the publications on [my university website](http://homepages.inf.ed.ac.uk/s0094060).

Compiling and Running from the Command Line
===========================================

To compile run:

    javac -d build/classes/ -classpath lib/piccolo.jar:lib/piccolox.jar:lib/freemarker.jar:src/ src/storymaps/Application.java

from inside this directory. Then to run the application directly from the class files do:

    java -classpath lib/piccolo.jar:lib/piccolox.jar:lib/freemarker.jar:src:build/classes/ storymaps.Application

To pack the application up into an executable JAR file do:

    TODO

To build the webstart version of the application:

TODO

Notes on Building with NetBeans
===============================

(With NetBeans 6.5 on Ubuntu 9.04)

As well as class files NetBeans will build an executable JAR file and (with a little configuration) a Java Web Start version for you.

If the NetBeans project is setup right NetBeans puts class files and some other files in build/ and puts an executable JAR file and other files for Java Web Start in dist/.

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