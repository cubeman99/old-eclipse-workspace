package projects.physicsHomework.labs;

import java.util.ArrayList;
import cmg.math.GMath;
import projects.physicsHomework.LabMath;
import projects.physicsHomework.lab.*;


public class Lab2 {
	/*
	 * 
	TRACK EXPERIMENT:
	
    Use your kinematic equations to calculate acceleration for
    the air track. Take the mean value from your data and express
    the error using the standard deviation from the norm.
    
    What are the x and y components of acceleration?
    
    
         ~  ~  ~ 
    
    FREEFALL EXPERIMENT:
    
    For the free fall experiment determine g using the time of
    free fall and the final velocity. These are two calculations
    so you should have two results. Use your mean value for time
    and the standard deviation from the norm as the error. 
    
    Compute using only the free fall time the value of g using
    the class data for all free fall drops.
    
    
    
    You will have 110 values of g. Calculate the mean value of
    these results and then use the standard deviation from the
    norm as the error.
    
	*/
	
	public static void partIndividual()
	{
		LabScriptReader labReader = new LabScriptReader();
		labReader.readScript("C:/Users/David/Documents/Homework/(14-15) Sophomore/Physics I/Lab2/lab2_data.txt");
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
		
		// Experiment: Freefall
		{
    		Experiment e = lab.getExperiments().get(1);

    		Measure ballDiameter     = e.getMeasurement("ball_diameter");
    		Measure distance         = e.getMeasurement("freefall_distance");
    		Dataset setFallTime      = e.getDataset("fall_time");
    		Dataset setPhotogateTime = e.getDataset("photogate_pass_time");

    		Measure fallTime   = new Measure("", setFallTime.getAverage(), setFallTime.getStandardDeviation(), (Units) null);
    		Measure photoTime = new Measure("", setPhotogateTime.getAverage(), setPhotogateTime.getStandardDeviation(), (Units) null);
    		
		
			double velocity = ballDiameter.getValue() / photoTime.getValue();
			double velocityError = LabMath.getDistance(ballDiameter.getError(), photoTime.getError());
			
			double gravity = velocity / fallTime.getValue();
			double gravityError = LabMath.getDistance(velocityError, fallTime.getError());

    		
    		System.out.printf("Velocity = %f m/s^2 %n", velocity);
    		System.out.printf("Error = %f m/s^2 %n", velocityError);

    		System.out.println();
    		
    		System.out.printf("Gravity = %f m/s^2 %n", gravity);
    		System.out.printf("Error = %f m/s^2 %n", gravityError);
		}
	}
	
	public static void partClass()
	{
		
	}
	
	public static void main(String[] args) {
		System.out.println("INDIVIDUAL DATA:");
		partIndividual();

		System.out.println("CLASS DATA:");
		partClass();
		
		LabScriptReader labReader = new LabScriptReader();
		labReader.readScript("C:/Users/David/Documents/Homework/(14-15) Sophomore/Physics I/Lab2/lab2_class_data.txt");
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
		

		// Use your kinematic equations to calculate acceleration for
		// the air track. Take the mean value from your data and express
		// the error using the standard deviation from the norm.
		// 
		// What are the x and y components of acceleration?
		
		// ----------------------------
		// x = (1/2)at^2 + x0
		// a = 2(x - x0) / t^2
		// ----------------------------
		// g = a / sin(angle)
		// ----------------------------
		// angle = {track_angle}
		// x0 = 0
		// x  = {track_length}
		// t  = {travel_time}
		// a  = ?
		// g  = ?
		// ----------------------------
		
		System.out.println("    Track Experiment    ");
		System.out.println("    ----------------    ");
		System.out.println();
    	{
    
    		Experiment gliderExp   = lab.getExperiments().get(0);
    
    
    		Dataset setTrackLength = gliderExp.getDataset("track_length");
    		Dataset setTrackAngle  = gliderExp.getDataset("track_angle");
    		Dataset setTravelTime  = gliderExp.getDataset("travel_time");
    		Dataset setGravity = new Dataset("gravity", 0, null);
    		
    		for (int student = 0; student < 11; student++)
    		{
    			double trackLength = setTrackLength.getData(student);
    			double trackAngle  = Math.toRadians(setTrackAngle.getData(student));
    			Dataset subsetTravelTime = setTravelTime.subset(student * 10, 10);
    			
    			for (int trial = 0; trial < 10; trial++)
    			{	
    				double travelTime   = subsetTravelTime.getData(trial);
    				double acceleration = (2.0f * trackLength) / LabMath.sqr(travelTime);
    				double gravity      = acceleration / Math.sin(trackAngle);
    				setGravity.addData(gravity);
    			}
    		}
    		
    		double gravity = setGravity.getAverage();
    		double error = setGravity.getStandardDeviation();
    		System.out.printf("Gravity = %f m/s^2 %n", gravity);
    		System.out.printf("Error = %f m/s^2 %n", error);
    	}
    	

	    // For the free fall experiment determine g using the time of
	    // free fall and the final velocity. These are two calculations
	    // so you should have two results. Use your mean value for time
	    // and the standard deviation from the norm as the error. 
	    
	    // Compute using only the free fall time the value of g using
	    // the class data for all free fall drops.
	    
	    // You will have 110 values of g. Calculate the mean value of
	    // these results and then use the standard deviation from the
	    // norm as the error.

		// ----------------------------
		// v = d / t
		// ----------------------------
		// v = v0 + at
		// a = (v - v0) / t
		// ----------------------------
		// d = {freefall_distance}
		// t = {fall_time}
		// a = ?
		// ----------------------------
		
		System.out.println("\n");
		System.out.println("   Freefall Experiment");
		System.out.println("   -------------------");
		System.out.println();
    	{
    		Experiment freefallExp   = lab.getExperiment("freefall");
    		Dataset setBallDiameter  = freefallExp.getDataset("ball_diameter");
    		Dataset setDistance      = freefallExp.getDataset("freefall_distance");
    		Dataset setFallTime      = freefallExp.getDataset("fall_time");
    		Dataset setPhotogateTime = freefallExp.getDataset("photogate_pass_time");
    		
    		Dataset setGravity = new Dataset("gravity", 0, null);
    		
    		for (int student = 0; student < 11; student++)
    		{
    			double ballDiameter         = setBallDiameter.getData(student);
    			double distance             = setDistance.getData(student);
    			Dataset subsetFallTime      = setFallTime.subset(student * 10, 10);
    			Dataset subsetPhotogateTime = setPhotogateTime.subset(student * 10, 10);
    			
    			for (int trial = 0; trial < 10; trial++)
    			{	
    				double fallTime      = subsetFallTime.getData(trial);
    				double photogateTime = subsetPhotogateTime.getData(trial);
    				double finalVelocity = ballDiameter / photogateTime;
    				double gravity       = finalVelocity / fallTime;
    				setGravity.addData(gravity);
    			}
    		}
    		
    		double gravity = setGravity.getAverage();
    		double error = setGravity.getStandardDeviation();
    		System.out.printf("Gravity = %f m/s^2 %n", gravity);
    		System.out.printf("Error = %f m/s^2 %n", error);
    	}
	}
}
