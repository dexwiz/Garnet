package garnet.board;

import garnet.ability.action.animation.Animation;
import garnet.gameobject.Render;

import java.util.ArrayList;

public class AnimationSystem {
	private ArrayList<Render> renders = new ArrayList<Render>();

	public ArrayList<Animation> animations = new ArrayList<Animation>();
	public ArrayList<Animation> animationQueue = new ArrayList<Animation>();
	public ArrayList<Animation> animationFinished = new ArrayList<Animation>();
	public void addAnimation(Animation action)
	{
		this.animationQueue.add(action);
	}
	
	public void addRender(Render render)
	{
		this.renders.add(render);
	}
	
	public void update(float delta)
	{
		animations.addAll(this.animationQueue);
		animationQueue.clear();
		for(Animation animation : animations)
		{
			if(animation.update(delta) >= 1.0)
			{
				animationFinished.add(animation);
				
				//cleans up a bit
//				animation.getRender().setScreenX(animation.getParentAction().getTarget().getLocation().x*32f);
//				animation.getRender().setScreenY(animation.getParentAction().getTarget().getLocation().y*32f);
			}
			
			
		}
		animations.removeAll(animationFinished);
		animationFinished.clear();
	}
}
