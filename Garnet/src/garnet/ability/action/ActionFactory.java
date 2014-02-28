package garnet.ability.action;

import com.badlogic.gdx.Gdx;

import garnet.ability.Ability;
import garnet.ability.Buff;
import garnet.ability.action.Action.ActionType;
import garnet.ability.action.animation.Animation;
import garnet.ability.action.animation.DelayAnimation;
import garnet.ability.action.animation.MoveByAnimation;
import garnet.ability.modifier.ActionModifier;
import garnet.ability.modifier.ActionModifier.ActionModifierType;
import garnet.board.TargetingSystem;
import garnet.gameobject.GameObjectState;
import garnet.util.Location;

/**
 * System responsible for assembling action flow based on ability.
 * Will announce flow for modifiers and interrupts before passing off to action system for processing.
 * Will announce flow for reactions after action system has processed the flow
 * @author Aaron
 *
 */
public class ActionFactory {
	
	private TargetingSystem targetingSystem;

	/**
	 * Gets the cost of an ability. 
	 * @param ability
	 * @param source
	 * @return cost of ability.
	 */
	public int getManaCost(Ability ability, GameObjectState source)
	{
		//if manaless ability then mana cost cannot be changed
		if(ability.isManaless()) return 0;
		
		Action cost = new Action();
		cost.setSource(source);
		cost.setType(ActionType.SPEND_MANA);
		cost.setValue(ability.getBaseManaCost());
		this.modifyCost(cost);
		return cost.getValue();
	}
	
	/**
	 * Constructs an action flow based on an abililty. Will announce and alter flow based on modifierers and interruptors on statuses
	 * @param ability
	 * @return Root action of fully modified flow
	 */
	public Action constructFlow(Ability ability, GameObjectState source, Location target)
	{
		Gdx.app.log("Ability System", "Constructing flow for " + ability.toString());
		Action root = this.sepndMana(source, ability.getBaseManaCost());
//		root.setAnimation(new Animation());
		
		Action front = this.castFrontSwing(source);
//		front.setAnimation(new Animation());
		root.addChild(front);
		
		Action back = this.castBackSwing(source);
//		back.setAnimation(new Animation());
		front.addChild(back);
		
		Action cast = this.castAction(source);
		front.addChild(cast);
		
		Action effect = ability.effects(target);
		cast.addChild(effect);
		for(Action effectAction: effect.getFlowAsList())
		{
			Gdx.app.log("Ability System", "Adding ability effect: " + effectAction.getType() + " from " + ability.toString());
			effectAction.setSource(source);
		}

		
		interruptFlow(root);
		replaceFlow(root);
		modifyFlow(root);	

		for(Action effectAction: effect.getFlowAsList())
		{
			if(effectAction.getAnimation() == null) effectAction.setAnimation(getAnimationForAction(effectAction));
		}
		return root;
	}


	/**
	 * Scans all interrupters and offers chance to interrupt flow based on actions it contains
	 * @param Root action of flow
	 */
	private void interruptFlow(Action action)
	{
		
	}
	
	
	private void replaceFlow(Action root) {
		
		search:
		for(Action action : root.getFlowAsList())
		{
//			Gdx.app.log("Ability", "Looking to repalce " + action.getType() + " action");
			
			//announce to target buffs
			if(action.getTarget() != null)
			{
				for(Buff buff : action.getTarget().getBuffs())
				{
//					Gdx.app.log("Ability", "Inspecting buffs on target");

					if(buff.getParameters() != null)
					{
//						Gdx.app.log("Ability", "Checking " + buff.toString() + " buff on target");
						if(this.targetingSystem.isValidActionListener(action, buff.getParameters()))
						{
							Gdx.app.log("Ability", "Need to replace " + action.getType() + " action targeting " + action.getTarget().toString());
							action.replaceAction(buff.effects(action));
							continue search;
						}
					}
				}
			}

			//announce to source buffs
			for(Buff buff : action.getSource().getBuffs())
			{
				if(buff.getParameters() != null)
				{
					if(this.targetingSystem.isValidActionListener(action, buff.getParameters()))
					{
						Gdx.app.log("Ability", "Need to replace " + action.getType() + " action from " + action.getTarget().toString());

						action = buff.effects(action);
					}
				}
			}

		}
		
	}

	public Action damageAction(GameObjectState source, GameObjectState target, int amount)
	{
		Action action = new Action();
		action.setType(ActionType.DAMAGE);
		action.setValue(amount);
		action.setTarget(target);
		action.setSource(source);
		action.setAnimation(getAnimationForAction(action));
		return action;
	}
	
	public Action healAction(GameObjectState source, GameObjectState target, int amount)
	{
		Action action = new Action();
		action.setType(ActionType.HEAL);
		action.setValue(amount);
		action.setTarget(target);
		action.setSource(source);
		action.setAnimation(getAnimationForAction(action));
		return action;
	}
	
	public Action manaBurnAction(GameObjectState source, GameObjectState target, int amount)
	{
		Action action = new Action();
		action.setType(ActionType.MANA_BURN);
		action.setValue(amount);
		action.setTarget(target);
		action.setSource(source);
		action.setAnimation(getAnimationForAction(action));
		return action;
	}
	
	public Action manaGainAction(GameObjectState source, GameObjectState target, int amount)
	{
		Action action = new Action();
		action.setType(ActionType.MANA_GAIN);
		action.setValue(amount);
		action.setTarget(target);
		action.setSource(source);
		action.setAnimation(getAnimationForAction(action));
		return action;
	}
	
	public Action shieldAction(GameObjectState source, GameObjectState target)
	{
		Action action = new Action();
		action.setType(ActionType.SHIELD);
		action.setTarget(target);
		action.setSource(source);
		action.setAnimation(getAnimationForAction(action));
		return action;
	}
	
	public Action applyBuffAction(GameObjectState source, GameObjectState target, Buff buff)
	{
		Action action = new Action();
		action.setType(ActionType.APPLY_BUFF);
		action.setBuff(buff);
		action.setTarget(target);
		action.setSource(source);
		action.setAnimation(getAnimationForAction(action));
		return action;
	}
	
	public Action removeBuffAction(GameObjectState source, GameObjectState target, Buff buff)
	{
		Action action = new Action();
		action.setType(ActionType.REMOVE_BUFF);
		action.setBuff(buff);
		action.setTarget(target);
		action.setSource(source);
		action.setAnimation(getAnimationForAction(action));
		return action;
	}
	
	public Action moveRelativeAction(GameObjectState source, Location targetLocation)
	{
		return moveRelativeAction(source, source, targetLocation);
	}
	
	public Action moveRelativeAction(GameObjectState source, GameObjectState target, Location targetLocation)
	{
		Action action = new Action();
		action.setType(ActionType.MOVE_RELATIVE);
		action.setTarget(target);
		action.setSource(source);
		action.setTargetLocation(targetLocation);
		action.setAnimation(getAnimationForAction(action));
		return action;
	}
	
	public Action moveDirectAction(GameObjectState source, Location targetLocation)
	{
		return moveDirectAction(source, source, targetLocation);
	}
	
	public Action moveDirectAction(GameObjectState source, GameObjectState target, Location targetLocation)
	{
		Action action = new Action();
		action.setType(ActionType.MOVE_DIRECT);
		action.setSource(source);
		action.setTarget(target);
		action.setTargetLocation(targetLocation);
		action.setAnimation(getAnimationForAction(action));
		return action;
	}
	
	public Action sepndMana(GameObjectState caster, int i)
	{
		Action action = new Action();
		action.setType(ActionType.SPEND_MANA);
		action.setValue(i);
		action.setSource(caster);
		action.setTarget(caster);
		action.setAnimation(getAnimationForAction(action));
		return action;
	}
	
	public Action castAction(GameObjectState caster)
	{
		Action action = new Action();
		action.setType(ActionType.CAST);
		action.setSource(caster);
		action.setTarget(caster);
		action.setAnimation(getAnimationForAction(action));
		return action;
	}
	
	public Action castFrontSwing(GameObjectState caster)
	{
		Action action = new Action();
		action.setType(ActionType.FRONT_SWING);
		action.setSource(caster);
		action.setTarget(caster);
		action.setAnimation(getAnimationForAction(action));
		return action;
	}
	
	public Action castBackSwing(GameObjectState caster)
	{
		Action action = new Action();
		action.setType(ActionType.BACK_SWING);
		action.setSource(caster);
		action.setTarget(caster);
		action.setAnimation(getAnimationForAction(action));
		return action;
	}
	
	public Action death(GameObjectState dead)
	{
		Action death = new Action();
		death.setType(ActionType.REMOVE_GAME_OBJECT);
		death.setTarget(dead);
		death.setSource(dead);
		this.getAnimationForAction(death);
		return death;
	}
	
	public boolean validateAction(Action action)
	{
		//If there is no source, its always invalid
		if(action.getSource() == null) 
		{
			Gdx.app.log("Action System", "No source for action");

			return false;
		}
		
		
		boolean valid = true;
		switch(action.getType())
		{
		case APPLY_BUFF:	
		case REMOVE_BUFF:
			if(action.getTarget() == null)
			{
				Gdx.app.log("Action System", "No target to remove/apply status");
				valid = false;
			}
			else if(action.getBuff() == null)
			{
				Gdx.app.log("Action System", "No status to remove/apply ");
				valid = false;
			}
			break;
		case DAMAGE:
		case HEAL:
		case MANA_BURN:
		case MANA_GAIN:
		case SPEND_MANA:
			if(action.getTarget() == null)
			{
				Gdx.app.log("Action System", "No target to remove/apply status");
				valid = false;
			}
			else if(action.getValue() == Integer.MIN_VALUE)
			{
				Gdx.app.log("Action System", "No value set for damage/heal/mana burn/mana gain/spend mana");
				valid = false;
			}
			break;
		case SHIELD:
			if(action.getTarget() == null)
			{
				Gdx.app.log("Action System", "No Target to sheild");
				valid = false;
			}
			break;
		case MOVE_RELATIVE:
		case MOVE_DIRECT:
		case PLACE_GAME_OBJECT:
			if(action.getTargetLocation() == null) 
			{
				Gdx.app.log("Action System", "No location to move/place game object");
				valid = false;
			}
			break;
		case REMOVE_GAME_OBJECT:
			if(action.getTarget() == null)
			{
				Gdx.app.log("Action System", "No objec to be removed");
				valid = false;
			}
			break;
		case FRONT_SWING:
		case BACK_SWING:
		case CAST:
		case NONE:
			break;
		default:
			Gdx.app.log("Action System", "No action type set");
			valid = false;
			break;
		}
		return valid;
	}
	
	/**
	 * Scans all modifiers and offers chnace to modify actions in the flow
	 * @param Root action of flow
	 */
	private void modifyFlow(Action rootAction)
	{
		for(Action action : rootAction.getFlowAsList())
		{
			switch(action.getType())
			{
			case DAMAGE:
				modifyDamage(action);
			case HEAL:
				modifyHeal(action);
			case SHIELD:
				break;
			case MOVE_RELATIVE:
			case MOVE_DIRECT:
				break;
			case APPLY_BUFF:
				break;
			case AURA:
				break;
			case BACK_SWING:
				break;
			case CAST:
				break;
			case FRONT_SWING:
				break;
			case MANA_BURN:
				break;
			case MANA_GAIN:
				break;
			case NONE:
				break;
			case PLACE_GAME_OBJECT:
				break;
			case REMOVE_GAME_OBJECT:
				break;
			case REMOVE_BUFF:
				break;
			case SPEND_MANA:
				modifyCost(action);

				break;
			default:
				break;
			}

		}
	}
		
	private void modifyCost(Action action)
	{	
		int cost = action.getValue();
		for(Buff buff : action.getSource().getBuffs())
		{
			for(ActionModifier mod : buff.getActionMods())
			{
				if(mod.getType() == ActionModifierType.COST_DECREASE)
				{
					cost -= mod.getValue();
				}
				else if(mod.getType() == ActionModifierType.COST_INCREASE)
				{
					cost += mod.getValue();
				}
			}
		}
		if(cost < 0) cost = 0;
		
		action.setValue(cost);
	}
	
	private void modifyDamage(Action action)
	{
		int damage = action.getValue();
		
		//gets damage mods on target
		for(Buff buff : action.getTarget().getBuffs())
		{
			for(ActionModifier mod : buff.getActionMods())
			{
				if(mod.getType() == ActionModifierType.DAMAGE_IN_DECREASE)
				{
					damage -= mod.getValue();
				}
				else if(mod.getType() == ActionModifierType.DAMAGE_IN_INCREASE)
				{
					damage += mod.getValue();
				}
			}
		}
		
		//gets damage increasing mods on source
		for(Buff buff : action.getSource().getBuffs())
		{
			for(ActionModifier mod : buff.getActionMods())
			{
				if(mod.getType() == ActionModifierType.DAMAGE_OUT_DECREASE)
				{
					damage -= mod.getValue();
				}
				else if(mod.getType() == ActionModifierType.DAMAGE_OUT_INCREASE)
				{
					damage += mod.getValue();
				}
			}
		}
		
		//damage cannot be below 0
		if(damage < 0) damage = 0;
		
		action.setValue(damage);
	}
	
	private void modifyHeal(Action action)
	{
		int heal = action.getValue();
		
		//gets heal effects on ta
		for(Buff buff : action.getTarget().getBuffs())
		{
			for(ActionModifier mod : buff.getActionMods())
			{
				if(mod.getType() == ActionModifierType.HEAL_IN_DECREASE)
				{
					heal -= mod.getValue();
				}
				else if(mod.getType() == ActionModifierType.HEAL_IN_INCREASE)
				{
					heal += mod.getValue();
				}
			}
		}
		
		//gets damage increasing mods on source
		for(Buff buff : action.getSource().getBuffs())
		{
			for(ActionModifier mod : buff.getActionMods())
			{
				if(mod.getType() == ActionModifierType.HEAL_OUT_DECREASE)
				{
					heal -= mod.getValue();
				}
				else if(mod.getType() == ActionModifierType.HEAL_OUT_INCREASE)
				{
					heal += mod.getValue();
				}
			}
		}
		
		
		//damage cannot be below 0
		if(heal < 0) heal = 0;
		
		action.setValue(heal);
	}
		
	/**
	 * Generates an animation based on action type. Useful for generating generic animations for damage, heal, manaburn, etc
	 * @param type of animation
	 * @return Animation action based on action type
	 */
	private Animation getAnimationForAction(Action action)
	{
		Animation animation = null;
		switch (action.getType())
		{
		case MOVE_RELATIVE:
			Location move = action.getTargetLocation();
			animation = new MoveByAnimation(action.getTarget().getRender(), .4f, 32*(move.x), 32*(move.y));
			break;
		case MOVE_DIRECT:
			break;
		case SHIELD:
		case APPLY_BUFF:
		case AURA:
		case BACK_SWING:
		case CAST:
		case DAMAGE:
		case FRONT_SWING:
		case HEAL:
		case MANA_BURN:
		case MANA_GAIN:
		case NONE:
		case PLACE_GAME_OBJECT:
		case REMOVE_GAME_OBJECT:
		case REMOVE_BUFF:
		case SPEND_MANA:
		default:
			animation = new DelayAnimation(action.getSource().getRender(), 1/120f);
			break;
		}
		return animation;

	}

	public void setTargetingSystem(TargetingSystem targetingSystem) {
		this.targetingSystem = targetingSystem;		
	}
}
