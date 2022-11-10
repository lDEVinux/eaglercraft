TO QUICKLY MAKE RESOURCE PACK:
   1. make your changes to the files in '/lwjgl-rundir/resources'
   2. double click 'run.bat' on windows, or run './run_unix.sh' in terminal on mac
   3. copy 'assets.epk from '/javascript' to your web directory

To manually use the CompilePackage.jar on a custom directory, run the jar file like this:

java -jar CompilePackage.jar <source directory> <output file>

To recompile the assets.epk file found in /javascript, run:

java -jar CompilePackage.jar "../lwjgl-rundir/resources" "../javascript/assets.epk"