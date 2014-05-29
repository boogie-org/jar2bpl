jar2bpl
=======

Translate java jar files into Boogie programs.

Requirements:
- java version >= 7 


For a quick start, download:
https://github.com/martinschaef/jar2bpl/blob/master/jar2bpl/dist/jar2bpl.jar

Usage: java -jar jar2bpl.jar -j [jar file to translate] -b [output boogie file]



To test the translation, go to the dist sub-directory and execute:

java -jar jar2bpl.jar -j ../lib/log4j-1.2.16.jar -b out.bpl

For the old checker (GraVy) visit:
https://code.google.com/p/jimple2boogie/

For the Boogie parser (boogieamp) visit:
https://code.google.com/p/boogieamp/

A paper containing some details of the translation is available here:
http://iist.unu.edu/sites/iist.unu.edu/files/biblio/soap2013.pdf
