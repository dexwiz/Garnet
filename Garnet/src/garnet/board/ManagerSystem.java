package garnet.board;

import garnet.ability.Ability;
import garnet.ability.action.Action;
import garnet.ability.action.ActionFactory;
import garnet.ability.action.Action.ActionType;
import garnet.gameobject.GameObject;
import garnet.gameobject.GameObjectState;
import garnet.gameobject.Team;
import garnet.ui.UIStage;
import garnet.util.Location;

import java.util.ArrayList;

public class ManagerSystem {
	private ArrayList<Team> teams = new ArrayList<Team>();
	private Team active;
	private UIStage ui;
	
	private MapSystem map;
	private ActionFactory actionFactory;
	private ActionSystem actionSystem;
	private TargetingSystem targetingSystem;
	
	private boolean actionOccuring = false;
	
	public void startGame()
	{
		active = teams.get(0);
		ui.setController(active.getController());
		active.startTurn();
	}
	
	/**
	 * Manager casts an ability at the request of a controller
	 * @param ability
	 * @param selected
	 * @param cursor
	 * @return
	 */
	public boolean castAbility(Ability ability, GameObjectState selected, Location cursor) {
		if(this.isAbilityCastable(ability, selected, cursor))
		{
			Action root = actionFactory.constructFlow(ability, selected, cursor);
			actionSystem.execute(root);
			selected.getAbilities().discard(ability);
			actionOccuring = true;
			return true;
		}
		else
		{
			return false;
		}		
	}
	
	/**
	 * Callback when an action tree has finished executing
	 * @param action
	 */
	public void finishAction(Action action)
	{
		checkForDead();
	}
	
	public boolean isAbilityCastable(Ability ability, GameObjectState selected, Location cursor)
	{
		if(ability == null || selected == null || cursor == null)
		{
			return false;
		}
		if(!selected.getTeam().equals(active))
		{
			return false;
		}

		else if(this.actionFactory.getManaCost(ability, selected) > selected.getMana())
		{
			return false;
		}
		else if(!this.targetingSystem.isValidAbilityTarget(cursor, ability.getTargetingParameters())) 
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public void checkForDead()
	{
		for(GameObjectState state : map.getGameObjectStates())
		{
			if(state.getHP() <= 0)
			{
				Action death = this.actionFactory.death(state);
				this.actionSystem.execute(death);
			}
		}
	}
	
	/**
	 * Starts the turn for a team. Draws abilities and fires turn start events
	 */
	public void startTurn()
	{
		ui.setController(active.getController());
		for(GameObject obj : map.getGameObjectsOnteam(active))
		{
			obj.getAbilities().draw();			
		}
	}

	/**
	 * Call back function called by teams when they need to end their turn
	 */
	public void endTurn() {
		this.nextTeam();	
	}
	
	private void nextTeam()
	{
		int nextTeam = teams.indexOf(active) +1;
		if(nextTeam >= teams.size()) nextTeam = 0;
		active = teams.get(nextTeam);
		this.startTurn();
	}

	public void addTeam(Team team) {
		this.teams.add(team);
	}
	
	public void setUIStage(UIStage uiStage)
	{
		this.ui = uiStage;
	}
	
	public void setMapSystem(MapSystem mapSystem)
	{
		this.map = mapSystem;
	}
	
	public void setAbilitySystem(ActionFactory abilitySystem) {
		this.actionFactory = abilitySystem;
	}
	
	public void setActionSystem(ActionSystem actionSystem) {
		this.actionSystem = actionSystem;
	}
	public void setTargetingSystem(TargetingSystem targetingSystem) {
		this.targetingSystem = targetingSystem;
	}

}
