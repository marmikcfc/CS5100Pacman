/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman;

import java.util.EnumMap;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.controllers.algoControllers.GeneticAlgorithmController;
import pacman.controllers.algoControllers.Evaluation;
import pacman.controllers.moveControllers.Tree;
import pacman.controllers.algoControllers.iterativeDeepeningController;
import pacman.game.Constants.GHOST;

/**
 *
 * @author Marmik
 */



public class myGeneticAlgorithmPacman extends Controller<MOVE>
{
        private GeneticAlgorithmController ga;
	private MOVE myMove=MOVE.NEUTRAL;
	private static final int MIN_DISTANCE=20;	//if a ghost is this close, run away

        public myGeneticAlgorithmPacman(){
        
		ga = new GeneticAlgorithmController();

        
        }
        
        @Override
	public MOVE getMove(Game game, long timeDue) 
	{
		//Place your game logic here to play the game as Ms Pac-Man
            
            int current=game.getPacmanCurrentNodeIndex();
		// assume ghosts are moving in same direction
		EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
		ghostMoves.put(GHOST.BLINKY, game.getGhostLastMoveMade(GHOST.BLINKY));
		ghostMoves.put(GHOST.INKY, game.getGhostLastMoveMade(GHOST.INKY));
		ghostMoves.put(GHOST.PINKY, game.getGhostLastMoveMade(GHOST.PINKY));
		ghostMoves.put(GHOST.SUE, game.getGhostLastMoveMade(GHOST.SUE));
		
		Tree tree = new Tree(Evaluation.DEPTH);
		tree.getHeadNode().setGameState(game);
		
	
              	return ga.getMove(game, ghostMoves, 9);	
	//	return myMove;
	}
}
