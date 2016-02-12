/* Don't use this since I realised later that Evalution file evaluates the best move from Pacman's Point of view
   and Not Ghost's Point of View. Got to work on evalutionGhosts.java
*/

package pacman.entries.ghosts;

import java.util.EnumMap;
import java.util.Random;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.controllers.Controller;
import pacman.controllers.algoControllers.BFSController;
import pacman.controllers.algoControllers.Evaluation;
import pacman.controllers.moveControllers.Tree;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getActions() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.ghosts.mypackage).
 */
public class MyBFSGhosts extends Controller<EnumMap<GHOST,MOVE>>
{
	private EnumMap<GHOST, MOVE> myMoves=new EnumMap<GHOST, MOVE>(GHOST.class);
        private EnumMap<GHOST, MOVE> ghostMoves=new EnumMap<GHOST, MOVE>(GHOST.class);
	private MOVE[] allMoves=MOVE.values();
	private Random rnd=new Random();
        
        private BFSController bfs;
        
        public MyBFSGhosts(){
        
            // idc = new iterativeDeepeningController();
            // bfs=new BFSController();
               bfs=new BFSController();

        
        }
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
	{
		myMoves.clear();
		
		//Place your game logic here to play the game as the ghosts
                Tree tree = new Tree(Evaluation.DEPTH);
		tree.getHeadNode().setGameState(game);
                
                for(GHOST ghostType : GHOST.values())
			if(game.doesGhostRequireAction(ghostType)){
				myMoves.put(ghostType,allMoves[rnd.nextInt(allMoves.length)]);
                                ghostMoves.put(ghostType, bfs.getMove(myMoves, tree));
                        }
		
		return ghostMoves;
	}
}