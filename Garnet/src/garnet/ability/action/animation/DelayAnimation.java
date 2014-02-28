package garnet.ability.action.animation;

import garnet.gameobject.Render;

public class DelayAnimation extends Animation{

	public DelayAnimation(Render render, float duration) {
		super(render, duration);

	}

	@Override
	public void act(float percent) {
		//does nothing, just waits
	}

}
