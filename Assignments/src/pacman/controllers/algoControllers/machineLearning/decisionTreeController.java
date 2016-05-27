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
import java.util.HashSet;
import java.util.Set;
import pacman.controllers.examples.AggressiveGhosts;
import pacman.controllers.examples.RandomGhosts;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.controllers.algoControllers.machineLearning.understandTrainingData;

/**
 *
 * @author Marmik
 */
public class decisionTreeController {
    
      // move and last move
    private Constants.MOVE myMove=Constants.MOVE.NEUTRAL;
    private Constants.MOVE lastMove = Constants.MOVE.NEUTRAL;

   understandTrainingData utd = new understandTrainingData();
    // Set up a Decision Tree based on Historical Data
    
         //historical Training Data
    String filename = "replay.txt";
    public ArrayList<understandTrainingData> historicalData = utd.understand(filename);

    tree decisionMaking = setupTree(historicalData);
 
            
    class tree {
        int[][][] scores; 
        
        public tree() {
            scores = new int[11][4][4];
        }
        
        int getIndexMax(int[] arr, boolean flag1, boolean flag2, boolean flag3, boolean flag4){
            int index = 0;
            int max = -1;
            if (flag1) {
                if (arr[0] > max) {
                    max = arr[0];
                    index = 0;
                }
            }
            if (flag2) {
                if (arr[1] > max) {
                    max = arr[1];
                    index = 1;
                }
            }
            if (flag3) {
                if (arr[2] > max) {
                    max = arr[2];
                    index = 2;
                }
            }
            if (flag4) {
                if (arr[3] > max) {
                    max = arr[3];
                    index = 3;
                }
            }
            return index;
        }
        
        public Constants.MOVE makeDecision(Game state, Constants.MOVE closestDir) {
            int dir = 0;
            if (closestDir == Constants.MOVE.LEFT) dir = 0;
            if (closestDir == Constants.MOVE.DOWN) dir = 1;
            if (closestDir == Constants.MOVE.RIGHT) dir = 2;
            if (closestDir == Constants.MOVE.UP) dir = 3;
            
            int decision = 0;
            Set<Constants.MOVE> moves = new HashSet();
            for (Constants.MOVE eachMove : state.getPossibleMoves(state.getPacmanCurrentNodeIndex())) {
                moves.add(eachMove);
            }
            
            if (moves.size() == 4) {
                decision = getIndexMax(scores[0][dir], true, true, true, true);
            } else if (moves.size() == 3) {
                if (!moves.contains(Constants.MOVE.DOWN)) {
                    decision = getIndexMax(scores[1][dir], true, false, true, true);
                } else if (!moves.contains(Constants.MOVE.RIGHT)) {
                    decision = getIndexMax(scores[2][dir], true, true, false, true);
                } else if (!moves.contains(Constants.MOVE.UP)) {
                    decision = getIndexMax(scores[3][dir], true, true, true, false);
                } else {
                    decision = getIndexMax(scores[4][dir], false, true, true, true);
                }
            } else {
                if (moves.contains(Constants.MOVE.DOWN)) {
                    if (moves.contains(Constants.MOVE.RIGHT)) {
                        decision = getIndexMax(scores[5][dir], false, true, true, false);
                    } else if (moves.contains(Constants.MOVE.UP)) {
                        decision = getIndexMax(scores[6][dir], false, true, false, true);
                    } else {
                        decision = getIndexMax(scores[7][dir], true, true, false, false);
                    }
                } else if (moves.contains(Constants.MOVE.RIGHT)) {
                    if (moves.contains(Constants.MOVE.UP)) {
                        decision = getIndexMax(scores[8][dir], false, false, true, true);
                    } else {
                        decision = getIndexMax(scores[9][dir], true, false, true, false);
                    }
                } else {
                    decision = getIndexMax(scores[10][dir], true, false, false, true);
                }
            }
            
            if (decision == 0) return Constants.MOVE.LEFT;
            if (decision == 1) return Constants.MOVE.DOWN;
            if (decision == 2) return Constants.MOVE.RIGHT;
            return Constants.MOVE.UP;
        }
        
        public void addCondition(Set<Constants.MOVE> moves, Constants.MOVE closestDir, Constants.MOVE pacmanMove) {
            int dir = 0;
            if (closestDir == Constants.MOVE.LEFT) dir = 0;
            if (closestDir == Constants.MOVE.DOWN) dir = 1;
            if (closestDir == Constants.MOVE.RIGHT) dir = 2;
            if (closestDir == Constants.MOVE.UP) dir = 3;
            
            int move = 0;
            if (pacmanMove == Constants.MOVE.LEFT) move = 0;
            if (pacmanMove == Constants.MOVE.DOWN) move = 1;
            if (pacmanMove == Constants.MOVE.RIGHT) move = 2;
            if (pacmanMove == Constants.MOVE.UP) move = 3;
            
            if (moves.size() == 4) {
                scores[0][dir][move]++;
            } else if (moves.size() == 3) {
                if (!moves.contains(Constants.MOVE.DOWN)) {
                    scores[1][dir][move]++;
                } else if (!moves.contains(Constants.MOVE.RIGHT)) {
                    scores[2][dir][move]++;
                } else if (!moves.contains(Constants.MOVE.UP)) {
                    scores[3][dir][move]++;
                } else {
                    scores[4][dir][move]++;
                }
            } else {
                if (moves.contains(Constants.MOVE.DOWN)) {
                    if (moves.contains(Constants.MOVE.RIGHT)) {
                        scores[5][dir][move]++;
                    } else if (moves.contains(Constants.MOVE.UP)) {
                        scores[6][dir][move]++;
                    } else {
                        scores[7][dir][move]++;
                    }
                } else if (moves.contains(Constants.MOVE.RIGHT)) {
                    if (moves.contains(Constants.MOVE.UP)) {
                        scores[8][dir][move]++;
                    } else {
                        scores[9][dir][move]++;
                    }
                } else {
                    scores[10][dir][move]++;
                }
            }
        }
    }
    
    public tree setupTree(ArrayList<understandTrainingData> data) {
        tree finalTree = new tree();
        for (int i = 0; i < data.size(); i++) {
            understandTrainingData cur = data.get(i);
            Set<Constants.MOVE> possibleMoves = new HashSet();
            
            for (Constants.MOVE eachMove : cur.possibleMoves) {
                possibleMoves.add(eachMove);
            }
            finalTree.addCondition(possibleMoves, cur.closestEnemyMove, cur.pacmanMove);
        }
        
        return finalTree;
    }
    
    public Constants.MOVE decisionTree(Game game, long timeDue) {
        Constants.MOVE bestMove = myMove;
        
        int current = game.getPacmanCurrentNodeIndex();
        
        double distanceAway = Double.POSITIVE_INFINITY;
        Constants.MOVE ghostDir = Constants.MOVE.NEUTRAL;
        for (Constants.GHOST g : Constants.GHOST.values()) {
            double distTemp = game.getShortestPathDistance(current, game.getGhostCurrentNodeIndex(g));
            if (distTemp != -1 && distTemp < distanceAway) {
                distanceAway = distTemp;
                ghostDir = game.getGhostLastMoveMade(g);
            }
        }
        
        bestMove = decisionMaking.makeDecision(game, ghostDir);
        
        lastMove = bestMove;
        return lastMove;
    }
}
