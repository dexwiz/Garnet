package garnet.ability.target;

import garnet.ability.target.area.Area;
import garnet.ability.target.area.SingleArea;

/**
 * Targeting for the AoE of abilities. By default defines a single area with parameters matching ability
 * @author Aaron
 *
 */
public class AreaTargetingParameters extends TargetingParameters{
	private Area area = new SingleArea();
		
	public AreaTargetingParameters()
	{
	}
	
	/**
	 * Copies the parameters of how the parent ability targets
	 * @param parameters
	 */
	public AreaTargetingParameters(AbilityTargetingParameters parameters)
	{
		this.setSource(parameters.getSource());
		this.setTargetingSystem(parameters.getTargetingSystem());
		this.setTargetAssociation(parameters.getTargetAssociation());
		this.setTargetLocation(parameters.getTargetLocation());
		this.setTargetBuffClassIDs(parameters.getTargetBuffClassIDs());
		this.setTargetBuffIDs(parameters.getTargetBuffIDs());
		this.setTargetClassIDs(parameters.getTargetClassIDs());
		this.setTargetIDs(parameters.getTargetIDs());
	}
	
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}	
}
