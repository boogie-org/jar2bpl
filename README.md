##jar2bpl
=======

Translate java, jar, and apk files into Boogie programs.
For a quick start, download the [jar file](https://github.com/martinschaef/jar2bpl/blob/master/jar2bpl/dist/jar2bpl.jar)

###Requirements:
- java jdk version >= 7 


###Usage: 

####Jar files: 

  java -jar jar2bpl.jar -j [jar file to translate] -b [output boogie file]

For a quick test of the translation, go to the dist sub-directory and execute:

  java -jar jar2bpl.jar -j ../lib/log4j-1.2.16.jar -b out.bpl

####Android files:
To translate Android apps you will need the android-platform jars from the Soot folks. We have a copy of them in jar2bpl_test/data, but the original (and latest) version is available [here](https://github.com/Sable/android-platforms). More information on how apk's are analyzed and papers about the underpinnings are available [here](http://www.abartel.net/dexpler/) and [here](http://www.bodden.de/2013/01/08/soot-android-instrumentation/). If you just want to run the translation use:

  java -jar jar2bpl.jar -j [apk file to translate] -android-jars [path to androids jars] -b [boogie output file]

For a quick test, go to the jar2bpl dirctory and run:

  java -jar ./dist/jar2bpl.jar -j ../jar2bpl_test/regression/android_input/snake.apk -android-jars ../jar2bpl_test/data/ -b ./test.bpl

####Java files:
  java -jar jar2bpl.jar -j [path to java files] -cp [path to java files] -b [output boogie file]

###Advanced options
By default, jar2bpl uses Boogie-assertions to model runtime errors. For example the java procedure:

    public static int foo(MyObject x) {
      return x.value;
    }

is translated into a Boogie procedure like

    implementation foo (x : ref) returns ($return : int) {
      assert x != $null;
      $return := $heap[x, value];
    }
    
This is very convenient when building a safety checker, but not very sound. Actually, if *x* happens to be *null*, Java does not stop the execution of the program (like an assertion would do), but instead creates a RuntimeException and leaves the function before returning a value. This can be achieved by using the **-err** option which encodes runtime exceptions using actual Java exceptions. That is, instead of running:

  java -jar jar2bpl.jar -j ../lib/log4j-1.2.16.jar -b out.bpl

one would run

  java -jar jar2bpl.jar **-err** -j ../lib/log4j-1.2.16.jar -b out.bpl

and the result would look somewhat like:

    //note the additional return parameter!
    implementation foo (x : ref) returns ($return : int, $exception:ref) {
      $exception := null; 
      if (x != $null) {
        $exception := $exception;
      } else {
        //create a new object of type NullPointerException
        havoc $fakelocal_0;
        assume !$heap[$fakelocal_0,$alloc];
        $heap := $heap[$fakelocal_0,$alloc := true];
        assume $fakelocal_0 != $null;
        $heap := $heap[$fakelocal_0,$type := java.lang.NullPointerException];
        $exception := $fakelocal_0;
        return;
      }
      $return := $heap[x, value];
    }

To do safety checking on a program like this, one could, for example, add a postcondition *$exception* must be *null* when leaving the function.

####What to do with Boogie files?
Boogie is an intermediate verification language (IVL) that is understood by a bunch of static analysis tools such as
[GraVy](http://rise4fun.com/GraVy), [Boogie](http://boogie.codeplex.com/), [Corral](http://corral.codeplex.com/), or [Ultimate](https://monteverdi.informatik.uni-freiburg.de/tomcat//Website/?task=VerifyC#). If you prefer **C** over Java, you should check [Smack](https://github.com/smackers/smack).

####Links
For the old checker (GraVy) visit:
https://code.google.com/p/jimple2boogie/

For the Boogie parser (boogieamp) visit:
https://code.google.com/p/boogieamp/

A paper containing some details of the translation is available here:
http://iist.unu.edu/sites/iist.unu.edu/files/biblio/soap2013.pdf

For our infeasible code detection tool Joogie visit:
https://code.google.com/p/joogie/ or http://www.joogie.org

