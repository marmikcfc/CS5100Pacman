/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.controllers.algoControllers;

import java.util.ArrayList;
import java.util.EnumMap;

import pacman.controllers.moveControllers.Node;
import pacman.controllers.moveControllers.Tree;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
/**
 *
 * @author Marmik
 */
public class DFSController {
    public MOVE getMove(EnumMap<GHOST, MOVE> ghostMoves, Tree tree) {
		long start = System.currentTimeMillis();
		ArrayList<Node> headNeighbors = tree.getHeadNode().getNeighbors();
		
		int leftValue = getBestValue(ghostMoves, headNeighbors.get(0));
		int rightValue = getBestValue(ghostMoves, headNeighbors.get(1));
		int upValue = getBestValue(ghostMoves, headNeighbors.get(2));
		int downValue = getBestValue(ghostMoves, headNeighbors.get(3));
		
		if (Evaluation.LOG_TIME) System.out.println(System.currentTimeMillis() - start);
		return Evaluation.getBestMove(leftValue, rightValue, upValue, downValue);
	}
	
	public int getBestValue(EnumMap<GHOST, MOVE> ghostMoves, Node node) {
		Game gameState = node.getPredecessor().getGameState().copy();
		if (!isValidMove(gameState.getPossibleMoves(gameState.getPacmanCurrentNodeIndex()), node.getMove()))
			return Integer.MIN_VALUE;
		
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
	
	boolean isValidMove(MOVE[] validMoves, MOVE move) {
		for (MOVE validMove : validMoves) {
			if (move == validMove) return true;
		}
		return false;
	}
}
