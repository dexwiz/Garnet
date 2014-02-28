package garnet.board;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;

import garnet.ability.Ability;
import garnet.ability.Buff;
import garnet.ability.action.ActionFactory;
import garnet.gameobject.Abilities;
import garnet.gameobject.GameObject;
import garnet.gameobject.GameObjectState;
import garnet.gameobject.Team;
import garnet.util.Location;

public class ControllerSystem {
	private MapSystem map;
	private RenderSystem renderSystem;
	private TargetingSystem targetingSystem;
	private ActionFactory abilitySystem;


	private GameObjectState selected;
	private GameObjectState targeted;
	
	private Abilities selectedAbilites;
	private Ability selectedAbility;
	
	private Location cursor = new Location(-1,-1);
	private Team team;

	private ManagerSystem managerSystem;
	
	/**
	 * Processes a screen touch, will try to move the cursor 
	 * @param screenX
	 * @param screenY
	 */
	public void touchBoard(int screenX, int screenY)
	{
		Location boardLocation = renderSystem.screenToMap(screenX, screenY);
		moveCursorTo(boardLocation);
	}
	
	
	/**
	 * Tries to move the cursor to a location if it exists. Also updates Render System with new cursor location, and draws ability highlights
	 * @param location
	 */
	public void moveCursorTo(Location location)
	{
		//if not on the map, do nothing
		if(!map.locationExists(location)) return;
		
		//moves the cursor
		cursor.set(location);
		renderSystem.setCursorLocation(location);
		
		//if there is a selected ability it tries to highlight the map
		if(selectedAbility != null)
		{
			renderSystem.setHighlights(targetingSystem.getMapTargetStates(cursor, selectedAbility));
			
			//if there is a selected ability tries to target a game object at the cursors location
			if(!selected.equals(map.getGameObjectAt(location)))
			{
				targetObjectAt(location);
			}

		}
		//if no selected ability then it tries to select the game object
		else
		{
			this.selectObjectAt(location);
		}

	}
	
	/**
	 * Selects an activity ability on bar and returns if castable
	 * @param i index of ability, -1 to deselect
	 * @return True if castable
	 */
	public boolean selectAbility(int i)
	{
		renderSystem.resetHighlights();
//		Gdx.app.log("Controller", "Select ability number " + i);

		if(selectedAbilites.getAbility(i).equals(selectedAbility))
		{
//			Gdx.app.log("Controller", "Ability clicked again and deselected");
			selectedAbility = null;
			return false;
		}
		else
		{
			selectedAbility = selectedAbilites.getAbility(i);
			renderSystem.setHighlights(targetingSystem.getMapTargetStates(cursor, selectedAbility));
			return this.isSelectedAbilityCastable();
		}
	}
	
	/**
	 * Attempts to cast the selected ability
	 * @return true if is castable
	 */
	public boolean castSelectedAbility()
	{
		Gdx.app.log("Controller", selectedAbility.toString() + " started casted!");
		boolean casted = managerSystem.castAbility(selectedAbility, selected, cursor);
		if(casted)
		{
			selectedAbility = null;
			this.renderSystem.resetHighlights();	
		}
		return casted;
	}
	
	/**
	 * Tries to select an object
	 * @param object Object being selected or null to deslect
	 * @return true if object was selected
	 */
	public boolean selectObject(GameObject object)
	{
		if(object != null)
		{
			selected = object.getState();
			selectedAbilites = object.getAbilities();
			return true;
		}
		else
		{
			selected = null;
			selectedAbility = null;
			return false;
		}
	}
	
	/**
	 * Tries to select object at location
	 * @param location
	 * @return true if object was selected
	 */
	public boolean selectObjectAt(Location location) {
		GameObject object = map.getGameObjectAt(location);
		return selectObject(object);
	}
	
	
	/**
	 * Tries to target an object at a location
	 * @param location of object or null to detarget
	 * @return True if object exists at location
	 */
	public boolean targetObjectAt(Location location)
	{
		GameObject object = map.getGameObjectAt(location);
		if(object != null)
		{
			targeted = object.getState();
			return true;
		}
		else
		{
			targeted = null;
			return false;
		}
	}
	
	/* Starts the turn */
	public void startTurn()
	{
		this.triggerStartOfTurnBuffs();
	}

	public void endTurn()
	{
		this.selected = null;
		this.selectedAbilites = null;
		this.selectedAbility = null;
		this.targeted = null;
		this.renderSystem.resetHighlights();
		this.triggerEndOfTurnBuffs();
		this.team.endTurn();
	}
	
	

	
	/* Triggers buffs at the start of the turn */
	private void triggerStartOfTurnBuffs()
	{
		for(GameObjectState state : map.getObjectStatesOnTeam(this.team))
		{
			for(Buff buff : state.getBuffs())
			{
//				if(buff.getParameters().get)
			}
		}
	}	
	
	/* Triggers buffs at the end of the turn */
	private void triggerEndOfTurnBuffs()
	{
		
	}
	
	/**
	 * Ends the turn, cleans up renderer first
	 */

	/**
	 * Gets a map of information about the selected object
	 * @return Map of strings or null if no object is selected
	 */
	public HashMap<String, Object> getSelectedInfo()
	{
		if(selected == null) return null;
		
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put("hp", Integer.toString(selected.getHP()));
		info.put("mana", Integer.toString(selected.getMana()));
		info.put("name", selected.getName());
		
		int i = 0;
		for(Buff buff: selected.getBuffs())
		{
			info.put("bufftooltip" + i, buff.getTooltip());
			i++;
		}
		info.put("buffcount", i);
		return info;
	}
	
	/**
	 * Gets a map of information about the targeted object
	 * @return Map of strings or null if no object is targeted
	 */
	public HashMap<String, String> getTargetedInfo()
	{
		if(targeted == null) return null;
		
		HashMap<String, String> info = new HashMap<String, String>();
		info.put("hp", Integer.toString(targeted.getHP()));
		info.put("mana", Integer.toString(targeted.getMana()));
		info.put("name", targeted.getName());
		return info;
	}
	
	public ArrayList<HashMap<String, String>> getAbilityBarInfo()
	{

		if(selected == null || selectedAbilites == null) return null;
		ArrayList<HashMap<String, String>> info = new ArrayList<HashMap<String, String>>();
		for(Ability ability : selectedAbilites.getCurrentBar())
		{
			HashMap<String, String> abilityInfo = new HashMap<String, String>();
			abilityInfo.put("name", ability.getName());
			abilityInfo.put("tooltip", ability.getToolTip());
			abilityInfo.put("manacost", Integer.toString(abilitySystem.getManaCost(ability,selected)));
			abilityInfo.put("manaless", Boolean.toString(ability.isManaless()));
			info.add(abilityInfo);
		}
		return info;
	}
	
	public int getAbilityBarCount()
	{
		return selectedAbilites.getBarSize();
	}
	
	/**
	 * Gets name of ability on bar
	 * @param i index of ability
	 * @return name of ability
	 */
	public String getAbilityName(int i)
	{
		return selectedAbilites.getAbility(i).getName();
	}
	
	/**
	 * Gets tooltip of ability on bar
	 * @param i index of ability
	 * @return name of ability
	 */
	public String getAbilityToolTip(int i)
	{
		return selectedAbilites.getAbility(i).getToolTip();
	}
	
	/**
	 * Gets mana cost of ability on bar
	 * @param i indx of ability
	 * @return mana cost of ability
	 */
	public int getAbilityManaCost(int i)
	{
		return this.abilitySystem.getManaCost(selectedAbilites.getAbility(i), selected);
	}
	
	/**
	 * Checks to see if there is a selected ability, ability is allowed to be cast, and cursor locatio is a valid target
	 */
	public boolean isSelectedAbilityCastable()
	{
		return selectedAbility != null && selected != null && managerSystem.isAbilityCastable(selectedAbility, selected, cursor);
	}

	public void setTeam(Team team) {
		this.team = team;		
	}
	public void setMapSystem(MapSystem mapSystem) {
		this.map = mapSystem;
	}
	public void setRenderSystem(RenderSystem renderSystem)
	{
		this.renderSystem = renderSystem;
	}
	public void setTargetingSystem(TargetingSystem targetingSystem) {
		this.targetingSystem = targetingSystem;
	}
	public void setAbilitySystem(ActionFactory abilitySystem) {
		this.abilitySystem = abilitySystem;
	}

	public void setManagerSystem(ManagerSystem managerSystem)
	{
		this.managerSystem = managerSystem;
	}

	

}
