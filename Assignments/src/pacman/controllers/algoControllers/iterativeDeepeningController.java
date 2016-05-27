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
    
 /**
 *
 * Creates a tree where a node based on game states and moves. Then uses Iterative Deepening to find the best move to move forward
 */
	
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
				return Evaluation.getBestMove(leftValue, rightValue, upValue, downValue);
			}
		}
		
		return null; 
	}
	
	public int getBestValue(EnumMap<GHOST, MOVE> ghostMoves, Node node) {
		Game gameState = node.getPredecessor().getGameState().copy();
		gameState.advanceGame(node.getMove(), ghostMoves);
		node.setGameState(gameState);
		
		ArrayList<Node> neighbors = node.getNeighbors();
		if (neighbors == null) return Evaluation.evaluateGameState(gameState); 
		
		int bestValue = Integer.MIN_VALUE;
		for (Node neighbor : neighbors) {
			int value = getBestValue(ghostMoves, neighbor);
			if (value > bestValue) bestValue = value;
		}
		
		return bestValue;
	}
}
