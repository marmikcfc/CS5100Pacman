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
import pacman.controllers.algoControllers.machineLearning.KNNController;
import pacman.game.Constants.GHOST;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class myKNNPacman extends Controller<MOVE>
{
        private KNNController knn;
	private MOVE myMove=MOVE.NEUTRAL;
	private static final int MIN_DISTANCE=20;	//if a ghost is this close, run away

        public myKNNPacman(){
        
             knn=new KNNController();

        }
        
        @Override
	public MOVE getMove(Game game, long timeDue) 
	{
		//Place your game logic here to play the game as Ms Pac-Man
		      return knn.kNN(game, 10, timeDue);

                
               
		
		//return myMove;
	}
}

 