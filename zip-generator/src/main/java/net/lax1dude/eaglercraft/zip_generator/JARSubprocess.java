package net.lax1dude.eaglercraft.zip_generator;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

// taken from 1.8 buildtools
public class JARSubprocess {
	
	public static final char classPathSeperator;
	
	static {
		classPathSeperator = System.getProperty("os.name").toLowerCase().contains("windows") ? ';' : ':';
	}

	public static int runJava(File directory, String[] javaExeArguments, String logPrefix) throws IOException {
		if(logPrefix.length() > 0 && !logPrefix.endsWith(" ")) {
			logPrefix = logPrefix + " ";
		}
		String javaHome = System.getProperty("java.home");
		if(classPathSeperator == ';') {
			File javaExe = new File(javaHome, "bin/java.exe");
			if(!javaExe.isFile()) {
				javaExe = new File(javaHome, "java.exe");
				if(!javaExe.isFile()) {
					throw new IOException("Could not find /bin/java.exe equivelant on java.home! (java.home=" + javaHome + ")");
				}
			}
			javaHome = javaExe.getAbsolutePath();
		}else {
			File javaExe = new File(javaHome, "bin/java");
			if(!javaExe.isFile()) {
				javaExe = new File(javaHome, "java");
				if(!javaExe.isFile()) {
					throw new IOException("Could not find /bin/java equivelant on java.home! (java.home=" + javaHome + ")");
				}
			}
			javaHome = javaExe.getAbsolutePath();
		}
		
		String[] fullArgs = new String[javaExeArguments.length + 1];
		fullArgs[0] = javaHome;
		System.arraycopy(javaExeArguments, 0, fullArgs, 1, javaExeArguments.length);
		
		ProcessBuilder exec = new ProcessBuilder(fullArgs);
		exec.directory(directory);
		
		exec.redirectOutput(Redirect.INHERIT);
		exec.redirectError(Redirect.INHERIT);
		
		Process ps = exec.start();
		
		while(true) {
			try {
				return ps.waitFor();
			} catch (InterruptedException e) {
			}
		}
	}
	
}
