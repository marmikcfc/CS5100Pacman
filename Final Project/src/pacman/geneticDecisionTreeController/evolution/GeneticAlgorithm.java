package pacman.geneticDecisionTreeController.evolution;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;        // for generating random numbers
import java.util.ArrayList;     // arrayLists are more versatile than arrays

import pacman.Executor;
import pacman.geneticDecisionTreeController.evolution.Gene;
import pacman.controllers.examples.Legacy;
import pacman.controllers.examples.StarterGhosts;

/**
 *
 * @author Marmik
 */


  /**
   * Genetic algorithm for the best input to the decision tree for decision making parameters
   */

public class GeneticAlgorithm {

    static int CHROMOSOME_SIZE=5;
    static int POPULATION_SIZE=100;


    /**
     * The population contains an ArrayList of genes (the choice of arrayList over
     * a simple array is due to extra functionalities of the arrayList, such as sorting)
     */
    ArrayList<Gene> mPopulation;
    Random rand;
    
    public int optimalFitness;
    public int MAX_GEN;
    public int PAIRINGS;
    public double MUTATIONCHANCE;
    public int MUTA_VARI;
    public int currentGen;


    /**
     * Creates the starting population of Gene classes, whose chromosome contents are random
     * The size of the popultion is passed as an argument from the main class
     */
    public GeneticAlgorithm(int size){
        // initialize the arraylist and each gene's initial weights HERE
        mPopulation = new ArrayList<Gene>();
        for(int i = 0; i < size; i++){
            Gene entry = new Gene();
            entry.randomizeChromosome();
            mPopulation.add(entry);
        }
        
        rand = new Random();
        
        optimalFitness = Integer.MAX_VALUE;
        
        MAX_GEN = 20;
        PAIRINGS = POPULATION_SIZE/4;
        MUTATIONCHANCE = 0.7;
        MUTA_VARI = POPULATION_SIZE/20;
        
    }
    
    public void setPairings(){
    	PAIRINGS = (int) Math.round(POPULATION_SIZE*(
    			((double)(currentGen/2)+5)/
    			(double)(MAX_GEN+5))
    			/2);
    }
    /**
     * For all members of the population, runs a heuristic (which is average of the score in 10
     * trials for every generation) that evaluates their fitness based on their phenotype.
     * The evaluation of this problem's phenotype is fairly simple,
     * and can be done in a straightforward manner. In other cases, such as agent
     * behavior, the phenotype may need to be used in a full simulation before getting
     * evaluated (e.g based on its performance)
     */
    public void evaluateGeneration(){
        for(int i = 0; i < mPopulation.size(); i++){
        	Gene g = mPopulation.get(i);
        	geneticDecisionTree tree = new geneticDecisionTree(g.getChromosomeElement(0), g.getChromosomeElement(1), g.getChromosomeElement(2));
            Executor exec = new Executor();
            g.setFitness((float) exec.runExperiment(tree, new Legacy(), 10));
        }
    }
    /**
     * With each gene's fitness as a guide, chooses which genes should mate and produce offspring.
     * The offspring are added to the population, replacing the previous generation's Genes either
     * partially or completely. The population size, however, should always remain the same.
     * If you want to use mutation, this function is where any mutation chances are rolled and mutation takes place.
     */
    public void mutate(){
    	mPopulation.sort(new Comparator<Gene>(){public int compare(Gene x, Gene y){return  (int) (x.mFitness - y.mFitness);}});
    	List<Gene> breeders = mPopulation.subList(PAIRINGS*2, mPopulation.size());
    	ArrayList<Gene> totalOffspring = new ArrayList<Gene>();
    	for(int i = 0; i<PAIRINGS; i++){
    		//get two random breeders
    		int motherIndex = rand.nextInt(breeders.size());
    		int fatherIndex = motherIndex;
    		while(fatherIndex == motherIndex){
    			fatherIndex = rand.nextInt(breeders.size());
    		}
    		Gene mother = breeders.get(motherIndex);
    		Gene father = breeders.get(fatherIndex);
    		
    		//combine and produce new Genotypes
    		Gene[] offspring = father.reproduce(mother);
    		
    		//decide whether mutation happens
    		if(rand.nextDouble() < MUTATIONCHANCE){
    			offspring[0].mutate();
    		}
    		if(rand.nextDouble() < MUTATIONCHANCE){
    			offspring[1].mutate();
    		}
    		totalOffspring.add(offspring[0]);
    		totalOffspring.add(offspring[1]);
    	}
        
    	//cast away non-breeders and create new population of breeders and offspring
        
    	mPopulation = new ArrayList<Gene>();
    	mPopulation.addAll(breeders);
    	mPopulation.addAll(totalOffspring);
    	
    	
    }

    public int size(){ return mPopulation.size(); }
    /**
     * Returns the Gene at position of the mPopulation arrayList
     * the position in the population of the Gene we want to retrieve
     * the Gene at position  of the mPopulation arrayList
     */
    public Gene getGene(int index){ 
        return mPopulation.get(index); 
    }
}

