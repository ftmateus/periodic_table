package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.ProcessBuilder.Redirect;
import java.net.URISyntaxException;

public class SystemUtils 
{

    public static final PrintStream debug;
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String TERMINAL;
	public static final String TERMINAL_OPTIONS;
	public static final String OS_SLASH = System.getProperty("file.separator");
	public static final String DEBUG_OUT_FILE = "Debug.txt";
	public static final String APP_LOCATION;

	static 
	{
		debug = setDebugStream();
		
		APP_LOCATION = getAppLocation();

		if (OS_NAME.contains("Windows")) 
		{
			TERMINAL = "cmd.exe";
			TERMINAL_OPTIONS = "/C";
        }
        else
        {
            TERMINAL = "/bin/bash";
            TERMINAL_OPTIONS = "-c";
		}
	}

    public static String getProcessOutput(Process process) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line, res = null;
		while ((line = reader.readLine()) != null)
			res = line;
		return res;
	}

	public static Process runProcessAndWait(String cmd, boolean inheritIO) throws InterruptedException
    {
		debug.println(cmd);
		String[] bCMD = { TERMINAL, TERMINAL_OPTIONS , cmd};

		Process p = null;
        try
        {
			ProcessBuilder pb = new ProcessBuilder(bCMD);
			pb.redirectErrorStream(true); 
			if(inheritIO) 
				pb.redirectOutput(Redirect.appendTo(new File(DEBUG_OUT_FILE)));
            p = pb.start();
            p.waitFor();
        } catch(Exception ex) {
            System.err.println("Failed attempt to run process " + cmd +  " : "+ex);
		}
		return p;
	}

	private static PrintStream setDebugStream()
	{
		if(debug != null)
			throw new Error("Debug stream already defined!");
		PrintStream d;
		try {
			d = new PrintStream(new File(DEBUG_OUT_FILE));
		} catch (FileNotFoundException e) {
			d = null;
		}
		return d;
	}

	private static String getAppLocation()
	{
		String location;
		try {
			location = new File(SystemUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI())
					.getPath();
			int i = location.lastIndexOf('\\');
			location = location.substring(0, i);
		} catch (URISyntaxException e) {
			location = null;
		}
		return location;
	}
}
