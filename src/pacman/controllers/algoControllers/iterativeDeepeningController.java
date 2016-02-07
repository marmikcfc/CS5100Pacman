/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.controllers.algoControllers;

import java.util.ArrayList;
import java.util.EnumMap;
import pacman.controllers.algoControllers.Evaluation;

import pacman.controllers.moveControllers.Node;
import pacman.controllers.moveControllers.Tree;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
/**
 *
 * @author Marmik
 */

public class iterativeDeepeningController {
	// simulates iterative deepening; true iterative deepening is not possible
	// since we are not searching for a goal node
	public MOVE getMove(Game game, EnumMap<GHOST, MOVE> ghostMoves, int depth) {
		long start = System.currentTimeMillis();
		for (int i = 1; i <= depth; i++) {
			Tree tree = new Tree(i);
			tree.getHeadNode().setGameState(game);
			ArrayList<Node> headNeighbors = tree.getHeadNode().getNeighbors();
			
			int leftValue = getBestValue(ghostMoves, headNeighbors.get(0));
			int rightValue = getBestValue(ghostMoves, headNeighbors.get(1));
			int upValue = getBestValue(ghostMoves, headNeighbors.get(2));
			int downValue = getBestValue(ghostMoves, headNeighbors.get(3));
			
			if (i == depth) {
				if (Evaluation.LOG_TIME) System.out.println(System.currentTimeMillis() - start);
				return Evaluation.getBestMove(leftValue, rightValue, upValue, downValue);
			}
		}
		
		return null; // should never reach this point
	}
	
	public int getBestValue(EnumMap<GHOST, MOVE> ghostMoves, Node node) {
		Game gameState = node.getPredecessor().getGameState().copy();
		gameState.advanceGame(node.getMove(), ghostMoves);
		node.setGameState(gameState);
		
		ArrayList<Node> neighbors = node.getNeighbors();
		if (neighbors == null) return Evaluation.evaluateGameState(gameState); // end of branch return heuristic
		
		int bestValue = Integer.MIN_VALUE;
		for (Node neighbor : neighbors) {
			int value = getBestValue(ghostMoves, neighbor);
			if (value > bestValue) bestValue = value;
		}
		
		return bestValue;
	}
}
