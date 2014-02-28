package garnet.ability.action.animation;

import com.badlogic.gdx.Gdx;

import garnet.ability.action.Action;
import garnet.gameobject.Render;

public abstract class Animation {
	private Action parentAction;
	protected Render render;
	private float duration = 0;
	private float time = 0;
	private float percent = 0;
	
	public Animation(Render render, float duration)
	{
		this.render = render;
		this.duration = duration;
	}
	
	public void animationComplete(){
		Gdx.app.log("Animation" ,"Animation belonging to " + parentAction.toString() + ":" + parentAction.getType().toString() + " is complete");
		parentAction.animationComplete();
	}

	/**
	 * Updates the animation by a time difference
	 * @param delta
	 * @return Percent that the animation is done from 0 to 1
	 */
	public float update(float delta)
	{
		this.time += delta;
		this.percent = time/duration;
		this.act(percent);
		if(percent >= 1.0) 
		{
			if(this.getRender() != null)
			{
				this.getRender().setScreenX(this.getParentAction().getSource().getLocation().x*32f);
				this.getRender().setScreenY(this.getParentAction().getSource().getLocation().y*32f);
			}
			animationComplete();
		}
		return this.percent;
	}
	
	public abstract void act(float percent);
	
	public void setParentAction(Action action) {
		this.parentAction = action;		
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}
	

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
	}

	public Action getParentAction() {
		return parentAction;
	}
	
	public Render getRender()
	{
		return this.render;
	}
	
}
