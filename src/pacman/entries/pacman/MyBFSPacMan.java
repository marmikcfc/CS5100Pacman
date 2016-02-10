package pacman.entries.pacman;

import java.util.EnumMap;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.controllers.algoControllers.BFSController;
import pacman.controllers.algoControllers.DFSController;
import pacman.controllers.algoControllers.Evaluation;
import pacman.controllers.moveControllers.Tree;
import pacman.controllers.algoControllers.iterativeDeepeningController;
import pacman.game.Constants.GHOST;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyBFSPacMan extends Controller<MOVE>
{
        private BFSController bfs;
	private MOVE myMove=MOVE.NEUTRAL;
	private static final int MIN_DISTANCE=20;	//if a ghost is this close, run away

        public MyBFSPacMan(){
        
             bfs=new BFSController();

        
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
		
		return bfs.getMove(ghostMoves, tree);
                
               
		
		//return myMove;
	}
}