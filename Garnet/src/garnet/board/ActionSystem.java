package garnet.board;


import garnet.ability.action.Action;
import garnet.ability.action.Action.ActionType;
import garnet.gameobject.GameObjectState;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

public class ActionSystem {
	

	private AnimationSystem animationSystem;
	
	private ArrayList<Action> waitingOnCompletion = new ArrayList<Action>();
	private ArrayList<GameObjectState> states = new ArrayList<GameObjectState>();

	private MapSystem mapSystem;

	private ManagerSystem managerSystem;
	
	/**
	 * Executes an set of actions
	 * @param action root action to be processed
	 */
	public void execute(Action action)
	{
		Gdx.app.log("Action System", "Received root action of flow");
		executeGameAction(action);
		waitingOnCompletion.add(action);
	}

	/**
	 * Call back fuction for action to process the next one
	 * @param action
	 */
	public void actionComplete(Action action) {
		waitingOnCompletion.remove(action);
		Gdx.app.log("Action System", "Action " + action.getType() + " is complete ");
		for(Action childAction : action.getChildren())
		{
			waitingOnCompletion.add(childAction);
			executeGameAction(childAction);

		}
		
		//if there is nothing left waiting for completion then calls back to manager
		if(this.waitingOnCompletion.size() == 0)
		{
			Gdx.app.log("Action System", "Finished executing " + action.getType() + " action and no further actions are being processed");
			managerSystem.finishAction(action.getRoot());
		}
		else
		{
			for(Action wait : waitingOnCompletion)
			{
				Gdx.app.log("Action System", "Action " + wait.getType() + " is still running ");

			}
		}

	}



	public void setAnimationSystem(AnimationSystem animationSystem) {
		this.animationSystem = animationSystem;
	}
	
	private void executeGameAction(Action action)
	{
		Gdx.app.log("Action System", "Executing Action " + action.getType());
		action.start(this);
		switch(action.getType())
		{

		case DAMAGE:
			executeDamage(action);
			break;
		case HEAL:
			executeHeal(action);
			break;
		case SHIELD:
			executeShield(action);
		case MANA_BURN:
			executeManaBurn(action);
			break;
		case MANA_GAIN:
			executeManaGain(action);
			break;
		case MOVE_RELATIVE:
			executeRelativeMove(action);
			break;
		case MOVE_DIRECT:
			executeDirectMove(action);
			break;
		case PLACE_GAME_OBJECT:
			executePlaceGameObject(action);
			break;
		case REMOVE_GAME_OBJECT:
			executeRemoveGameObject(action);
			break;
		case REMOVE_BUFF:
			executeRemoveBuff(action);
			break;
		case APPLY_BUFF:
			executeApplyBuff(action);
			break;
		default:
			break;
		}
		
		//starts the animation if there is one, otherwise just completes the action
		if(action.getAnimation() != null)
		{
			animationSystem.addAnimation(action.getAnimation());
		}
		else
		{
			action.animationComplete();
		}

	}
	


	private void executeDamage(Action action) {
		int damage = action.getValue();
		GameObjectState target = action.getTarget();
		int currentHP = target.getHP();
		int finalHP = currentHP - damage;
		target.setHP(finalHP);

	}
	
	private void executeHeal(Action action) {
		int heal = action.getValue();
		GameObjectState target = action.getTarget();
		int currentHP = target.getHP();
		int finalHP = currentHP + heal;
		target.setHP(finalHP);
	}
	
	private void executeShield(Action action) {
		// TODO Auto-generated method stub
	}

	private void executeRelativeMove(Action action) {
		mapSystem.moveGameObject(action.getTarget(), action.getTargetLocation().add(action.getTarget().getLocation()));
	}
	
	private void executeDirectMove(Action action)
	{
		mapSystem.moveGameObject(action.getTarget(), action.getTargetLocation());
	}
	
	private void executeRemoveBuff(Action action) {
				
	}

	private void executeRemoveGameObject(Action action) {
		mapSystem.removeGameObject(action.getTarget());		
	}

	private void executePlaceGameObject(Action action) {
		// TODO Auto-generated method stub
		
	}

	private void executeManaGain(Action action) {
		// TODO Auto-generated method stub
		
	}

	private void executeManaBurn(Action action) {
		// TODO Auto-generated method stub
		
	}

	private void executeApplyBuff(Action action) {
		// TODO Auto-generated method stub
		
	}

	public void addState(GameObjectState state)
	{
		this.states.add(state);
	}

	public void setMapSystem(MapSystem map) {
		this.mapSystem = map;		
	}

	public void setManagerSystem(ManagerSystem managerSystem) {
		this.managerSystem = managerSystem;		
	}
}
