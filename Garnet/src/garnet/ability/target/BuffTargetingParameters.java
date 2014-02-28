package garnet.ability.target;

import garnet.ability.action.Action.ActionType;

/**
 * Contains targeting parameters for an action listener. By default parameters match the target of the action.
 * @author Aaron
 *
 */
public class BuffTargetingParameters extends TargetingParameters{
	private boolean sourceListener = false;
	private ActionType actionType;
	private ListenerType listenerType;
			
	/**
	 * @return True if targeting parameters determine action source, false if not.
	 */
	public boolean isSourceListener() {
		return sourceListener;
	}

	/**
	 * Defines if listener is looking at source or target of action. Is target by default
	 * @param sourceListener
	 */
	public void setSourceListener(boolean sourceListener) {
		this.sourceListener = sourceListener;
	}


	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public ListenerType getListenerType() {
		return listenerType;
	}

	/*
	 * Set what stage the listener will be notified at
	 */
	public void setListenerType(ListenerType listenerType) {
		this.listenerType = listenerType;
	}

	/**
	 * What stage that the listenerer will be notified and some constraints on which will be notified
	 * @author Aaron
	 *
	 */
	public enum ListenerType
	{
		/* Triggers after front swing but before actual cast. All listeners are notified */
		INTERRUPT,
		/* Replaces an action with another action. Only the source and target notified. Use auras for a buff that gives other users a replace trigger */
		REPLACE,
		/* Queues an action queue after one is completed, all listeners are notified. */
		COUNTER,
		OWN_TURN_START,
		OWN_TURN_END,
		OTHER_TURN_START,
		OTHER_TURN_END,
		ANY_TURN_START,
		ANY_TURN_END,
	}
	
}
