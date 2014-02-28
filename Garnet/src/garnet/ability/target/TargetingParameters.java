package garnet.ability.target;

import java.util.HashSet;

import garnet.board.TargetingSystem;
import garnet.gameobject.GameObjectState;

public class TargetingParameters {
	private TargetingSystem targetingSystem;
	
	private boolean global = false;
	private int minRange = 0;
	private int maxRange = 0;
	
	private GameObjectState source;
	
	private HashSet<GameObjectState> targetObjects = new HashSet<GameObjectState>();
	private HashSet<String> targetIDs = new HashSet<String>();
	private HashSet<String> targetClassIDs = new HashSet<String>();
	private HashSet<String> targetBuffIDs = new HashSet<String>();
	private HashSet<String> targetBuffClassIDs = new HashSet<String>();
	private TargetAssociation targetAssociation = TargetAssociation.ANY;
	private TargetLocation targetLocation = TargetLocation.ANY;
	
	public void setTargetingSystem(TargetingSystem targetingSystem)
	{
		this.targetingSystem = targetingSystem;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public int getMinRange() {
		return minRange;
	}

	public void setMinRange(int minRange) {
		this.minRange = minRange;
	}

	public int getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(int maxRange) {
		this.maxRange = maxRange;
	}

	public TargetingSystem getTargetingSystem() {
		return targetingSystem;
	}

	public GameObjectState getSource() {
		return source;
	}

	public void setSource(GameObjectState source) {
		this.source = source;
	}
	
	public HashSet<GameObjectState> getTargetObjects() {
		return targetObjects;
	}

	public void setTargetObjects(HashSet<GameObjectState> targetObjects) {
		this.targetObjects = targetObjects;
	}

	public HashSet<String> getTargetIDs() {
		return targetIDs;
	}

	public void setTargetIDs(HashSet<String> targetIDs) {
		this.targetIDs = targetIDs;
	}

	public HashSet<String> getTargetClassIDs() {
		return targetClassIDs;
	}

	public void setTargetClassIDs(HashSet<String> targetClassIDs) {
		this.targetClassIDs = targetClassIDs;
	}

	public HashSet<String> getTargetBuffIDs() {
		return targetBuffIDs;
	}

	public void setTargetBuffIDs(HashSet<String> targetBuffIDs) {
		this.targetBuffIDs = targetBuffIDs;
	}

	public HashSet<String> getTargetBuffClassIDs() {
		return targetBuffClassIDs;
	}

	public void setTargetBuffClassIDs(HashSet<String> targetBuffClassIDs) {
		this.targetBuffClassIDs = targetBuffClassIDs;
	}

	public TargetAssociation getTargetAssociation() {
		return targetAssociation;
	}

	public void setTargetAssociation(TargetAssociation targetAssociation) {
		this.targetAssociation = targetAssociation;
	}

	public TargetLocation getTargetLocation() {
		return targetLocation;
	}

	public void setTargetLocation(TargetLocation targetLocation) {
		this.targetLocation = targetLocation;
	}

	public enum TargetAssociation
	{
		ENEMY, ALLY, ANY
	}
	
	public enum TargetLocation
	{
		/** Any location in range */
		ANY, 
		/** Location must be empty */
		EMPTY, 
		/** Target in range the conforms to team, ID, and classID rules 
		 * Target object for targeted effects and target of event for triggered effects
		 */
		OBJECT
	}
}
