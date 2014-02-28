package garnet.util;


public class Location {
	public int x;
	public int y;
	
	public Location(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public Location(Location location) {
		this.x = location.x;
		this.y = location.y;
	}

	public Location(MoveLocation move) {
		this.x = move.x;
		this.y = move.y;
	}

	public String toString()
	{
		return x + "," + y;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Location)
		{
			if(((Location) obj).x == x && ((Location) obj).y == y)
			{
				return true;
			}
		}
		if(obj instanceof MoveLocation)
		{
			if(((MoveLocation) obj).x == x && ((MoveLocation) obj).y == y)
			{
				return true;
			}
		}
		return false;
	}

	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(Location v)
	{
		this.x = v.x;
		this.y = v.y;
	}

	public int distance(Location targetLocation) {
		return Math.abs(x - targetLocation.x) + Math.abs(y - targetLocation.y);

	}

	public Location add(Location location) {
		return new Location(x + location.x, y + location.y);
	}

	public Location subtract(Location location) {
		return new Location(x - location.x, y - location.y);
	}
	
}
