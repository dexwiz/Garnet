package garnet.board;

import garnet.ability.Ability;
import garnet.ability.Buff;
import garnet.ability.action.Action;
import garnet.ability.target.AbilityTargetingParameters;
import garnet.ability.target.AreaTargetingParameters;
import garnet.ability.target.BuffTargetingParameters;
import garnet.ability.target.TargetingParameters;
import garnet.ability.target.TargetingParameters.TargetAssociation;
import garnet.ability.target.TargetingParameters.TargetLocation;
import garnet.gameobject.GameObjectState;
import garnet.util.Location;
import garnet.util.MoveLocation;
import garnet.util.MoveLocation.Dir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import com.badlogic.gdx.Gdx;
import com.google.common.collect.Sets;

public class TargetingSystem {
	private MapSystem mapSystem;
	

	
	/**
	 * Generates a map of locations that are in range, targetable, and effected by current ability and a target location.
	 * Effected overrides targetable overrides inrange
	 * @param target
	 * @param ability
	 * @return Map of lcoations and their targetstate
	 */
	public HashMap<Location, TargetState> getMapTargetStates(Location target, Ability ability)
	{
		HashMap<Location, TargetState> targets = new HashMap<Location, TargetState>();
		
		//first finds all locations in range, if targetable makrs them as targetable
		for(Location location: this.getLocationsInAbilityRange(ability.getTargetingParameters()))
		{
			if(this.isValidAbilityTarget(location, ability.getTargetingParameters()))
			{
				targets.put(location, TargetState.TARGETABLE);
			}
			else
			{
				targets.put(location, TargetState.INRANGE);
			}
		}
		
		//if current target is a valid target then gets effected locations
		if(this.isValidAbilityTarget(target, ability.getTargetingParameters()))
		{
			for(AreaTargetingParameters areaParam : ability.getAreaTargetingParameters())
			{
				Gdx.app.log("Targeting", "Checking " + areaParam.toString());
				for(Location location : this.getAreaEffectedLocations(target, areaParam))
				{
					targets.put(location, TargetState.EFFECTED);
				}
			}
		}
	
		return targets;
	}
	
	/**
	 * Checks to see if location is a valid ability target
	 * @param target
	 * @param parameters
	 * @return True if valid place to cast ability
	 */
	public boolean isValidAbilityTarget(Location target, AbilityTargetingParameters parameters)
	{
		//if targetless anything is valid
		if(parameters.isTargetless()) return true;
		
		//must be in range
		if(!isInRange(target, parameters))
		{
			return false;
		}
		
		//must match location type
		if(!isMatchingLocation(target, parameters)) 
		{
			return false;
		}
		
		//if its not any location, target state must match type
		if(parameters.getTargetLocation() == TargetLocation.OBJECT && !isMatchingState(target, parameters)) 
		{
			return false;
		}

//		Gdx.app.log("Targeting", target.toString() + " is a valid location for cast");
		return true;
	}
	
	public boolean isValidActionListener(Action action, BuffTargetingParameters parameters)
	{
		if(action.getType() != parameters.getActionType()) return false;
		
		//sets the location, if source listenering then source's location, other wise it is target's location or target location
		//if neither exist then the listner is invalid returns false
		Location target;
		if(parameters.isSourceListener())
		{
			target = action.getSource().getLocation();
		}
		else if(action.getTarget() != null)
		{
			target = action.getTarget().getLocation();
		}
		else if(action.getTargetLocation() != null)
		{
			target = action.getTargetLocation();
		}
		else
		{
			Gdx.app.log("Targeting", "Action target listener is looking at action with no target or target location");
			return false;
		}
		
		//must be in range
		if(!isInRange(target, parameters))
		{
			return false;
		}
		
		//must match location type
		if(!isMatchingLocation(target, parameters)) 
		{
			return false;
		}
		
		//if its not any location, target state must match type
		if(parameters.getTargetLocation() != TargetLocation.ANY && !isMatchingState(target, parameters)) 
		{
			return false;
		}
		
		return true;
	
	}
	
	/**
	 * Gets all locations in area
	 * @param target
	 * @param parameters
	 * @return List of all locations, will return empty locations if object targted, but only empty locations
	 * if empty targeted
	 */
	public ArrayList<Location> getAreaEffectedLocations(Location target, AreaTargetingParameters parameters)
	{
		ArrayList<Location> locations = new ArrayList<Location>();
		for(Location relativeLocation : parameters.getArea().getRelativeLocations())
		{
			Location realLocation = new Location(target.x + relativeLocation.x,target.y + relativeLocation.y);
			if(mapSystem.locationExists(realLocation))
			{
				if(parameters.getTargetLocation() == TargetLocation.EMPTY 
						&& mapSystem.getObjectSateAt(realLocation) == null)
				{
					locations.add(realLocation);
				}
				else
				{
					locations.add(realLocation);
				}

			}
		}
		return locations;
	}
	
	/**
	 * Gets objects effect by area targeted at location
	 * @return
	 */
	public ArrayList<GameObjectState> getAreaEffectedObjects(Location target, AreaTargetingParameters parameters)
	{
		ArrayList<GameObjectState> objects = new ArrayList<GameObjectState>();
		for(Location location : parameters.getArea().getRelativeLocations())
		{
			GameObjectState state = mapSystem.getObjectSateAt(new Location(target.x + location.x,target.y + location.y));
			if(state != null && isMatchingState(state, parameters))
			{
				objects.add(state);
			}
		}
		return objects;
	}
		
	/**
	 * Gets locations in range for ability
	 * TODO: Add different markers for can target, untargetable, etc
	 * @param parameters
	 * @return
	 */
	public ArrayList<Location> getLocationsInAbilityRange(AbilityTargetingParameters parameters)
	{
		ArrayList<Location> locations = new ArrayList<Location>();
		
		for(Location targetLocation : mapSystem.getLocations())
		{
			if(isInRange(targetLocation, parameters)) 
			{
//				Gdx.app.log("Targeting", targetLocation.toString() + " in range");
				locations.add(targetLocation);
			}
			
		}
		
		return locations;
	}
	
	/**
	 * Checks if location is in range of ability
	 * @param targetLocation
	 * @param parameters of ability being cast
	 * @return
	 */
	public boolean isInRange(Location targetLocation, TargetingParameters parameters)
	{
//		boolean axisLock = parameters.isAxisLocked();

//		if(axisLock & !(objectLocation.x == targetLocation.x || objectLocation.y == targetLocation.y))
//		{
//			return false;
//		}
		if(parameters instanceof AbilityTargetingParameters && ((AbilityTargetingParameters) parameters).isMovement())
		{
			if(this.findMoveLocations((AbilityTargetingParameters) parameters).contains(targetLocation)) 
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if(parameters.isGlobal())
		{
			return true;
		}
		else
		{
			Location objectLocation = parameters.getSource().getLocation();
			int dist = objectLocation.distance(targetLocation);
			if (dist >= parameters.getMinRange() && dist <= parameters.getMaxRange()) return true;
		}

		
		return false;
	}
	
	
	/**
	 * Checks if a location matches the target location type
	 * @param targetLocation
	 * @param parameters
	 * @return
	 */
	public boolean isMatchingLocation(Location targetLocation, TargetingParameters parameters)
	{
		GameObjectState state = mapSystem.getObjectSateAt(targetLocation);
		if(parameters.getTargetLocation() == TargetLocation.ANY)
		{
			return true;
		}
		else if(parameters.getTargetLocation() == TargetLocation.OBJECT && state != null)
		{
			return true;
		}
		else if(parameters.getTargetLocation() == TargetLocation.EMPTY &&  state == null)
		{
			return true;
		}
		else
		{
			return false;
		}

	}
	
	/**
	 * Checks to see if object at location matches target parameters
	 * @param targetLocation
	 * @param parameters
	 * @return True if matches parameters, false is does not or no object at location
	 */
	public boolean isMatchingState(Location targetLocation, TargetingParameters parameters)
	{
		return isMatchingState(mapSystem.getObjectSateAt(targetLocation), parameters);
	}
	
	/**
	 * Checks to see if object state matches targeting parameters
	 * @param targetState
	 * @param parameters
	 * @return True if matches, false if it does not
	 */
	public boolean isMatchingState(GameObjectState targetState, TargetingParameters parameters)
	{
		//if null further tests cannot be done so checks for target location type immediately
		if(targetState == null) return false;
		
		if(parameters.getTargetAssociation() == TargetAssociation.ALLY
				&& !targetState.getTeam().equals(parameters.getSource().getTeam())) return false;
		if(parameters.getTargetAssociation() == TargetAssociation.ENEMY
				&& targetState.getTeam().equals(parameters.getSource().getTeam())) return false;		
		
		//checks all of the HashSets for IDS and buffs
		//if isEmpty  false and contain is false then it passes check
		//they both cannot be true(can't be both empty and contains something)
		if(parameters.getTargetObjects().isEmpty() == parameters.getTargetObjects().contains(targetState))
		{
			Gdx.app.log("Targeting", "Invalid target because not amount target objects");
			return false;
		}
		if(parameters.getTargetIDs().isEmpty() == parameters.getTargetIDs().contains(targetState.getID()))
		{
			Gdx.app.log("Targeting", "Invalid target because not amount target object IDs");

			return false;
		}
		
		//False if the class IDs are not empty and the intersection of the classIDs are empty
		if(!parameters.getTargetClassIDs().isEmpty() && Sets.intersection(parameters.getTargetClassIDs(), targetState.getClassIDs()).isEmpty())
		{
			Gdx.app.log("Targeting", "Invalid target because not amount target object classIDs");

			return false;
		}
		
		//Buffs are used as markers and needs to see if it has a buff that matches the target buff ID
		//Or has a class buff the intersects
		if(!parameters.getTargetBuffIDs().isEmpty() || !parameters.getTargetBuffClassIDs().isEmpty())
		{
			boolean marked = false;
			
			for(Buff buff : targetState.getBuffs())
			{
				//If the target buffIDs are not empty and the paramters contain the buff's ID then the target is marked
				if(!parameters.getTargetBuffIDs().isEmpty() && parameters.getTargetBuffIDs().contains(buff.getID()))
				{
					marked = true;
				}
				
				//if there are buffClassIDs and the intersection is not empty then it is marked
				if(!parameters.getTargetBuffClassIDs().isEmpty() && !Sets.intersection(parameters.getTargetBuffClassIDs(), buff.getClassIDs()).isEmpty())
				{
					marked = true;
				}
			}
		
			if(!marked)
			{
				Gdx.app.log("Targeting", "Invalid target because target not properly marked");

				return false;
			}
		}
			
		return true;
	}
	
	/**
	 * Finds move locations on the map
	 * @param map
	 * @return
	 */
	public ArrayList<MoveLocation> findMoveLocations(AbilityTargetingParameters parameters)
	{
		GameObjectState movingState = parameters.getSource();
		ArrayList<MoveLocation> closed = new ArrayList<MoveLocation>();
		PriorityQueue<MoveLocation> open = new PriorityQueue<MoveLocation>();
		MoveLocation start = new MoveLocation(movingState.getLocation(), 0);
		int range = movingState.getMoveRange();
		open.add(start);
		if(range < 1) 
		{
			Gdx.app.log("Move", "Move range is 0, no valid locations");
			closed.add(start);
			return closed;
		}
		
		ArrayList<MoveLocation> cost = new ArrayList<MoveLocation>();
		for(Location location : mapSystem.getLocations())
		{
			cost.add(new MoveLocation(location.x, location.y));
		}
		
		



//		Gdx.app.log("MoveTargeting", "Starting to find move locations for " + owner.getGameID() + " from " + open.peek().toString());
		while(!open.isEmpty())
		{

			MoveLocation location = open.poll();
//			Gdx.app.log("MoveTarget", "Checking for movements from " + location.toString());

			
			if(location.cost < range)
			{
				closed.add(location);
				
				MoveLocation north = new MoveLocation(location, Dir.N);
				MoveLocation east = new MoveLocation(location, Dir.E);
				MoveLocation south = new MoveLocation(location, Dir.S);
				MoveLocation west = new MoveLocation(location, Dir.W);
				
				if(cost.contains(north) && mapSystem.getGameObjectAt(north.toLocation()) == null)
				{
					int oldCost = cost.get(cost.indexOf(north)).cost;
					if(oldCost == -1 || north.cost < oldCost)
					{
						cost.remove(north);
						open.remove(north);
						open.offer(north);
					}
				}
				if(cost.contains(east) && mapSystem.getGameObjectAt(east.toLocation()) == null)
				{
					int oldCost = cost.get(cost.indexOf(east)).cost;
					if(oldCost == -1 || east.cost < oldCost)
					{
						cost.remove(east);
						open.remove(east);
						open.offer(east);
					}
				}
				if(cost.contains(south) && mapSystem.getGameObjectAt(south.toLocation()) == null)
				{
					int oldCost = cost.get(cost.indexOf(south)).cost;
					if(oldCost == -1 || south.cost < oldCost)
					{
						cost.remove(south);
						open.remove(south);
						open.offer(south);
					}
				}
				if(cost.contains(west) && mapSystem.getGameObjectAt(west.toLocation()) == null)
				{
					int oldCost = cost.get(cost.indexOf(west)).cost;
					if(oldCost == -1 || west.cost < oldCost)
					{
						cost.remove(west);
						open.remove(west);
						open.offer(west);
					}
				}
			}
		}

		
		closed.remove(start);
		return closed;
	}
	
	public MoveLocation findMovePath(AbilityTargetingParameters parameters, Location target)
	{
		ArrayList<MoveLocation> locations = this.findMoveLocations(parameters);
		MoveLocation end = locations.get(locations.indexOf(target));
		return end;
	}

	public void setMapSystem(MapSystem mapSystem) {
		this.mapSystem = mapSystem;		
	}

	
	public enum TargetState
	{
		INRANGE,
		TARGETABLE,
		EFFECTED
	}
}
