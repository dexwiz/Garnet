package game.basic.abilities;

import com.badlogic.gdx.Gdx;
import garnet.ability.Ability;
import garnet.ability.action.Action;
import garnet.ability.action.Action.ActionType;
import garnet.ability.target.AbilityTargetingParameters;
import garnet.ability.target.AreaTargetingParameters;
import garnet.ability.target.TargetingParameters.TargetAssociation;
import garnet.ability.target.TargetingParameters.TargetLocation;
import garnet.gameobject.GameObjectState;
import garnet.util.Location;

public class Attack extends Ability{
	
	private int damage;
	
	public Attack(int attack)
	{
		this.setName("Attack");
		this.setToolTip("Attack for " + attack + " damage.");
		this.setBaseManaCost(0);
		this.setManaless(true);
		AbilityTargetingParameters targeting = new AbilityTargetingParameters();
		targeting.setMaxRange(5);
		targeting.setMinRange(1);
		targeting.setTargetAssociation(TargetAssociation.ENEMY);
		targeting.setTargetLocation(TargetLocation.OBJECT);
		this.setTargetingParams(targeting);
		
		AreaTargetingParameters areaTargeting = new AreaTargetingParameters(targeting);
		this.addAreaTargetingParams(areaTargeting);
		
		damage = attack;
	}

	@Override
	public Action effects(Location target) {
		Gdx.app.log("Attack", "Executing Attack");
		Action root = actions.castAction(source);
		for(GameObjectState object : this.targeting.getAreaEffectedObjects(target, this.getAreaTargetingParameters().get(0)))
		{
			Action action = actions.damageAction(source, object, damage);
			root.addChild(action);
		}
		return root;
	}

}
