package game.basic.abilities;

import garnet.ability.Ability;
import garnet.ability.action.Action;
import garnet.ability.action.Action.ActionType;
import garnet.ability.target.AbilityTargetingParameters;
import garnet.ability.target.AreaTargetingParameters;
import garnet.ability.target.TargetingParameters.TargetLocation;
import garnet.util.Location;
import garnet.util.MoveLocation;

import com.badlogic.gdx.Gdx;

public class Move extends Ability{
	public Move(int range)
	{
		this.setName("Move");
		this.setToolTip("Move up to " + range + " tiles.");
		this.setBaseManaCost(0);
		this.setManaless(true);
		AbilityTargetingParameters targeting = new AbilityTargetingParameters();
		targeting.setMaxRange(range);
		targeting.setMinRange(1);
		targeting.setTargetLocation(TargetLocation.EMPTY);
		targeting.setMovement(true);
		this.setTargetingParams(targeting);
		
		AreaTargetingParameters areaTargeting = new AreaTargetingParameters(targeting);
		this.addAreaTargetingParams(areaTargeting);
		
	}

	@Override
	public Action effects(Location target) {
		Gdx.app.log("Move", "Executing Move");
		this.getTargetingParameters().setMaxRange(this.getTargetingParameters().getSource().getMoveRange());
		MoveLocation end = this.targeting.findMovePath(this.getTargetingParameters(), target);
		
		Action root = actions.castAction(source);
		Location previousLoc = this.getTargetingParameters().getSource().getLocation();
		Action prevAction = null;
		for(Location moveStep : end.getPath())
		{
			
			Action action = actions.moveRelativeAction(source, moveStep.subtract(previousLoc));
			
			if(prevAction == null)
			{
				root = action;
			}
			else
			{
				prevAction.addChild(action);
			}
			prevAction = action;
			previousLoc = moveStep;
		}
		return root;
	}
}
