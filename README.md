##jar2bpl
=======

[![Build Status](https://travis-ci.org/martinschaef/jar2bpl.png)](https://travis-ci.org/martinschaef/jar2bpl)
[![Coverity Scan](https://scan.coverity.com/projects/5808/badge.svg)](https://scan.coverity.com/projects/5808)
[![Coverage Status](https://coveralls.io/repos/martinschaef/jar2bpl/badge.svg?branch=master)](https://coveralls.io/r/martinschaef/jar2bpl?branch=master) 

Translate java, jar, and apk files into Boogie programs.
For a quick start, download the [jar file](https://github.com/martinschaef/jar2bpl/blob/master/jar2bpl/dist/jar2bpl.jar)

We are currently under construction. The tool still works but the website needs some tlc.

Build the project using:

    gradlew shadowJar
    
This builds a fat jar containing all dependencies. Test the project by translating itself to boogie as follows:

    java -jar build/libs/jar2bpl.jar -j build/classes/main/ -b ouput.bpl

This translation is currently only used by [Bixie](https://github.com/martinschaef/bixie). To use it with Boogie or Corral, a few changes have to be made that are mentioend in the issues list.
