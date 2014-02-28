package garnet.board;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import game.basic.abilities.Attack;
import game.basic.abilities.Move;
import game.basic.buffs.Strength;
import game.test.objects.Robot;
import garnet.ability.Ability;
import garnet.ability.Buff;
import garnet.ability.action.ActionFactory;
import garnet.ability.target.AreaTargetingParameters;
import garnet.gameobject.*;
import garnet.util.Location;

public class Factory {
	private ActionSystem actionSystem = new ActionSystem();
	private ActionFactory actionFactory = new ActionFactory();
	private RenderSystem renderSystem = new RenderSystem();
	private TargetingSystem targetingSystem = new TargetingSystem();
	private AnimationSystem animationSystem = new AnimationSystem();
	private ManagerSystem managerSystem = new ManagerSystem();
	private MapSystem gameBoard = new MapSystem();

	
	public Factory()
	{
		//gives targeting access to the map
		targetingSystem.setMapSystem(gameBoard);
		
		//gives ability access to targeting for checking action listeners
		actionFactory.setTargetingSystem(targetingSystem);
		
		
		//Gives action system access to animation and game to assign actions
		actionSystem.setAnimationSystem(animationSystem);
		
		//gives action system access to manager for callbacks once actions are compelted
		actionSystem.setManagerSystem(managerSystem);
		
		//give action system access to map to move/add/remove objects
		actionSystem.setMapSystem(gameBoard);
		
		//gives turn system access to map to trigger start/end turn effects
		managerSystem.setMapSystem(gameBoard);
		
		//gives turn system access to ability system to create start/turn effects
		managerSystem.setAbilitySystem(actionFactory);
		managerSystem.setActionSystem(actionSystem);
		managerSystem.setTargetingSystem(targetingSystem);
		
		renderSystem.setMapSystem(gameBoard);
	}
	public void loadMap()
	{
		TiledMap tiledMap = new TmxMapLoader().load("data/Maps/basictest.tmx");
		renderSystem.setTiledMap(tiledMap);
		MapProperties mapProperties = tiledMap.getProperties();		
		//gets basic map properties
		int mapWidth = mapProperties.get("width", Integer.class);
		int mapHeight = mapProperties.get("height", Integer.class);
		

		
		//creates a base map that is the mapWidth and mapHeight
		ArrayList<Location> locations = new ArrayList<Location>();
		for(int i = 0; i < mapWidth; i++)
		{
			for(int j = 0; j < mapHeight; j++)
			{
					locations.add(new Location(i, j));
			}
		}
		//removes any locations which has a collision object sitting at it
		Iterator<MapObject> collisionObjects = (Iterator<MapObject>)tiledMap.getLayers().get("Collision").getObjects().iterator();
		while(collisionObjects.hasNext())
		{
			MapObject collisionObject = collisionObjects.next();
			
			MapProperties collisionProperties = collisionObject.getProperties();
			String collide = collisionProperties.get("collide", "yes", String.class);
			int x = collisionProperties.get("x" , -32, Integer.class)/32;
			int y = collisionProperties.get("y" , -32, Integer.class)/32;
			
			if(x >= 0 && x < mapWidth && y >= 0 && y < mapHeight && collide.equals("yes") )
			{
				locations.remove(new Location(x,y));
			}
		}
		
		//adds locations to map
		gameBoard.addGameBoardLocations(locations);
		
		ArrayList<Team> teams = new ArrayList<Team>();
		
		Iterator<MapObject> mapObjects = (Iterator<MapObject>)tiledMap.getLayers().get("Spawn").getObjects().iterator();
		while(mapObjects.hasNext())
		{
			MapObject mapObject = mapObjects.next();
			MapProperties objectProperties = mapObject.getProperties();
			
//			while(keys.hasNext())
//			{
//				String key = keys.next();
//				Gdx.app.log("map", "Key: " + key + " Value: " + objectProperties.get(key).toString() + " Class " + objectProperties.get(key).getClass());
//			}
			
			int x = objectProperties.get("x" , -32, Integer.class)/32;
			int y = objectProperties.get("y" , -32, Integer.class)/32;
			Gdx.app.log("Factory", "Loading gameObject at  " +  x + "-" + y);

			
			GameObject gameObject = createGameObject(new Robot());
			gameObject.getState().setName("Obj " + x + " " + y);
			gameObject.getState().setLocation(new Location(x,y));
			gameObject.getRender().setScreenX(x*32);
			gameObject.getRender().setScreenY(y*32);
			gameBoard.placeGameObject(gameObject, new Location(x,y));			

			
			int teamNumber = Integer.valueOf(objectProperties.get("team", "-1", String.class));
			while(teamNumber > teams.size())
			{
				Team team = new Team(teamNumber);
				team.setTurnSystem(managerSystem);
				team.setController(createControllerSystem());
				teams.add(team);
				managerSystem.addTeam(team);
			}
			gameObject.getState().setTeam(teams.get(teamNumber-1));
		}	
	}
	
	public ControllerSystem createControllerSystem()
	{
		ControllerSystem controllerSystem = new ControllerSystem();
		
		//gives the controller access to the gameboard to make selections
		controllerSystem.setMapSystem(gameBoard);
		
		//gives controller access to renderer to convert screen -> board clicks and pass cursor info
		controllerSystem.setRenderSystem(renderSystem);
		
		//gives controller access to targeting system to check for validity of targets and pass highlights to render
		controllerSystem.setTargetingSystem(targetingSystem);
		
		//gives controller access to Ability system to create action flows
		controllerSystem.setAbilitySystem(actionFactory);
		

		
		//gives controller access to manager system to end turns and cast abilites
		controllerSystem.setManagerSystem(managerSystem);
		
		
		return controllerSystem;
	}
	
	public GameObject createGameObject(GameObjectDefinition gameObjectDef)
	{
		GameObject gameObject = new GameObject();
		GameObjectState state = new GameObjectState();
		Render render = new Render();
		Abilities abilities = new Abilities();

		
		state.setHP(gameObjectDef.getHp());
		state.setMana(gameObjectDef.getMana());
		

		//Gives Components to entity
		gameObject.setState(state);
		gameObject.setRender(render);
		gameObject.setAbilities(abilities);

		//Gives entity to systems
		actionSystem.addState(state);
		animationSystem.addRender(render);
		
		if(gameObjectDef.getAttack() > 0)
		{
			this.createAbility(new Attack(gameObjectDef.getAttack()), gameObject);
		}
		
		if(gameObjectDef.getMoveRange() > 0)
		{
			this.createAbility(new Move(gameObjectDef.getMoveRange()), gameObject);
		}
		
		for(int i =0; i < 10; i++)
		{
			this.createAbility(new Move(gameObjectDef.getAttack()), gameObject);

		}
		
		for(int i = 0; i < 16; i++)
		{
			Strength strenth = new Strength();
			state.applyBuff(strenth);
		}
		

		
		return gameObject;
	}
	
	public Ability createAbility(Ability ability, GameObject gameObject)
	{
		Gdx.app.log("Factory", "Creating " + ability.getName() + " for " + gameObject.getClass().toString());
		gameObject.getAbilities().addToPool(ability);
//		gameObject.getAbilities().draw();
		ability.setTargetingSystem(this.targetingSystem);
		ability.setActionFactory(this.actionFactory);
		ability.setSource(gameObject.getState());
		ability.getTargetingParameters().setSource(gameObject.getState());
		for(AreaTargetingParameters atParams : ability.getAreaTargetingParameters())
		{
			atParams.setSource(gameObject.getState());
		}
		return ability;
	}
	
	public Buff createBuff(Buff buff, GameObject sourcegameObject, GameObject targetgameObject)
	{
		buff.setActionFactory(this.actionFactory);
		buff.setSource(sourcegameObject.getState());
		buff.setHolder(targetgameObject.getState());
		targetgameObject.getState().applyBuff(buff);
		
		return buff;
	}

	public ActionSystem getActionSystem() {
		return actionSystem;
	}

	public RenderSystem getRenderSystem() {
		return renderSystem;
	}

	public TargetingSystem getTargetingSystem() {
		return targetingSystem;
	}

	public MapSystem getGameBoard() {
		return gameBoard;
	}

	public AnimationSystem getAnimationSystem() {
		return this.animationSystem;
	}
	public ManagerSystem getTurnSystem() {
		return this.managerSystem;
	}
	
	
}
