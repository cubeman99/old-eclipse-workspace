package projects.physicsHomework.labs;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import projects.physicsHomework.LabMath;
import projects.physicsHomework.List;


public class Lab1 {
	
	public static void main(String[] args) {
		
		String filename = "C:/Users/David/Documents/Homework/(14-15) Sophomore/Physics I/lab1_class_data.txt";
		
		ArrayList<List> lists = new ArrayList<List>();
		
		
		try {
			FileReader reader = new FileReader(filename);
			Scanner in = new Scanner(reader);
			
			List currentList = null;
			
			// File read loop.
			while (in.hasNextLine()) {
				String line = in.nextLine();
				
				if (line.length() == 0 || line.charAt(0) == ' ') {
					// Ignore line.
				}
				else if (line.charAt(0) == '#') {
					// Commented.
				}
				else if (line.charAt(0) == '@') {
					// End of file.
					if (line.startsWith("@eof"))
						break;
					
					// End of instruction.
					if (line.startsWith("@end") && currentList != null) {
						lists.add(currentList);
						currentList = null;
					}
					
					// Data set.
					else if (line.startsWith("@dataset"))
						currentList = new List(line.substring("@dataset".length() + 1));
				}
				else if (currentList != null) {
					currentList.append(Double.parseDouble(line));
				}
			}
		}
		catch (IOException e) {
			System.err.println("File not found: \"" + filename + "\"");
		}
		
		double sumMeanOffsets = 0;
		
		for (List list : lists)
		{
			System.out.println("Dataset \"" + list.getName() + "\":");
			
			double mean = list.getAverage();
			sumMeanOffsets = 0;
			for (int i = 0; i < list.size(); i++) {
				double e = list.getElement(i);
				sumMeanOffsets += (e - mean) * (e - mean);
				//System.out.printf("| %2d | %8.2f | %8.2f | %8.2f |%n", i, e, e - mean, (e - mean) * (e - mean));
			}
			//System.out.printf("%n");
			
			System.out.println("sum = " + list.getSum());
			System.out.println("mean = " + list.getAverage());
			System.out.println("standard deviation = " + list.getStandardDeviation());
			System.out.println("sum of mean offsets = " + sumMeanOffsets);
			System.out.printf("%%pe = %f %%%n", (0.1 / mean) * 100);
			System.out.println();
		}
		/*
		double density = lists.get(0).getAverage() / (lists.get(1).getAverage() * lists.get(2).getAverage() * lists.get(3).getAverage());
		System.out.printf("Rectangle Density = %f%n", density * 1000000.0);

		density = lists.get(4).getAverage() / (lists.get(5).getAverage() * lists.get(5).getAverage() * lists.get(6).getAverage() * Math.PI);
		System.out.printf("Cylinder Density = %f%n", density * 1000000.0);

		density = lists.get(7).getAverage() / (lists.get(8).getAverage() * lists.get(8).getAverage() * lists.get(8).getAverage() * Math.PI * (4.0 / 3.0));
		System.out.printf("Sphere Density = %f%n", density * 1000000.0);
		*/
		
		
		// Conversion Factors.
		double GramsToKilograms   = 1000.0;
		double MilimetersToMeters = 1000.0;
		double GMM3toKGM3 = 1e6d;
		

		System.out.println("==============================");
		System.out.println(" (1) CALCULATIONS WITH MEANS");
		System.out.println("==============================");
		
		// RECTANGLE:
		{
    		double mass    = lists.get(0).getAverage();
    		double length  = lists.get(1).getAverage();
    		double width   = lists.get(2).getAverage();
    		double height  = lists.get(3).getAverage();
    		
    		double massPE   = lists.get(0).getStandardDeviation() / mass;
    		double lengthPE = lists.get(1).getStandardDeviation() / length;
    		double widthPE  = lists.get(2).getStandardDeviation() / width;
    		double heightPE = lists.get(3).getStandardDeviation() / height;
    		
    		double volume  = length * width * height;
    		double density = mass / volume;
    		double pe      = LabMath.getDistance(massPE, lengthPE, widthPE, heightPE);
    		double error   = pe * density;
    		
    		System.out.printf("RECTANGLE:%n");
    		System.out.printf("Mass    = %f g %n", mass);
    		System.out.printf("Length  = %f mm %n", length);
    		System.out.printf("Width   = %f mm %n", width);
    		System.out.printf("Height  = %f mm %n", height);
    		System.out.printf("Volume  = %f mm^3 %n", volume);
    		System.out.printf("Density = %f g/mm^3 %n", density);
    		System.out.printf("        = %f kg/m^3 %n", density * GMM3toKGM3);
    		System.out.printf("pe      = %f g/mm^3 %n", error);
    		System.out.printf("        = %f kg/m^3 %n", error * GMM3toKGM3);
    		System.out.printf("%%pe     = %f %% %n", pe * 100.0);
		}
		
		// CYLINDER:
		{
    		double mass    = lists.get(4).getAverage();
    		double radius  = lists.get(5).getAverage() / 2.0;
    		double height  = lists.get(6).getAverage();
    		
    		double massPE   = lists.get(4).getStandardDeviation() / mass;
    		double radiusPE = lists.get(5).getStandardDeviation() / (radius * 2.0);
    		double heightPE = lists.get(6).getStandardDeviation() / height;

    		double volume  = Math.PI * (radius * radius) * height;
    		double density = mass / volume;
    		double pe      = LabMath.getDistance(massPE, radiusPE, radiusPE, heightPE);
    		double error   = pe * density;
    		
    		System.out.printf("%nCYLINDER:%n");
    		System.out.printf("Mass    = %f g %n", mass);
    		System.out.printf("Radius  = %f mm %n", radius);
    		System.out.printf("Height  = %f mm %n", height);
    		System.out.printf("Volume  = %f mm^3 %n", volume);
    		System.out.printf("Density = %f g/mm^3 %n", density);
    		System.out.printf("        = %f kg/m^3 %n", density * GMM3toKGM3);
    		System.out.printf("pe      = %f g/mm^3 %n", error);
    		System.out.printf("        = %f kg/m^3 %n", error * GMM3toKGM3);
    		System.out.printf("%%pe     = %f %% %n", pe * 100.0);
		}
		
		// SPHERE:
		{
    		double mass    = lists.get(7).getAverage();
    		double radius  = lists.get(8).getAverage() / 2.0;
    		
    		double massPE   = lists.get(7).getStandardDeviation() / mass;
    		double radiusPE = lists.get(8).getStandardDeviation() / (radius * 2.0);

    		double volume  = (4.0 / 3.0) * Math.PI * (radius * radius * radius);
    		double density = mass / volume;
    		double pe      = LabMath.getDistance(massPE, radiusPE, radiusPE, radiusPE);
    		double error   = pe * density;
    		
    		System.out.printf("%nSPHERE:%n");
    		System.out.printf("Mass    = %f g %n", mass);
    		System.out.printf("Radius  = %f mm %n", radius);
    		System.out.printf("Volume  = %f mm^3 %n", volume);
    		System.out.printf("Density = %f g/mm^3 %n", density);
    		System.out.printf("        = %f kg/m^3 %n", density * GMM3toKGM3);
    		System.out.printf("pe      = %f g/mm^3 %n", error);
    		System.out.printf("        = %f kg/m^3 %n", error * GMM3toKGM3);
    		System.out.printf("%%pe     = %f %% %n", pe * 100.0);
		}
		
		


		System.out.println("=========================================");
		System.out.println(" (2) CALCULATIONS WITH NEAREST MEASURES");
		System.out.println("=========================================");
		
		double mErr = 0.1; // Measured error (same for all my measurements)
		
		// RECTANGLE:
		{
    		double mass    = lists.get(0).getElementNearestToAverage();
    		double length  = lists.get(1).getElementNearestToAverage();
    		double width   = lists.get(2).getElementNearestToAverage();
    		double height  = lists.get(3).getElementNearestToAverage();
    		
    		double massPE   = mErr / mass;
    		double lengthPE = mErr / length;
    		double widthPE  = mErr / width;
    		double heightPE = mErr / height;
    		
    		double volume  = length * width * height;
    		double density = mass / volume;
    		double pe      = massPE + lengthPE + widthPE + heightPE;
    		double error   = pe * density;
    		
    		System.out.printf("RECTANGLE:%n");
    		System.out.printf("Mass    = %f g %n", mass);
    		System.out.printf("Length  = %f mm %n", length);
    		System.out.printf("Width   = %f mm %n", width);
    		System.out.printf("Height  = %f mm %n", height);
    		System.out.printf("Volume  = %f mm^3 %n", volume);
    		System.out.printf("Density = %f g/mm^3 %n", density);
    		System.out.printf("        = %f kg/m^3 %n", density * GMM3toKGM3);
    		System.out.printf("pe      = %f g/mm^3 %n", error);
    		System.out.printf("        = %f kg/m^3 %n", error * GMM3toKGM3);
    		System.out.printf("%%pe     = %f %% %n", pe * 100.0);
		}
		
		// CYLINDER:
		{
    		double mass    = lists.get(4).getElementNearestToAverage();
    		double radius  = lists.get(5).getElementNearestToAverage() / 2.0;
    		double height  = lists.get(6).getElementNearestToAverage();
    		
    		double massPE   = mErr / mass;
    		double radiusPE = mErr / (radius * 2.0);
    		double heightPE = mErr / height;

    		double volume  = Math.PI * (radius * radius) * height;
    		double density = mass / volume;
    		double pe      = massPE + radiusPE + radiusPE + heightPE;
    		double error   = pe * density;
    		
    		System.out.printf("%nCYLINDER:%n");
    		System.out.printf("Mass    = %f g %n", mass);
    		System.out.printf("Radius  = %f mm %n", radius);
    		System.out.printf("Height  = %f mm %n", height);
    		System.out.printf("Volume  = %f mm^3 %n", volume);
    		System.out.printf("Density = %f g/mm^3 %n", density);
    		System.out.printf("        = %f kg/m^3 %n", density * GMM3toKGM3);
    		System.out.printf("pe      = %f g/mm^3 %n", error);
    		System.out.printf("        = %f kg/m^3 %n", error * GMM3toKGM3);
    		System.out.printf("%%pe     = %f %% %n", pe * 100.0);
		}
		
		// SPHERE:
		{
    		double mass    = lists.get(7).getElementNearestToAverage();
    		double radius  = lists.get(8).getElementNearestToAverage() / 2.0;
    		
    		double massPE   = mErr / mass;
    		double radiusPE = mErr / (radius * 2.0);

    		double volume  = (4.0 / 3.0) * Math.PI * (radius * radius * radius);
    		double density = mass / volume;
    		double pe      = massPE + radiusPE + radiusPE + radiusPE;
    		double error   = pe * density;
    		
    		System.out.printf("%nSPHERE:%n");
    		System.out.printf("Mass    = %f g %n", mass);
    		System.out.printf("Radius  = %f mm %n", radius);
    		System.out.printf("Volume  = %f mm^3 %n", volume);
    		System.out.printf("Density = %f g/mm^3 %n", density);
    		System.out.printf("        = %f kg/m^3 %n", density * GMM3toKGM3);
    		System.out.printf("pe      = %f g/mm^3 %n", error);
    		System.out.printf("        = %f kg/m^3 %n", error * GMM3toKGM3);
    		System.out.printf("%%pe     = %f %% %n", pe * 100.0);
		}
	}
}
