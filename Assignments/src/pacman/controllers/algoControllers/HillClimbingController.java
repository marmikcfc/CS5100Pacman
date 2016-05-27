/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.controllers.algoControllers;

/**
 *
 * @author Marmik
 */

import pacman.controllers.moveControllers.Node;
import pacman.controllers.moveControllers.Tree;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import java.util.ArrayList;
import java.util.EnumMap;

/*
    Uses greedy approach to find the best value at given point and then it goes to that node
    until it reaches Maxima.

*/
public class HillClimbingController {
	public MOVE getMove(EnumMap<GHOST, MOVE> ghostMoves, Tree tree) {
		long start = System.currentTimeMillis();
		
		boolean isMaxima = false;
		Node maximaNode = tree.getHeadNode();
		while (!isMaxima) {
			int leftValue = 0, rightValue = 0, upValue = 0, downValue = 0;
			ArrayList<Node> neighbors = maximaNode.getNeighbors();
			if (neighbors == null) {
				// if it has no neighbors anymore, it has reached the local maxima
				break;
			}
			
			for (int i = 0; i < 4; i++) {
				Game copy = maximaNode.getGameState().copy();
				switch(i) {
				case 0:
					copy.advanceGame(MOVE.LEFT, ghostMoves);
					leftValue = Evaluation.evaluateGameState(copy);
					break;
				case 1:
					copy.advanceGame(MOVE.RIGHT, ghostMoves);
					rightValue = Evaluation.evaluateGameState(copy);
					break;
				case 2:
					copy.advanceGame(MOVE.UP, ghostMoves);
					upValue = Evaluation.evaluateGameState(copy);
					break;
				case 3:
					copy.advanceGame(MOVE.DOWN, ghostMoves);
					downValue = Evaluation.evaluateGameState(copy);
					break;
				}
				neighbors.get(i).setGameState(copy);
			}
			
			int currentValue = Evaluation.evaluateGameState(maximaNode.getGameState());
			
			if (currentValue > leftValue && currentValue > rightValue &&
					currentValue > upValue && currentValue > downValue) {
				isMaxima = true;
			} else {
				if (leftValue > currentValue) {
					currentValue = leftValue;
					maximaNode = neighbors.get(0);
				}
				if (rightValue > currentValue) {
					currentValue = rightValue;
					maximaNode = neighbors.get(1);
				}
				if (upValue > currentValue) {
					currentValue = upValue;
					maximaNode = neighbors.get(2);
				}
				if (downValue > currentValue) {
					currentValue = downValue;
					maximaNode = neighbors.get(3);
				}
			}
		}
		
		// get move to this local maxima value
		if (maximaNode == tree.getHeadNode()) return MOVE.NEUTRAL;
		while (maximaNode.getPredecessor().getMove() != MOVE.NEUTRAL) {
			maximaNode = maximaNode.getPredecessor();
		}
		
		return maximaNode.getMove();
	}
}
