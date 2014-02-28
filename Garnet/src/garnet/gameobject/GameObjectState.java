package garnet.gameobject;

import java.util.ArrayList;
import java.util.HashSet;

import garnet.ability.Buff;
import garnet.ability.modifier.StateModifier;
import garnet.ability.modifier.StateModifier.StateModifierType;
import garnet.util.Location;


public class GameObjectState {
	private String name = "Default";
	
	private String ID = "DefaultID";
	private HashSet<String> classIDs = new HashSet<String>();
	
	private int HP = 1;
	private int mana = 0;

	private int baseMoveRange = 4;
	
	private Team team = null;
	
	private Location location = new Location(-1,-1);
	
	private ArrayList<Buff> buffs = new ArrayList<Buff>();

	private GameObject entity;
	
	public GameObjectState()
	{
		
	}

	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getMoveRange() {
		int moveRange = baseMoveRange;
		for(Buff buff : this.buffs)
		{
			for(StateModifier mod : buff.getStateMods())
			{
				if(mod.getType() == StateModifierType.MOVEMENT)
				{
					moveRange += mod.getValue();
				}
			}
		}
		
		return moveRange;
	}

	public void setBaseMoveRange(int moveRange) {
		this.baseMoveRange = moveRange;
	}

	public Location getLocation() {
		return location;
	}

	/**
	 * DO NOT CALL DIRECTLY, ONLY USED BY MAP SYSTEM
	 * @param location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	
	public void applyBuff(Buff buff)
	{
		//TODO: if stacking, checks for previous buffs
		
		//TODO: if unique, silently reapply old
		
		//TODO: if normal then add buff
		buffs.add(buff);
		buff.setOwner(this);
	}
	
	public ArrayList<Buff> getBuffs()
	{
		return buffs;
	}
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	
	public HashSet<String> getClassIDs() {
		return classIDs;
	}

	public void setClassIDs(HashSet<String> classIDs) {
		this.classIDs = classIDs;
	}

	public Render getRender()
	{
		return this.entity.getRender();
	}

	public Abilities getAbilities()
	{
		return this.entity.getAbilities();
	}
	
	public void setEntity(GameObject gameObject) {
		this.entity = gameObject;		
	}

	public GameObject getGameObject() {
		return entity;
	}
	
	
	
}
