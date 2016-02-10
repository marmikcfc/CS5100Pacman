/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.controllers.algoControllers;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;

import pacman.controllers.moveControllers.Node;
import pacman.controllers.moveControllers.Tree;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 *
 * @author Marmik
 */
public class BFSController {
    
  /**
 *
 * Creates a tree where a node based on game states and moves. Then uses BFS to find the best move to move forward
 */
    
    public MOVE getMove(EnumMap<GHOST, MOVE> ghostMoves, Tree tree) {
		long start = System.currentTimeMillis();
		LinkedList<Node> nodes = new LinkedList<Node>();
		nodes.add(tree.getHeadNode());
		
		int leftValue = Integer.MIN_VALUE;
		int rightValue = Integer.MIN_VALUE;
		int upValue = Integer.MIN_VALUE;
		int downValue = Integer.MIN_VALUE;
		
		while(!nodes.isEmpty()) {
			Node node = nodes.removeFirst();
			node.setVisited(true);
			if (node.getPredecessor() != null) { // regular Node
				// set gameState and advance move based on current node
				Game gameState = node.getPredecessor().getGameState().copy();
				gameState.advanceGame(node.getMove(), ghostMoves);
				node.setGameState(gameState);
			}
			if (node.getNeighbors() == null) { // end of tree branch
				int value = Evaluation.evaluateGameState(node.getGameState());
				// get head node move type
				Node nodeType = node.getPredecessor();
				while (nodeType.getPredecessor().getMove() != MOVE.NEUTRAL) {
					nodeType = nodeType.getPredecessor();
				}
				switch (nodeType.getMove()) {
				case LEFT:
					if (value > leftValue) leftValue = value;
					break;
				case RIGHT:
					if (value > rightValue) rightValue = value;
					break;
				case UP:
					if (value > upValue) upValue = value;
					break;
				case DOWN:
					if (value > downValue) downValue = value;
					break;
				case NEUTRAL:
					break;
				}
			} else { // regular node
				// add neighbors to be searched
				ArrayList<Node> neighbors = node.getNeighbors();
				for (Node neighbor : neighbors) {
					if (!neighbor.isVisited()) nodes.add(neighbor);
				}
			}
		}
		
		return Evaluation.getBestMove(leftValue, rightValue, upValue, downValue);
	}

    
}
