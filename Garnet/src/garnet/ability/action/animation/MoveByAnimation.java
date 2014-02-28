package garnet.ability.action.animation;

import com.badlogic.gdx.Gdx;

import garnet.gameobject.Render;

public class MoveByAnimation extends Animation{
	public float x, y;
	float lastPercent;
	public MoveByAnimation(Render render, float duration, float x, float y)
	{
		super(render, duration);
		this.x = x;
		this.y = y;
		lastPercent = 0;
		Gdx.app.log("Animation Action", "New action moving renderer " + x + "," + y);
	}

	@Override
	public void act(float percent) {
		float percentDelta = percent - lastPercent;
		lastPercent = percent;
		this.render.move(x * percentDelta, y * percentDelta);
	}
}
