/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman;

/**
 *
 * @author Marmik
 */




import java.util.EnumMap;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.controllers.algoControllers.BFSController;
import pacman.controllers.algoControllers.DFSController;
import pacman.controllers.algoControllers.Evaluation;
import pacman.controllers.moveControllers.Tree;
import pacman.controllers.algoControllers.machineLearning.decisionTreeController;
import pacman.game.Constants.GHOST;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class myDecisionTreePacman extends Controller<MOVE>
{
        private decisionTreeController dt;
	private MOVE myMove=MOVE.NEUTRAL;
	private static final int MIN_DISTANCE=20;	//if a ghost is this close, run away

        public myDecisionTreePacman(){
        
             dt=new decisionTreeController();

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
		
		      return dt.decisionTree(game, timeDue);

                
               
		
		//return myMove;
	}
}

