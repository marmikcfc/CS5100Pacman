package pacman.entries.pacman;

import pacman.geneticDecisionTreeController.evolution.GeneticAlgorithm;
import pacman.geneticDecisionTreeController.evolution.geneticDecisionTree;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


/**
 *
 * @author Marmik
 */



public class myGeneticDecisionTree extends Controller<MOVE>
{
    // contants for the input to decision tree
    static int preyDistance,spDistance,enemyDistance;

	
        public myGeneticDecisionTree(){
        
        // Initializing the population 
        GeneticAlgorithm population = new GeneticAlgorithm(500);
        
        double maximumFitness = 0;
        int[] param = new int [3];
    
        while(true){
            // evaluate current generation:
            population.evaluateGeneration();

            double avgFitness=0.0;
            double minFitness=Double.POSITIVE_INFINITY;
            double maxFitness=Double.NEGATIVE_INFINITY;
            String bestIndividual="";
            String worstIndividual="";
            for(int i = 0; i < population.size(); i++){
                double currFitness = population.getGene(i).getFitness();
                avgFitness += currFitness;
                if(currFitness < minFitness){
                    minFitness = currFitness;
                    worstIndividual = population.getGene(i).getPhenotype();
                }
                if(currFitness > maxFitness){
                    maxFitness = currFitness;
                    bestIndividual = population.getGene(i).getPhenotype();
                }
            }
            if(population.size()>0){ avgFitness = avgFitness/population.size(); 
                           if(maximumFitness < maxFitness){
                                maximumFitness = maxFitness;
                                String[] tempStringToRemoveParameters = bestIndividual.split(" ");
                                param[0] = Integer.parseInt(tempStringToRemoveParameters[0]);
                                param[1] = Integer.parseInt(tempStringToRemoveParameters[1]);
                                param[2] = Integer.parseInt(tempStringToRemoveParameters[2]);
                           }
            }
            String output = "Generation: " + population.currentGen;
            output += "\t AvgFitness: " + avgFitness;
            output += "\t MinFitness: " + minFitness + " (" + worstIndividual +")";
            output += "\t MaxFitness: " + maxFitness + " (" + bestIndividual +")";
            System.out.println(output);
            // produce next generation:
            population.mutate();
            population.currentGen++;
            population.setPairings();
            if(population.optimalFitness == maxFitness | population.currentGen > population.MAX_GEN){
            	System.out.println("Best individual 123 4: " + bestIndividual);
              	System.out.println("Best input parameters " + param[0] + " "+ param[1] +" "+param[2]);
            	System.out.println("Maximum Fitness " + maximumFitness);
                
                preyDistance = param[0]; spDistance=param[1];enemyDistance=param[2];
            	break;
            }
        }
        
        }
        
        @Override
	public MOVE getMove(Game game, long timeDue) 
	{
		//Place your game logic here to play the game as Ms Pac-Man
            
            //get the best move based on decision tree logic
        geneticDecisionTree edt = new geneticDecisionTree(preyDistance,spDistance,enemyDistance);
        return edt.getMove(game, timeDue);
	}
}



