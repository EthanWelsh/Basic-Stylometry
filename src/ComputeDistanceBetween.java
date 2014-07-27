import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Ethan Welsh
 * George Novaky (1501)
 * September 5th, 2013
 * Angle Between Two File
 */

public class ComputeDistanceBetween 
{
	public static void main (String args[])
	{
		StopWatch watch = new StopWatch();
		watch.start();

		if(args.length < 2 || args.length > 2) 
		{ // If there aren't the right amount of arguments...
			System.out.println("Usage: java ComputeDistanceBetween filename_1 filename_2");
			return;
		}

		HashMap<String, Double> x = populateHashMap(args[0]);
		HashMap<String, Double> y = populateHashMap(args[1]);

		if(x == null || y == null) 
		{ // If one or both of the files cannot be found.
			if(x==null) System.out.println("File " + args[0] + ": 0 lines, 0 words, 0 distinct words");
			if(y==null) System.out.println("File " + args[1] + ": 0 lines, 0 words, 0 distinct words");
			System.out.println("The distance between the documents is: NaN radians");
			watch.stop();
			System.out.println("Time elapsed: "+ watch.getTimeSecs() + " seconds");
			return;
		} else if (args[0].equals(args[1])) {
			System.out.println("The distance between the documents is: 0 radians");
			watch.stop();
			System.out.println("Time elapsed: "+ watch.getTimeSecs() + " seconds");
			return;
		}

		double innerProduct = 0;
		double normX = 0;
		double normY = 0;

		for(String s : x.keySet()) normX = normX + (x.get(s) * x.get(s)); // Calculate the norm of X by going through each unique word in a keySet 
		for(String s : y.keySet()) normY = normY + (y.get(s) * y.get(s)); // and multiplying the frequency by itself, then add that to the sum.

		normX = Math.sqrt(normX); // Take the sqrt...
		normY = Math.sqrt(normY);

		for(String s : x.keySet()) if(y.keySet().contains(s)) innerProduct = innerProduct + (x.get(s) * y.get(s)); // Calculate the innerProduct for the words that are shared between files.

		double a = Math.acos(innerProduct / (normX*normY));
		System.out.println("The distance between the documents is: " + a + " radians");
		watch.stop();
		System.out.println("Time elapsed: "+ watch.getTimeSecs() + " seconds");
	}

	public static HashMap<String, Double> populateHashMap(String fName) 
	{
		try {
			File f = new File(fName);
			BufferedReader br = new BufferedReader(new FileReader(f));
			String sCurrentLine;
			HashMap<String, Double> dictHash = new HashMap<String, Double>();
			int line = 0;
			int words = 0;

			while ((sCurrentLine = br.readLine()) != null) 
			{
				line++;
				sCurrentLine = sCurrentLine.toLowerCase();
				for(char c : sCurrentLine.toCharArray()) if(((int) c >= 65 && (int) c <= 90 || (int) c >= 97 && (int) c <= 122 || (int) c >= 48 && (int)c <= 57) == false) sCurrentLine = sCurrentLine.replace(c, ' '); // If it isn't a letter/number, replace it with a space
				
			    Pattern pattern = Pattern.compile("\\W+"); // Replace all recurring whitespace with a single space.
			    Matcher matcher = pattern.matcher(sCurrentLine);
			    sCurrentLine = matcher.replaceAll(" ");
				String[] apple = sCurrentLine.split(" "); // Split on said space				
			
				for(String x : apple) // For each word in that line
				{	
					if(dictHash.get(x) != null) 
					{
						dictHash.put(x, dictHash.get(x) + 1.0); // If the word is already in dict, increment counter
						words++;
					}
					else if(x.length() != 0) 
					{
						dictHash.put(x, 1.0); // add it
						words++;
					}
				}
			}
			br.close();
			System.out.println("File " + fName + ": " + line + " lines, " + words + " words, " + dictHash.keySet().size() + " distinct words");
			return dictHash;			
		} catch (IOException e) {
			System.out.println(fName + " is not found.");
			return null;
		}
	}
}

class StopWatch 
{
	private double startTime = 0;
	private double stopTime = 0;
	private boolean flag = false;

	public void start() 
	{
		startTime = System.currentTimeMillis();
		flag = true;
	}

	public void stop() 
	{
		stopTime = System.currentTimeMillis();
		flag = false;
	}

	public double getTime() 
	{
		double elapsedTime;
		if (flag) elapsedTime = (System.currentTimeMillis()-startTime);
		else elapsedTime = (stopTime-startTime); 
		return elapsedTime;
	}

	public double getTimeSecs() 
	{
		double elapsed;
		if (flag) elapsed = ((System.currentTimeMillis()-startTime)/1000);
		else elapsed = ((stopTime - startTime) / 1000);
		return elapsed;
	}
}