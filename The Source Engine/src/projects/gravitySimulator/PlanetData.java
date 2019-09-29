package projects.gravitySimulator;

import java.awt.Color;
import java.util.ArrayList;
import common.GMath;
import common.Vector;
import main.ImageLoader;

public class PlanetData {
	public static GravitySimulator simulator;
	
	public static final double MASS_OF_EARTH   = 5973600000000000000000000.0; // kg
	public static final double RADIUS_OF_EARTH = 6378.1; // km
	
	public static ArrayList<Mass> planets;
	public static Mass sun;
	public static Mass mercury;
	public static Mass venus;
	public static Mass earth;
	public static Mass moon;
	public static Mass mars;
	public static Mass phobos;
	public static Mass deimos;
	public static Mass jupiter;
	public static Mass saturn;
	public static Mass uranus;
	public static Mass neptune;
	public static Mass pluto;
	
	
	public static void createSolarSystem(GravitySimulator simulator) {
		PlanetData.simulator = simulator;
		planets = new ArrayList<Mass>();
		sun		= createPlanet("Sun", 109, 333000, 0, 0);
		
		mercury	= createPlanet("Mercury", 0.3829, 0.055, 9089.4836, 47.87);
		venus	= createPlanet("Venus", 0.9499, 0.815, 16984.4608, 35.02);
		earth	= createPlanet("Earth", 1, 1, 23481.40009, 29.78);
		mars	= createPlanet("Mars", 0.533, 0.107, 35777.6016, 24.077);
		jupiter	= createPlanet("Jupiter", 10.9733, 317.8, 122201.72657, 13.07);
		saturn	= createPlanet("Saturn", 9.4492, 95.152, 224995.97708, 9.69);
		uranus	= createPlanet("Uranus", 4.007, 14.536, 451527.08868, 6.81);
		neptune	= createPlanet("Neptune", 3.883, 17.147, 706866.05886, 5.43);
		pluto	= createPlanet("Pluto", 0.18, 0.00218, 921990.2684, 4.7 + 3);
		
		saturn.setImage("Saturn", true, 217);
		
		moon   = createMoon("Moon", earth, 0.273, 0.0123, 60.2687, 1.022);
		
		
		// MARS:
//		createMoon("Moon", mars, 0.0021, 0.00000001794562, 1.47185, 2.138); // Phobos
//		createMoon("Moon", mars, 0.00097, 0.00000000025, 3.68231, 1.35); // Deimos
		createMoonFromOrbit("Phobos", mars, 0.0021, 0.00000001794562, 9377.2, 0.31891023);
		createMoonFromOrbit("Deimos", mars, 0.00097, 0.00000000025, 23460, 1.26244);
		
		// JUPITER:
		createMoonFromOrbit("Io", jupiter, 0.286, 0.015, 421700, 1.769137786);
		createMoonFromOrbit("Europa", jupiter, 0.245, 0.008, 670900, 3.551181);
		createMoonFromOrbit("Ganymede", jupiter, 0.413, 0.025, 1070400, 7.15455296);
		createMoonFromOrbit("Callisto", jupiter, 0.378, 0.018, 1882700, 16.6890184);
		
		// SATURN:
		createMoonFromOrbit("Titan", saturn, 0.404, 0.0225, 1221870, 15.945);
		
		// URANUS:
		createMoonFromOrbit("Titania", uranus, 0.1235, 0.0005908, 435910, 8.706234);
		createMoonFromOrbit("Ariel", uranus, 0.0908, 0.000226, 190900, 2.520);
		createMoonFromOrbit("Miranda", uranus, 0.03697, 0.00001103, 129390, 1.413479);
		createMoonFromOrbit("Umbriel", uranus, 0.092 , 0.00002, 266000, 4.144);
		
		// NEPTUNE:
		createMoonFromOrbit("Triton", neptune, 0.2122, 0.00359, 354759, -5.876854);
		
		
		
//		sun.dynamic = false;
	}
	
	public static Mass createPlanet(String name, double radius, double mass, double distance, double velocity) {
		Mass m = simulator.addMass(new Vector(distance, 0), radius, new Vector(0, -(velocity / (6378.1 * 60.0)) * GravitySimulator.SCALE));
		m.mass = mass;
		m.color   = new Color(200, 200, 200);
		m.setImage(ImageLoader.getImage(name.toLowerCase()));
		m.setName(name);
		planets.add(m);
		return m;
	}
	
	public static Mass createMoon(String name, Mass planet, double radius, double mass, double distance, double velocity) {
		Mass m = simulator.addMass(planet.getPosition().plus(new Vector(distance, 0)), radius, new Vector(0, -(velocity / (6378.1 * 60.0)) * GravitySimulator.SCALE).plus(planet.getVelocity()));
		m.mass = mass;
		m.color   = new Color(200, 200, 200);
		m.setName(name);
		if (ImageLoader.getImage(name.toLowerCase()) == null)
			m.setImage(ImageLoader.getImage("moon"));
		else
			m.setImage(ImageLoader.getImage(name.toLowerCase()));
		return m;
	}

	
	public static Mass createMoonFromOrbit(String name, Mass planet, double radius, double mass, double kmDistance, double orbitDaysPeriod) {
		double circumference = kmDistance * GMath.PI * 2.0;
		double velocity      = circumference / (orbitDaysPeriod * 24.0 * 3600.0);
		return createMoon(name, planet, radius, mass, kmDistance / 6378.1, velocity);
	}
}
