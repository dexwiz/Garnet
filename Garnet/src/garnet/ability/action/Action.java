package garnet.ability.action;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import garnet.ability.Buff;
import garnet.ability.action.animation.Animation;
import garnet.board.ActionSystem;
import garnet.gameobject.GameObjectState;
import garnet.util.Location;

/**
 * Base action class. Ordered in a tree, created by 
 * @author Aaron
 *
 */
public class Action {
	private ActionSystem system;

	private Action parent;
	private ArrayList<Action> children = new ArrayList<Action>();

	private Animation animation;

	private GameObjectState source;
	private GameObjectState target;
	
	private int value = Integer.MIN_VALUE;
	private Location targetLocation;
	private Buff status;
	private ActionType type = ActionType.NONE;
	
	private ArrayList<String> modifiedBy = new ArrayList<String>();
	
	private String ID = "";
		
	//package constructor to force using the facotry
	Action(){}
	
	/**
	 * Start is called well relevant actions are given to other systems, allows action system to listen for callback
	 * @param system
	 */
	public void start(ActionSystem system)
	{
		this.system = system;
	}
	
	/**
	 * Called by animation system when animation is done
	 */
	public void animationComplete()
	{
		this.system.actionComplete(this);
	}
	

	/**
	 * Adds a child action and sets its parent action
	 * @param action
	 */
	public void addChild(Action action)
	{	
		if(action != null)
		{
			this.children.add(action);
			action.parent = this;
		}
	}
	
	/**
	 * Replaces action in flow with, migrates children, adds parent action, and removes current action from parent
	 * @param action
	 */
	public void replaceAction(Action action)
	{
		for(Action child : children)
		{
			action.addChild(child);
		}
		action.source = this.source;
		this.parent.children.remove(this);
		this.parent.addChild(action);
	}
	
//	/**
//	 * Adds an action to a parent actions
//	 * @param parent
//	 */
//	public void setParent(Action parent)
//	{
//		this.parent = parent;
//		parent.addChild(this);
//	}
	
	/**
	 * @return list of direct children actions
	 */
	public ArrayList<Action> getChildren()
	{
		return this.children;
	}
	
	/**
	 * @return A list of actions in the flow in a breadth first search pattern
	 */
	public ArrayList<Action> getFlowAsList()
	{
		ArrayList<Action> tree = new ArrayList<Action>();
		Queue<Action> queue = new LinkedList<Action>();
		queue.add(this);
		while(!queue.isEmpty())
		{
			Action action = queue.remove();
			tree.add(action);
			for(Action child : action.children)
			{
				queue.add(child);
			}
		}
		return tree;
	}
	
	public boolean hasChildren()
	{
		return !this.children.isEmpty();
	}
	

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animationAction) {
		this.animation = animationAction;
		if(this.animation != null) this.animation.setParentAction(this);
	}

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public GameObjectState getSource() {
		return source;
	}

	public void setSource(GameObjectState source) {
		this.source = source;
	}

	public GameObjectState getTarget() {
		return target;
	}

	public void setTarget(GameObjectState target) {
		this.target = target;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Location getTargetLocation() {
		return targetLocation;
	}

	public void setTargetLocation(Location targetLocation) {
		this.targetLocation = targetLocation;
	}

	public Buff getBuff() {
		return status;
	}

	public void setBuff(Buff status) {
		this.status = status;
	}
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public enum ActionType
	{
		NONE,
		SPEND_MANA,
		FRONT_SWING,
		BACK_SWING,
		CAST,
		DAMAGE,
		HEAL,
		SHIELD,
		AURA,
		MANA_GAIN,
		MANA_BURN,
		PLACE_GAME_OBJECT,
		REMOVE_GAME_OBJECT,
		MOVE_DIRECT,
		MOVE_RELATIVE,
		APPLY_BUFF,
		REMOVE_BUFF
	}

	/**
	 * @return The root action of an actiont tree
	 */
	public Action getRoot() {
		if(this.parent == null)
		{
			return this;
		}
		else
		{
			return this.parent.getRoot();
		}
	}
	
}
