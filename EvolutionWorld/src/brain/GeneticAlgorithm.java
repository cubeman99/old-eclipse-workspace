package brain;

import java.util.ArrayList;
import common.GMath;
import common.Settings;

public class GeneticAlgorithm {
	private ArrayList<Genome> population;
	private int populationSize;
	private int chromoLength;
	private double totalFitness;
	private double bestFitness;
	private double averageFitness;
	private double worstFitness;
	private Genome fittestGenome;
	
	private double mutationRate;
	private double crossoverRate;
	private int generation;
	
	
	
	public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int numWeights) {
		this.populationSize = populationSize;
		this.mutationRate   = mutationRate;
		this.crossoverRate  = crossoverRate;
		this.chromoLength   = numWeights;
		this.totalFitness   = 0;
		this.bestFitness    = 0;
		this.averageFitness = 0;
		this.worstFitness   = 99999999;
		this.generation     = 0;
		this.fittestGenome  = null;
		this.population     = new ArrayList<Genome>();
		
		// Initialize population with chromosomes consisting of random
		// weights and all fitnesses set to zero.
		for (int i = 0; i < populationSize; i++) {
			Genome genome = new Genome();
			
			for (int j = 0; j < chromoLength; j++)
				genome.weights.add(GMath.randomClamped());
			
			population.add(genome);
		}
	}
	
	private Genome[] crossover(Genome mom, Genome dad, Genome baby1, Genome baby2) {
		// Just return parents as offspring dependent on the rate
		// or if parents are the same
		if (GMath.random() > crossoverRate || mom.equals(dad))
			return new Genome[] {mom.getCopy(), dad.getCopy()};
		
		//determine a crossover point
		int cp = GMath.randomInt(chromoLength - 1);
		
		//create the offspring
		for (int i = 0; i < cp; i++) {
			baby1.weights.add(mom.weights.get(i));
			baby2.weights.add(dad.weights.get(i));
		}
		
		for (int i = cp; i < mom.weights.size(); i++) {
			baby1.weights.add(dad.weights.get(i));
			baby2.weights.add(mom.weights.get(i));
		}
		
		return new Genome[] {baby1, baby2};
	}
	
	/** Mutates a chromosome by perturbing its weights by an amount not 
		greater than the max pertubation **/
	private void mutate(ArrayList<Double> chromo) {
		// Traverse the chromosome and mutate each weight dependent
		// on the mutation rate.
		for (int i = 0; i < chromo.size(); i++) {
			// Do we perturb this weight?
			if (GMath.random() < mutationRate) {
				// Add or subtract a small value to the weight
				chromo.set(i, chromo.get(i) + (GMath.randomClamped() * Settings.MAX_PERTURBATION));
			}
		}
	}
	
	private Genome getChromoRoulette() {
		/*
		//generate a random number between 0 & total fitness count
		double Slice = (double) (GMath.random() * totalFitness);

		//this will be set to the chosen chromosome
		Genome theChosenOne = null;

		//go through the chromosones adding up the fitness so far
		double fitnessSoFar = 0;

		for (int i = 0; i < populationSize; i++) {
			fitnessSoFar += population.get(i).fitness;

			// If the fitness so far > random number return the chromo at 
			// this point
			if (fitnessSoFar >= Slice) {
				theChosenOne = population.get(i);
				break;
			}

		}
		*/
		return population.get(GMath.randomInt(populationSize));
	}
	
	private void reset() {
		totalFitness   = 0;
		bestFitness    = 0;
		averageFitness = 0;
		worstFitness   = 99999999;
	}
	
	/** Sort the given population (higher fitness at the end). **/
	private void sort(ArrayList<Genome> pop) {
		for (int i = 0; i < pop.size() - 1; i++) {
			Genome best   = pop.get(i);
			int bestIndex = i;
			
			for (int j = i + 1; j < pop.size(); j++) {
				if (pop.get(j).fitness < best.fitness) {
					best = pop.get(j);
					bestIndex = j;
				}
			}
			
			Genome temp = pop.get(i);
			pop.set(i, best);
			pop.set(bestIndex, temp);
		}
	}
	
	/** This works like an advanced form of elitism by inserting NumCopies
 		copies of the NBest most fittest genomes into a population vector. **/
	private void grabBestGenomes(int numBest, int numCopies, ArrayList<Genome> pop) {
		// Add the required amount of copies of the nnumber of
		// most fittest to the supplied vector.
		
		for (int i = 0; i < numBest; i++) {
			for (int j = 0; i < numCopies; i++)
				pop.add(population.get(population.size() - 1 - i));
		}
	}
	
	/**	Calculates the fittest and weakest genome and the average/total 
		fitness scores. **/
	private void calculateFitnessStats() {
		totalFitness   = 0;
		bestFitness    = 0;
		worstFitness   = 99999999;
		
		for (int i = 0; i < population.size(); i++) {
			double fit = population.get(i).fitness;
			totalFitness += fit;
			
			if (fit > bestFitness)
				bestFitness = fit;
			if (i == 0 || fit < worstFitness)
				worstFitness = fit;
		}
		
		averageFitness = totalFitness / (double) population.size();
		
		System.out.println("Best: " + bestFitness);
		System.out.println("Worst " + worstFitness);
		System.out.println("Avg:  " + averageFitness);
		System.out.println();
	}
	
	public ArrayList<Genome> epoch(ArrayList<Genome> oldPopulation) {
		// Assign the given population to the classes population.
		population = oldPopulation;
		
		// Reset the appropriate variables.
		reset();
		
		// Sort the population (for scaling and elitism).
		sort(population);
		System.out.println("(" + population.get(population.size() - 1).fitness + ")(" + population.get(0).fitness + ")");
		
		// Calculate best, worst, average and total fitness
		calculateFitnessStats();
		
		//create a temporary list to store new chromosones
		ArrayList<Genome> newPopulation = new ArrayList<Genome>();
		
		// Now to add a little elitism we shall add in some copies of the
		// fittest genomes. Make sure we add an EVEN number or the roulette
		// wheel sampling will crash.
		if ((Settings.NUM_COPIES_ELITES * Settings.NUM_ELITES) % 2 == 0) {
			grabBestGenomes(Settings.NUM_ELITES, Settings.NUM_COPIES_ELITES, newPopulation);
		}
		
		// Now we enter the Genetic Algorithm loop.
		
		// Repeat until a new population is generated.
		while (newPopulation.size() < populationSize) {
			// Grab two chromosones.
			Genome mom = getChromoRoulette();
			Genome dad = getChromoRoulette();
			
			// Create some offspring via crossover
			Genome baby1 = new Genome();
			Genome baby2 = new Genome();
			
			Genome[] babies = crossover(mom, dad, baby1, baby2);
			baby1 = babies[0];
			baby2 = babies[1];
			
			// Now we mutate.
			mutate(baby1.weights);
			mutate(baby2.weights);
			
			// Now copy into vecNewPop population.
			newPopulation.add(baby1);
			newPopulation.add(baby2);
		}
		
		// Finished so assign new pop back into m_vecPop
		population = newPopulation;
		
		return population;
	}
	
	public ArrayList<Genome> getPopulation() {
		return population;
	}
}
