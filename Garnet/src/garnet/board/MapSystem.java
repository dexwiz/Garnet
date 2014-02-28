package garnet.board;


import garnet.gameobject.GameObject;
import garnet.gameobject.GameObjectState;
import garnet.gameobject.Team;
import garnet.util.Location;

import java.util.ArrayList;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table.Cell;

public class MapSystem {
	private ArrayList<Location> locations = new ArrayList<Location>();
	private HashBasedTable<Integer, Integer, GameObject> gameObjectLocations = HashBasedTable.create();
	
	/**
	 * Adds map locations to game board
	 * @param locations
	 */
	public void addGameBoardLocations(ArrayList<Location> locations) {
		this.locations.addAll(locations);		

	}
	
	/**
	 * Tests to see if location is a valid game board location
	 * @param location
	 * @return True if valid location
	 */
	public boolean locationExists(Location location)
	{
//		Gdx.app.log("Board", "Checking if " + location.toString() + " is on board " + locations.contains(location));
		return locations.contains(location);
	}
	
	/**
	 * Tries to place game object at location
	 * @param gameObject
	 * @param location
	 * @return False if location doesnt exist or location is occupied
	 */
	public boolean placeGameObject(GameObject gameObject, Location location)
	{
		if(locations.contains(location) && !gameObjectLocations.contains(location.x, location.y))
		{
			gameObjectLocations.put(location.x, location.y, gameObject);
			gameObject.getState().setLocation(location);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Tries to move gameobject to a location
	 * @param gameObject
	 * @param location
	 * @return False if location DNE or location is occupied
	 */
	public boolean moveGameObject(GameObjectState gameState, Location location)
	{
		if(locations.contains(location) && !gameObjectLocations.contains(location.x, location.y))
		{
			GameObject gameObject = null;
			for(Cell<Integer, Integer, GameObject> scanned : this.gameObjectLocations.cellSet())
			{
				if(scanned.getValue().getState().equals(gameState))
				{
					gameObject = scanned.getValue();
				}
			}
			
			Location oldLoc = gameObject.getState().getLocation();
			gameObjectLocations.put(location.x, location.y, gameObject);
			gameObject.getState().setLocation(location);
			gameObjectLocations.remove(oldLoc.x, oldLoc.y);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Gets a game object at a particular location
	 * @param location
	 * @return First object at location, else null
	 */
	public GameObject getGameObjectAt(Location location)
	{
		return gameObjectLocations.get(location.x, location.y);
	}
	
	/**
	 * Gets a game object state at a particular location
	 * @param location
	 * @return First game object state at location, else null
	 */
	public GameObjectState getObjectSateAt(Location location)
	{
		GameObject obj = gameObjectLocations.get(location.x, location.y);
		if(obj != null ) return obj.getState();
		return null;
	}
	
	public ArrayList<GameObjectState> getObjectStatesOnTeam(Team team)
	{
		ArrayList<GameObjectState> states = new ArrayList<GameObjectState>();
		for(GameObject obj : this.gameObjectLocations.values())
		{
			if(team.equals(obj.getState().getTeam())) states.add(obj.getState());
		}
		return states;
	}
	
	public ArrayList<GameObject> getGameObjectsOnteam(Team team)
	{
		ArrayList<GameObject> states = new ArrayList<GameObject>();
		for(GameObject obj : this.gameObjectLocations.values())
		{
			if(team.equals(obj.getState().getTeam())) states.add(obj);
		}
		return states;
	}
	
	public ArrayList<GameObjectState> getGameObjectStates()
	{
		ArrayList<GameObjectState> states = new ArrayList<GameObjectState>();
		for(GameObject obj : this.gameObjectLocations.values())
		{
			states.add(obj.getState());
		}
		return states;
	}
	
	public ArrayList<GameObject> getGameObjects()
	{
		ArrayList<GameObject> states = new ArrayList<GameObject>();
		for(GameObject obj : this.gameObjectLocations.values())
		{
			states.add(obj);
		}
		return states;
	}
	
	public ArrayList<Location> getLocations() {
		return this.locations;
	}

	public void removeGameObject(GameObjectState target) {
		gameObjectLocations.values().remove(target.getGameObject());	
	}
}
