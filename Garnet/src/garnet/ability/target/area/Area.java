package garnet.ability.target.area;

import garnet.util.Location;

import java.util.ArrayList;
public class Area {
	private ArrayList<Location> relativeLocations = new ArrayList<Location>();
	private AreaAnchor areaAnchor = AreaAnchor.TARGET;
		
	public void addRelativeLocation(int x, int y)
	{
		
		Location loc = new Location(x,y);

		if(!relativeLocations.contains(loc))
		{
			relativeLocations.add(loc);
		}

	}
	
	public void clearRelativeLocations()
	{
		relativeLocations.clear();
	}
	
	public ArrayList<Location> getRelativeLocations() {
		return relativeLocations;
	}
	
	public AreaAnchor getAreaAnchor() {
		return areaAnchor;
	}

	public void setAreaAnchor(AreaAnchor areaAnchor) {
		this.areaAnchor = areaAnchor;
	}
	

	/** Where the zones target */
	public enum AreaAnchor
	{
		/** Target based on current selected tile */
		TARGET,
		/** Target based on caster of effect */
		SOURCE,
		/** Target based on caster of effect and pointed towards selected tile. 
		 * For best results set axisLock to true on parent {@link Effect} */
		RELATIVE,
	}
	
	/** Shape of predefined zones or custom */
	public enum AreaShape
	{
		/** Custom zone shape. Set if using dynamic zone */
		CUSTOM,
		/** AoE in spread pattern based on Manhattan Distance*/
		AOE_SPREAD,
		/** AoE spread based on Chebyshev Disnace */
		AOE_SQUARE,
		/** AoE Cone, for best results use {@link ZoneAnchor.RELATIVE} */
		AOE_CONE,
		/** AoE Line that extends set distance, for best results use {@link ZoneAnchor.RELATIVE} */
		AOE_LINE,
		/** Singe square */
		SINGLE
	}
}
