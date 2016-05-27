package pacman.geneticDecisionTreeController.evolution;

import java.util.Random;

/**
 *
 * @author Marmik
 */


public class Gene {

    /**
     * Fitness evaluates to how "close" the current gene is to the optimal solution.
     */
    protected double mFitness;
 
    /**
     * The chromosome contains only integers 0 or 1 
     */
    protected int mChromosome[];

    private Random rand;
    
    // --- functions:
    /**
     * Allocates memory for the mChromosome array and initializes any other data, such as fitness
     */
    Gene() {
    	rand = new Random();
        // allocating memory for the chromosome array
        mChromosome = new int[GeneticAlgorithm.CHROMOSOME_SIZE];
        // initial fitness 0
        mFitness = 0.0;
    }

    /**
     * Randomizes the numbers on the mChromosome array
     */
    public void randomizeChromosome(){
        for(int i = 0; i < mChromosome.length; i++){
        	mChromosome[i] = rand.nextInt(100);
        }
        
    }

    /**
     * Creates a number of offspring by using crossover the current
     * Gene's chromosome with another Gene's chromosome.
     */
    public Gene[] reproduce(Gene other){
        Gene[] result = new Gene[2];
        
        for(int n = 0; n < 2; n++){
        	result[n] = new Gene();
            for(int i = 0; i < getChromosomeSize(); i++){
            	//takes each value from either parent
                int variance = rand.nextInt(getChromosomeSize());
            	result[n].setChromosomeElement(i, (variance != 0 ? this.getChromosomeElement(i) : other.getChromosomeElement(i)));
            }
        }
        return result;
    }

    /**
     * Mutates a gene using random mutation
     * This function is called after the mutation chance is rolled.
     * Mutation can occur (depending on the designer's wishes) to a parent
     * before reproduction takes place, an offspring at the time it is created,
     * or (more often) on a gene which will not produce any offspring afterwards.
     */
    public void mutate(){
    	for(int i = 0; i < getChromosomeSize();i++){
    		mChromosome[i] = getChromosomeSize() + rand.nextInt(10)-5;
    	}
    }
    /**
     * Sets the fitness, after it is evaluated in the GeneticAlgorithm class.
     * @param value: the fitness value to be set
     */
    public void setFitness(double value) { mFitness = value; }
    /**
     * @return the gene's fitness value
     */
    public double getFitness() { return mFitness; }
    /**
     * Returns the element at position <b>index</b> of the mChromosome array
     * @param index: the position on the array of the element we want to access
     * @return the value of the element we want to access (0 or 1)
     */
    public int getChromosomeElement(int index){ return mChromosome[index]; }

    /**
     * Sets a <b>value</b> to the element at position <b>index</b> of the mChromosome array
     * @param index: the position on the array of the element we want to access
     * @param value: the value we want to set at position <b>index</b> of the mChromosome array (0 or 1)
     */
    public void setChromosomeElement(int index, int value){ mChromosome[index]=value; }
    /**
     * Returns the size of the chromosome (as provided in the Gene constructor)
     * @return the size of the mChromosome array
     */
    public int getChromosomeSize() { return mChromosome.length; }
    /**
     * Corresponds the chromosome encoding to the phenotype, which is a representation
     * that can be read, tested and evaluated by the main program.
     * @return a String with a length equal to the chromosome size, composed of A's
     * at the positions where the chromosome is 1 and a's at the posiitons
     * where the chromosme is 0
     */
    public String getPhenotype() {
        return mChromosome[0] + " " + mChromosome[1] + " " + mChromosome[2];
    }
    
    
}
