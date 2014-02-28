package garnet.util;

import java.util.ArrayList;


public class MoveLocation implements Comparable<Object>{
	public int x;
	public int y;
	public int cost;
	public MoveLocation parent;
	
	public MoveLocation(MoveLocation parent, Dir direction, int cost)
	{
		this.parent = parent;

		if(parent != null)
		{
			this.cost = parent.cost + cost;
			switch(direction)
			{
			case E:
				this.x = parent.x;
				this.y = parent.y+1;
				break;
			case N:
				this.x = parent.x+1;
				this.y = parent.y;
				break;
			case S:
				this.x = parent.x-1;
				this.y = parent.y;
				break;
			case W:
				this.x = parent.x;
				this.y = parent.y-1;
				break;
			default:
				this.x = parent.x;
				this.y = parent.y;
				break;
			}	
		}
	}
	
	public MoveLocation(MoveLocation parent, Dir direction)
	{
		this(parent, direction, 1);
	}
	
	public MoveLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.cost = -1;
	}
	
	public MoveLocation(int x, int y, int cost)
	{
		this.x = x;
		this.y = y;
		this.cost = cost;
	}
	
	public MoveLocation(Location v)
	{
		this(v.x, v.y);
	}

	public Location toLocation()
	{
		return new Location(x,y);
	}

	public MoveLocation(Location start, int cost) {
		this.x = start.x;
		this.y = start.y;
		this.cost = cost;
	}
	
	/**
	 * Gets the move path to a location minus the location currently at
	 * @return
	 */
	public ArrayList<Location> getPath()
	{
		MoveLocation stepBefore = parent;
		ArrayList<Location> path = new ArrayList<Location>();
		path.add(new Location(this));
		
		do
		{
			path.add(0,new Location(stepBefore));
			stepBefore = stepBefore.parent;
		}
		while(stepBefore != null);
		path.remove(0);
		
		return path;
	}
	
	@Override
	public int compareTo(Object o) {
		if(o instanceof MoveLocation)
		{
			return this.cost - ((MoveLocation)o).cost;
		}
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MoveLocation)
		{
			if(((MoveLocation)obj).x == x && ((MoveLocation)obj).y == y)
			{
				return true;
			}
		}
		else if(obj instanceof Location)
		{
			if(((Location)obj).x == x && ((Location)obj).y == y)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public enum Dir
	{
		N, E, S, W
	}
	
	@Override
	public String toString()
	{
		return x + "," + y;
	}
	
}
