package pacman.controllers.examples;

import pacman.controllers.algoControllers.iterativeDeepeningController;
import java.util.ArrayList;
import java.util.EnumMap;
import pacman.controllers.Controller;
import pacman.controllers.algoControllers.BFSController;
import pacman.controllers.algoControllers.DFSController;
import pacman.controllers.algoControllers.Evaluation;
import pacman.controllers.moveControllers.Tree;
import pacman.game.Game;

import static pacman.game.Constants.*;

/*
 * Pac-Man controller as part of the starter package - simply upload this file as a zip called
 * MyPacMan.zip and you will be entered into the rankings - as simple as that! Feel free to modify 
 * it or to start from scratch, using the classes supplied with the original software. Best of luck!
 * 
 * This controller utilises 3 tactics, in order of importance:
 * 1. Get away from any non-edible ghost that is in close proximity
 * 2. Go after the nearest edible ghost
 * 3. Go to the nearest pill/power pill
 */
public class StarterPacMan extends Controller<MOVE>
{	
	private static final int MIN_DISTANCE=20;	//if a ghost is this close, run away
	
//        private BFSController bfs;
    //    private iterativeDeepeningController idc;
        private DFSController dfs;

    public StarterPacMan() {

       // idc = new iterativeDeepeningController();
       dfs=new DFSController();

    }
        
        
        @Override
	public MOVE getMove(Game game,long timeDue)
	{			
		int current=game.getPacmanCurrentNodeIndex();
		// assume ghosts are moving in same direction
		EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
		ghostMoves.put(GHOST.BLINKY, game.getGhostLastMoveMade(GHOST.BLINKY));
		ghostMoves.put(GHOST.INKY, game.getGhostLastMoveMade(GHOST.INKY));
		ghostMoves.put(GHOST.PINKY, game.getGhostLastMoveMade(GHOST.PINKY));
		ghostMoves.put(GHOST.SUE, game.getGhostLastMoveMade(GHOST.SUE));
		
		Tree tree = new Tree(Evaluation.DEPTH);
		tree.getHeadNode().setGameState(game);
		
		//return bfs.getMove(ghostMoves, tree);
                
               // return idc.getMove(game, ghostMoves, Evaluation.DEPTH);
               
               return dfs.getMove(ghostMoves, tree);
	}
}























