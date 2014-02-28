package garnet.gameobject;

import garnet.ability.Ability;

import java.util.ArrayList;

public class Abilities {
	private ArrayList<Ability> abilityPool = new ArrayList<Ability>();
	private ArrayList<Ability> abilityBar = new ArrayList<Ability>();
	private Ability attack;
	private Ability move;
	
	
	private int maxBar = 5;
	private int currentBar = 0;
	
	
	public Abilities()
	{
		
	}
	
	public void draw()
	{
		if(currentBar < maxBar && abilityPool.size() > 0)
		{
			abilityBar.add(abilityPool.remove(0));
		}
	}
	
	public void discard(Ability ability)
	{
		abilityBar.remove(ability);
	}
	
	public ArrayList<Ability> getCurrentBar()
	{
		ArrayList<Ability> current = new ArrayList<Ability>();
		if(attack != null)
		{
			current.add(attack);
		}
		if(move != null)
		{
			current.add(move);
		}
		current.addAll(abilityBar);
		
		return current;
	}

	public void addToPool(Ability ability) {
		abilityPool.add(ability);		
	}
	
	public void setMove(Ability move) {
		this.move = move;
	}
	
	public void setAttack(Ability attack)
	{
		this.attack = attack;
	}

	/**
	 * Gets the number of abilites on bar
	 * @return
	 */
	public int getBarSize() {
		return abilityBar.size();
	}
	
	/**
	 * Gets ability from the bar
	 * @param i index of ability
	 * @return Ability if it exists
	 */
	public Ability getAbility(int i)
	{
		return abilityBar.get(i);
	}
}
