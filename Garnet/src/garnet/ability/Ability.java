package garnet.ability;

import java.util.ArrayList;

import garnet.ability.action.Action;
import garnet.ability.action.ActionFactory;
import garnet.ability.target.AbilityTargetingParameters;
import garnet.ability.target.AreaTargetingParameters;
import garnet.board.TargetingSystem;
import garnet.gameobject.GameObjectState;
import garnet.util.Location;

public abstract class Ability {
	private String name = "default";
	private String toolTip = "Cast it, fucker!";
	private int manaCost = 0;
	private boolean manaless = false;

	private AbilityTargetingParameters targetingParams = null;
	private ArrayList<AreaTargetingParameters> areaTargetingParams = new ArrayList<AreaTargetingParameters>();
	protected TargetingSystem targeting;
	protected ActionFactory actions;
	protected GameObjectState source;
	
	abstract public Action effects(Location target);
	

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	/**
	 * Gets the base mana cost, should not be called directly. To get real mana cost use the ability system function getManaCost instead
	 * @return base mana cost
	 */
	public int getBaseManaCost() {
		return manaCost;
	}

	public boolean isManaless() {
		return manaless;
	}

	public void setManaless(boolean manaless) {
		this.manaless = manaless;
	}

	public void setBaseManaCost(int manaCost) {
		this.manaCost = manaCost;
	}

	public AbilityTargetingParameters getTargetingParameters() {
		return targetingParams;
	}

	public void setTargetingParams(AbilityTargetingParameters targeting) {
		this.targetingParams = targeting;
	}

	public void addAreaTargetingParams(AreaTargetingParameters targeting) {
		this.areaTargetingParams.add(targeting);
	}

	public ArrayList<AreaTargetingParameters> getAreaTargetingParameters() {
		return areaTargetingParams;
	}
	
	public void setSource(GameObjectState object)
	{
		this.source = object;
	}
	public void setTargetingSystem(TargetingSystem targetingSystem) {
		this.targeting = targetingSystem;		
	}
	public void setActionFactory(ActionFactory actionFactory) {
		this.actions = actionFactory;
	}
	
	
}
