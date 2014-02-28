package garnet.gameobject;

public class GameObject {
	private GameObjectState state;
	private Render render;
	private Abilities abilities;

	
	public GameObjectState getState() {
		return state;
	}
	public void setState(GameObjectState state) {
		this.state = state;
		state.setEntity(this);
	}
	public Render getRender() {
		return render;
	}
	public void setRender(Render render) {
		this.render = render;
	}
	public Abilities getAbilities() {
		return abilities;
	}
	public void setAbilities(Abilities abilities) {
		this.abilities = abilities;
	}

	public void addState(GameObjectState state2) {
		// TODO Auto-generated method stub
		
	}
	
}
