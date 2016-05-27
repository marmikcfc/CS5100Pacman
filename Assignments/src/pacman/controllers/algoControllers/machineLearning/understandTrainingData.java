/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.controllers.algoControllers.machineLearning;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import pacman.controllers.examples.RandomGhosts;
import pacman.game.Constants;
import pacman.game.Game;

/**
 *
 * @author Marmik
 */
public class understandTrainingData {
    
    
     int pacmanPosition;
        Constants.MOVE pacmanMove;
        int[] ghostPosition;
        Constants.MOVE[] ghostMove;
        double averageDistance;
        boolean moveAway;
        Constants.MOVE[] possibleMoves;
        Constants.MOVE closestEnemyMove = Constants.MOVE.NEUTRAL;

        
 // Empty Contsrtuctor just to access the variables        
        understandTrainingData() {
        System.out.print("Into uninitialized constructor");
        }
    
        public understandTrainingData(String setting) {
            String[] values = setting.split(",");
            
            pacmanPosition = Integer.parseInt(values[5]);
            pacmanMove = Constants.MOVE.valueOf(values[6]);
            ghostPosition = new int[]{Integer.parseInt(values[9]), Integer.parseInt(values[13]), 
                Integer.parseInt(values[17]), Integer.parseInt(values[21])};
            ghostMove = new Constants.MOVE[]{Constants.MOVE.valueOf(values[12]), Constants.MOVE.valueOf(values[16]), 
                Constants.MOVE.valueOf(values[20]), Constants.MOVE.valueOf(values[24])};
            
            Game state = new Game(0);
            state.setGameState(setting);
            
            averageDistance = 0;
            double closestEnemyDistance = Double.POSITIVE_INFINITY;
            for (int i = 0; i < ghostPosition.length; i++) {
                double tempDistance = state.getShortestPathDistance(pacmanPosition, ghostPosition[i]);
                if (tempDistance != -1 && tempDistance < closestEnemyDistance) {
                    closestEnemyDistance = tempDistance;
                    closestEnemyMove = ghostMove[i];
                }
                averageDistance += state.getShortestPathDistance(pacmanPosition, ghostPosition[i]);
            }
            averageDistance /= ghostPosition.length;
            
            state.advanceGame(pacmanMove, new RandomGhosts().getMove());
            
            double newAverageDistance = 0;
            for (int i = 0; i < ghostPosition.length; i++) {
                newAverageDistance += state.getShortestPathDistance(pacmanPosition, ghostPosition[i]);
            }
            newAverageDistance /= ghostPosition.length;
            
            if (newAverageDistance < averageDistance) {
                moveAway = true;
            } else {
                moveAway = false;
            }
            
            possibleMoves = state.getPossibleMoves(pacmanPosition);
            
            
        }

         
    public ArrayList<understandTrainingData> understand(String filename) {
        ArrayList<String> replay = new ArrayList<String>();
		
        try {         	
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));	 
            String input = br.readLine();		
            
            while(input!=null) {
            	if (!input.equals("")) replay.add(input);
            	input=br.readLine();	
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        
        ArrayList<understandTrainingData> history = new ArrayList();
        
        for (int i = 0; i < replay.size(); i++) {
            String data = replay.get(i);
            
            history.add(new understandTrainingData(data));
        }
        Collections.sort(history, new dataCompare());
        
        return history;
    }
           
}


    class dataCompare implements Comparator<understandTrainingData> {
        public int compare(understandTrainingData r1, understandTrainingData r2) {
            if (r1.averageDistance < r2.averageDistance) return -1;
            if (r2.averageDistance < r1.averageDistance) return 1;
            return 0;
        }
    }