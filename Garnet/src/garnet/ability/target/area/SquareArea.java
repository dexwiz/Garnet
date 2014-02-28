package garnet.ability.target.area;



public class SquareArea extends Area {


	public SquareArea() {
		this(0 ,1);
	}

	public SquareArea(int maxAoE)
	{
		this(0, maxAoE);
	}
	
	public SquareArea(int minAoE, int maxAoE)
	{
		defineSquareArea(minAoE, maxAoE);
	}
	
	private void defineSquareArea(int minAoE, int maxAoE)
	{
		clearRelativeLocations();
		for(int i = -maxAoE; i <= maxAoE; i ++)
		{
			for(int j = -maxAoE; j <= maxAoE; j++)
			{
				int dist = Math.max(Math.abs(i), Math.abs(j));
				if(dist >= minAoE && dist <= maxAoE)
				{
					addRelativeLocation(i,j);
				}
			}
		}
	}
	
	public void setMinMaxRange(int min, int max)
	{
		defineSquareArea(min, max);
	}
}
