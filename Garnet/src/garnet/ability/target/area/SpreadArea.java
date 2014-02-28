package garnet.ability.target.area;



public class SpreadArea extends Area {

	

	public SpreadArea(int maxAoE)
	{
		this(0, maxAoE);
	}
	
	public SpreadArea(int minAoE, int maxAoE)
	{

		defineSpreadArea(minAoE, maxAoE);

	}
	
	private void defineSpreadArea(int minAoE, int maxAoE)
	{
		clearRelativeLocations();
		for(int i = -maxAoE; i <= maxAoE; i ++)
		{
			for(int j = -maxAoE; j <= maxAoE; j++)
			{
				int dist = Math.abs(i) +  Math.abs(j);
				if(dist >= minAoE && dist <= maxAoE)
				{
					addRelativeLocation(i,j);
				}
			}
		}
	}
	
	public void setMinMaxRange(int min, int max)
	{
		defineSpreadArea(min, max);
	}
}
