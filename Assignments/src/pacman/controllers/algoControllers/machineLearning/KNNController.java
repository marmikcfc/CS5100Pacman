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
import pacman.controllers.examples.AggressiveGhosts;
import pacman.game.Constants;
import pacman.game.Game;

/**
 *
 * @author Marmik
 */
public class KNNController {
    
    
    
    // move and last move
    private Constants.MOVE myMove=Constants.MOVE.NEUTRAL;
    private Constants.MOVE lastMove = Constants.MOVE.NEUTRAL;
    
     understandTrainingData utd = new understandTrainingData();
     
              //historical Training Data
    String filename = "trainingData.txt";
    ArrayList<understandTrainingData> historicalData = utd.understand(filename);

    
     private double averageGhostDistance(Game state){
        double sumDistance = 0;
        for (Constants.GHOST g : Constants.GHOST.values()) {
            double d = state.getManhattanDistance(state.getPacmanCurrentNodeIndex(), state.getGhostCurrentNodeIndex(g));
            sumDistance += d;
        }
        return (sumDistance/Constants.GHOST.values().length);
    }

    private int kNNSearch(int first, int last, double key) {
        if (first >= last) {
            if (last < 0) return first;
            if (first != 0) return first - 1;
            return first;
        }
        int mid = (first + last) / 2;
        if (historicalData.get(mid).averageDistance == key) {
            return mid;
        } else if (historicalData.get(mid).averageDistance > key) {
            return kNNSearch(first, mid - 1, key);
        } else {
            return kNNSearch(mid + 1, last, key);
        }
    }
    
    public Constants.MOVE kNN(Game game, int k, long timeDue) {
        Constants.MOVE bestMove = myMove;
        
        int current = game.getPacmanCurrentNodeIndex();
        
        double averageDistance = averageGhostDistance(game);
        
        int positionBottom = kNNSearch(0, historicalData.size(), averageDistance);
        int positionTop = positionBottom + 1;
        k--;
        int totalCount = k / 2;
        while (totalCount > 0 && k > 0) {
            if (positionBottom > 0) {
                if (historicalData.get(positionBottom).moveAway) {
                    totalCount--;
                }
                positionBottom--;
                k--;
                if (k <= 0) break;
            }
            
            if (positionTop < historicalData.size()) {
                if (historicalData.get(positionTop).moveAway) {
                    totalCount--;
                }
                positionTop++;
                k--;
            }
        }
        
        Constants.MOVE[] next = game.getPossibleMoves(current);
        if (totalCount > 0) {
            double closestAway = Double.POSITIVE_INFINITY;
            for (int index : game.getActivePillsIndices()) {
                double distanceAway = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), index);
                if (distanceAway < closestAway) closestAway = distanceAway;
            }
            
            for (Constants.MOVE eachMove : next) {
                Game newState = game.copy();
                newState.advanceGame(eachMove, new AggressiveGhosts().getMove());
                // if near to pill good;
                double nextClosestAway = Double.POSITIVE_INFINITY;
                    for (int index : newState.getActivePillsIndices()) {
                        double distanceAway = newState.getShortestPathDistance(newState.getPacmanCurrentNodeIndex(), index);
                        if (distanceAway < closestAway) nextClosestAway = distanceAway;
                    }
                    if (nextClosestAway <= closestAway) bestMove = eachMove;
            } 
        } else {
            for (Constants.MOVE eachMove : next) {
                Game newState = game.copy();
                newState.advanceGame(eachMove, new AggressiveGhosts().getMove());
                if (averageGhostDistance(newState) < averageDistance) {
                    bestMove = eachMove;
                }
            }
        }
        
        lastMove = bestMove;
        return bestMove;

    }
    
}

