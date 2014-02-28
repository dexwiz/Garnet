package garnet.gameobject;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Render {
	private ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();
	private float screenX, screenY;
	public Render()
	{
		Texture texture = new Texture(Gdx.files.internal("data/icons/ghost.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 0, 32, 32);
		frames.add(region);
	}
	
	public void render(SpriteBatch batch, float delta)
	{
		batch.draw(frames.get(0), screenX, screenY);
	}

	public float getScreenX() {
		return screenX;
	}

	public void setScreenX(float screenX) {
		this.screenX = screenX;
	}

	public float getScreenY() {
		return screenY;
	}

	public void setScreenY(float screenY) {
		this.screenY = screenY;
	}

	public void move(float deltaX, float deltaY) {
		this.screenX += deltaX;
		this.screenY += deltaY;		
	}
	
	
}
