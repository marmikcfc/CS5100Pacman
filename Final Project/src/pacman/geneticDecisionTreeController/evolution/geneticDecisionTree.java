package pacman.geneticDecisionTreeController.evolution;

import java.util.ArrayList;
import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class geneticDecisionTree extends Controller<MOVE>
{

	static int preyDist = 25;
	static int spDist = 10;
	static int enemyDist = 25;
	
	public geneticDecisionTree(int preyDist, int spDist, int enemyDist){
		preyDist = preyDist;
		spDist = spDist;
		enemyDist = enemyDist;
	}
	
	private MOVE myMove=MOVE.NEUTRAL;
	
	public MOVE getMove(Game game, long timeDue) 
	{
		//Decision Tree
		if(IsEnemyNearby(game))
		{
			if(IsSPNearby(game)){
				myMove = GoSP(game);
			}
			else
			{
				myMove = Flee(game);
			}
		}
		else
		{
			if(IsPreyNearby(game)){
				myMove = Hunt(game);
				//System.out.println("Hunt");
			}
			else{
				myMove = Eat(game);
			}
		}
		
		return myMove;
	}
	
	public boolean IsEnemyNearby(Game game){
		GHOST[] ghosts = GHOST.values();
		for (GHOST g : ghosts){
			//if ghost is in lair
			if(game.getGhostLairTime(g) > 0){
				continue;
			}
			//if ghost is not hostile
			if(0 < game.getGhostEdibleTime(g)){
				continue;
			}
			//if hostile ghost is too far away
			if(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), DM.PATH) > enemyDist){
				continue;
			}
			return true;
		}
		return false;
	}
	
	public boolean IsPreyNearby(Game game){
		GHOST[] ghosts = GHOST.values();
		for (GHOST g : ghosts){
			//if ghost is in lair
			if(game.getGhostLairTime(g) > 0){
				continue;
			}
			//if ghost is not edible
			if(0 == game.getGhostEdibleTime(g)){
				continue;
			}
			//if edible ghost is too far away
			if(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), DM.PATH) > preyDist){
				continue;
			}
			return true;
		}
		return false;
	}
	
	public boolean IsSPNearby(Game game){
		int[] pPills = game.getActivePowerPillsIndices();
		for (int pNode : pPills){
			if(game.getDistance(game.getPacmanCurrentNodeIndex(), pNode, DM.PATH) > spDist){
				continue;
			}
			return true;
		}
		return false;
	}
	
	public MOVE GoSP(Game game){
		int[] pillsHere = game.getActivePowerPillsIndices();
		int closest = -1;
		double closestDist = 0;
		for(int pillI : pillsHere){
			double dist = game.getDistance(game.getPacmanCurrentNodeIndex(), pillI, DM.PATH);
			if(closest == -1 || dist < closestDist){
				closest = pillI;
				closestDist = dist;
			}
		}
		
		return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), closest, DM.PATH);
	}
	
	public MOVE Flee(Game game){
		GHOST[] ghosts = GHOST.values();
		ArrayList<GHOST> ediGhosts = new ArrayList<GHOST>();
		for (GHOST g : ghosts){
			//if ghost is in lair
			if(game.getGhostLairTime(g) > 0){
				continue;
			}
			//if ghost is not edible
			if(0 != game.getGhostEdibleTime(g)){
				continue;
			}
			//if edible ghost is too far away
			if(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), DM.PATH) > enemyDist){
				continue;
			}
			ediGhosts.add(g);
		}

		int closest = -1;
		double closestDist = 0;
		for(GHOST eGhost : ediGhosts){
			int gPos = game.getGhostCurrentNodeIndex(eGhost);
			double dist = game.getDistance(game.getPacmanCurrentNodeIndex(), gPos, DM.PATH);
			if(closest == -1 || dist < closestDist){
				closest = gPos;
				closestDist = dist;
			}
		}

		return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), closest, DM.PATH);
	}
	
	public MOVE Hunt(Game game){
		GHOST[] ghosts = GHOST.values();
		ArrayList<GHOST> ediGhosts = new ArrayList<GHOST>();
		for (GHOST g : ghosts){
			//if ghost is in lair
			if(game.getGhostLairTime(g) > 0){
				continue;
			}
			//if ghost is not edible
			if(0 == game.getGhostEdibleTime(g)){
				continue;
			}
			//if edible ghost is too far away
			if(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), DM.PATH) > preyDist){
				continue;
			}
			ediGhosts.add(g);
		}

		int closest = -1;
		double closestDist = 0;
		for(GHOST eGhost : ediGhosts){
			int gPos = game.getGhostCurrentNodeIndex(eGhost);
			double dist = game.getDistance(game.getPacmanCurrentNodeIndex(), gPos, DM.PATH);
			if(closest == -1 || dist < closestDist){
				closest = gPos;
				closestDist = dist;
			}
		}

		return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), closest, DM.PATH);
	}
	
	public MOVE Eat(Game game){
		int[] pillsHere = game.getActivePillsIndices();
		int closest = -1;
		double closestDist = 0;
		for(int pillI : pillsHere){
			double dist = game.getDistance(game.getPacmanCurrentNodeIndex(), pillI, DM.PATH);
			if(closest == -1 || dist < closestDist){
				closest = pillI;
				closestDist = dist;
			}
		}
		
		return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), closest, DM.PATH);
	}
}