package garnet.ability;

import java.util.ArrayList;
import java.util.HashSet;

import garnet.ability.action.Action;
import garnet.ability.action.ActionFactory;
import garnet.ability.modifier.ActionModifier;
import garnet.ability.modifier.StateModifier;
import garnet.ability.target.BuffTargetingParameters;

import garnet.gameobject.GameObjectState;

public abstract class Buff {
	private ArrayList<ActionModifier> actionMods = new ArrayList<ActionModifier>();
	private ArrayList<StateModifier> stateMods = new ArrayList<StateModifier>();
	
	private String ID = "default";
	private String name = "default";
	private String tooltip = "deafult tooltip";
	
	private HashSet<String> classIDs = new HashSet<String>();
	private GameObjectState owner;
	private BuffTargetingParameters parameters;
	protected ActionFactory actions;
	protected GameObjectState source;
	protected GameObjectState holder;
	
	public abstract Action effects(Action action);
	
	public ArrayList<ActionModifier> getActionMods() {
		return actionMods;
	}
	
	public ArrayList<StateModifier> getStateMods() {
		return stateMods;
	}
	
	public void addActionModifier(ActionModifier mod)
	{
		this.actionMods.add(mod);
	}
	
	public void addStateModifier(StateModifier mod)
	{
		this.stateMods.add(mod);
	}

	public BuffTargetingParameters getParameters() {
		return parameters;
	}

	public void setParameters(BuffTargetingParameters parameters) {
		this.parameters = parameters;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public HashSet<String> getClassIDs() {
		return classIDs;
	}

	public void setClassIDs(HashSet<String> classIDs) {
		this.classIDs = classIDs;
	}

	public GameObjectState getOwner() {
		return owner;
	}

	public void setOwner(GameObjectState owner) {
		this.owner = owner;
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actions = actionFactory;		
	}

	public void setSource(GameObjectState state) {
		this.source = state;		
	}

	public void setHolder(GameObjectState state) {
		this.holder = state;
		
	}

}
