package game.basic.buffs;

import java.util.HashSet;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import garnet.ability.Buff;
import garnet.ability.action.Action;
import garnet.ability.action.Action.ActionType;
import garnet.ability.modifier.ActionModifier;
import garnet.ability.modifier.ActionModifier.ActionModifierType;
import garnet.ability.modifier.StateModifier;
import garnet.ability.modifier.StateModifier.StateModifierType;
import garnet.ability.target.BuffTargetingParameters;
import garnet.board.ActionSystem;
import garnet.gameobject.GameObjectState;

public class Strength extends Buff{

	public Strength()
	{
		ActionModifier mod = new ActionModifier(ActionModifierType.DAMAGE_OUT_INCREASE, 4);
		this.addActionModifier(mod);
		
		StateModifier mod2 = new StateModifier(StateModifierType.MOVEMENT, 2);
//		this.addStateModifier(mod2);
		
		BuffTargetingParameters params = new BuffTargetingParameters()
		{

			@Override
			public HashSet<GameObjectState> getTargetObjects() {
				HashSet<GameObjectState> target = new HashSet<GameObjectState>();
				target.add(this.getSource());
				return target;
			}
			
		};
		params.setGlobal(true);
		params.setActionType(ActionType.DAMAGE);
//		this.setParameters(params);
	}

	@Override
	public Action effects(Action action) {
		
		return actions.shieldAction(source, holder);

	}
}
