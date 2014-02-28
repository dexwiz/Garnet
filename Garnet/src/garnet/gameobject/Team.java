package garnet.gameobject;

import garnet.board.ControllerSystem;
import garnet.board.ManagerSystem;

public class Team {
	private int teamNumber;
	private boolean playerControllered = true;
	private ControllerSystem controller;
	private ManagerSystem turnSystem;
	
	public Team(int teamNumber)
	{
		this.teamNumber = teamNumber;
	}
	
	public void startTurn()
	{

	}
	
	public void endTurn()
	{
		turnSystem.endTurn();
	}

	public int getTeamNumber() {
		return teamNumber;
	}

	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}

	public boolean isPlayerControllered() {
		return playerControllered;
	}

	public void setPlayerControllered(boolean playerControllered) {
		this.playerControllered = playerControllered;
	}

	public ControllerSystem getController() {
		return controller;
	}

	public void setController(ControllerSystem controller) {
		this.controller = controller;
		this.controller.setTeam(this);
	}

	public ManagerSystem getTurnSystem() {
		return turnSystem;
	}

	public void setTurnSystem(ManagerSystem turnSystem) {
		this.turnSystem = turnSystem;
	}
	
}
