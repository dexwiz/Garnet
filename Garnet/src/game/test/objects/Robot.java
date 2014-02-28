package game.test.objects;

import garnet.gameobject.GameObjectDefinition;

public class Robot extends GameObjectDefinition{

	public Robot()
	{
		this.name = "robot";
		this.hp = 11;
		this.mana = 11;
		this.moveRange = 4;
	}
}
