package projects.physicsHomework.labs;

import java.util.ArrayList;
import cmg.math.GMath;
import projects.physicsHomework.LabMath;
import projects.physicsHomework.lab.*;


public class Lab6 {
	public static void partIndividual()
	{
		LabScriptReader labReader = new LabScriptReader();
		labReader.readScript("C:/Users/David/Documents/Homework/(14-15) Sophomore/Physics I/Lab 6 - Conservation of Mechanical Energy/lab6_data.txt");
		LabData lab = labReader.getLabData();
		
		// Print out the recorded data.
		System.out.println();
		System.out.println("========================");
		System.out.println("       LAB DATA         ");
		System.out.println("========================");
		System.out.println();
		
		lab.printData();

		System.out.println();
		System.out.println("========================");
		System.out.println("      CALCULATIONS      ");
		System.out.println("========================");
		System.out.println();
		
		/*
		// Experiment: Glider
		{
    		Experiment e = lab.getExperiments().get(0);
    		Measure trackLength   = e.getMeasurement("track_length");
    		Measure trackAngle    = e.getMeasurement("track_angle");
    		Dataset setTravelTime = e.getDataset("travel_time");
    		
    		trackAngle.setValue(Math.toRadians(trackAngle.getValue()));
    		trackAngle.setError(Math.toRadians(trackAngle.getError()));
    		
    		Measure travelTime = new Measure("", setTravelTime.getAverage(), setTravelTime.getStandardDeviation(), (Units) null);
    		
			double acceleration = (2.0f * trackLength.getValue()) / LabMath.sqr(travelTime.getValue());
			double accelerationError = LabMath.getDistance(trackLength.getError(), 0.5f * travelTime.getError());
			
			double accX = acceleration * Math.cos(trackAngle.getValue());
			double accY = acceleration * -Math.sin(trackAngle.getValue());

			double cosAngleError = Math.cos(trackAngle.getValue() + trackAngle.getError()) - Math.cos(trackAngle.getValue());
			double sinAngleError = Math.sin(trackAngle.getValue() + trackAngle.getError()) - Math.sin(trackAngle.getValue());

			double accXError = LabMath.getDistance(accelerationError, cosAngleError);
			
			double gravity = acceleration / Math.sin(trackAngle.getValue());
			double gravityError = LabMath.getDistance(accelerationError, sinAngleError);
    		
    		//double gravity = setGravity.getAverage();
    		//double error = setGravity.getStandardDeviation();
    		System.out.printf("Acceleration = %f m/s^2 %n", acceleration);
    		System.out.printf("Error = %f m/s^2 %n", accelerationError);

    		System.out.println();
    		
    		System.out.printf("Acc X = %f m/s^2 %n", accX);
    		System.out.printf("Acc Y = %f m/s^2 %n", accY);
    		System.out.printf("Acc X Error = %f m/s^2 %n", accXError);
    		System.out.printf("Acc Y Error = %f m/s^2 %n", gravityError);

    		System.out.println();
    		
    		System.out.printf("Gravity = %f m/s^2 %n", gravity);
    		System.out.printf("Error = %f m/s^2 %n", gravityError);
    		
    		System.out.println("\n");
		}
		*/
		/*
		char POM = (char) 177; // Plus or minus
		
		// Experiment: Static Friction
		{
    		Experiment e = lab.getExperiments().get(0);
    		
    		for (Dataset set : e.getDatasets()) {
        		System.out.println("DATASET: " + set.getName());
        		
        		double angle = set.getAverage();
        		double angleError = set.getStandardDeviation();
        		double cosf = Math.tan(Math.toRadians(angle));
        		double cosfError = (Math.tan(Math.toRadians(angle + angleError)) - Math.tan(Math.toRadians(angle - angleError))) / 2.0f;
        		

        		System.out.printf("Angle = %.2g %c %.1g degrees %n", angle, POM, angleError);
        		System.out.printf("CoSF  = %.2g %c %.1g %n", cosf, POM, cosfError);
        		System.out.println();
    		}
		}
		
		// Experiment: Force and Motion
		{
    		Experiment e = lab.getExperiments().get(1);
    		Dataset setTime = e.getDataset("travel_time");
    		Measure trackLength = e.getMeasurement("track_length");
    		Measure trackAngle  = e.getMeasurement("track_angle");
    		Measure gliderMass  = e.getMeasurement("glider_mass");
    		double weightMass = 0.050;
    		
    		gliderMass.setValue(0.19252);
    		
    		double time = setTime.getAverage();
    		double timeError = setTime.getStandardDeviation();

			double acceleration = (2.0f * trackLength.getValue()) / LabMath.sqr(time);
			double accelerationError = LabMath.getDistance(trackLength.getError(), 0.5f * timeError);
    		
    		
    		System.out.printf("time = %.4g %c %.1g s %n", time, POM, timeError);
    		System.out.printf("acceleration = %.4g %c %.1g m/s^2 %n", acceleration, POM, accelerationError);
    		
    		double f1 = Math.sin(Math.toRadians(trackAngle.getValue())) * gliderMass.getValue() * 9.81f;
    		double f2 = weightMass * 9.81f;
    		double acc = (f2 - f1) / (gliderMass.getValue() + weightMass);
    		System.out.printf("acceleration = %f m/s^2 %n", acc);
    		
		}
		*/
	}
	
	public static void main(String[] args) {
		System.out.println("INDIVIDUAL DATA:");
		partIndividual();

	}
}
