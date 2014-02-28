package garnet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

public class AbilityButtonDrawable extends BaseDrawable{

	private Sprite outline;
	private Sprite icon;
	
	public AbilityButtonDrawable(TextureRegion icon)
	{
		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal("sprites/abilities/Abilities.atlas"));
		AtlasRegion outline = atlas.findRegion("outline");
		this.outline = new Sprite(outline);
		this.icon = new Sprite(icon);
		setMinWidth(this.icon.getWidth());
		setMinHeight(this.icon.getHeight());
	}
	
	public void draw (SpriteBatch batch, float x, float y, float width, float height) {
		icon.setBounds(x, y, width, height);
		Color color = icon.getColor();
		icon.setColor(Color.tmp.set(color).mul(batch.getColor()));
		icon.draw(batch);
		icon.setColor(color);
		
		outline.setBounds(x, y, width, height);
		color = outline.getColor();
		outline.setColor(Color.tmp.set(color).mul(batch.getColor()));
		outline.draw(batch);
		outline.setColor(color);
		
		
//		outline.draw(batch);
//		icon.draw(batch);
	}
	
	/**
	 * Returns the ability button with a tinted outline
	 * @param color Tint of outline
	 * @return new drawable
	 */
	public AbilityButtonDrawable newAbilityButtonDrawable(Color color)
	{
		AbilityButtonDrawable newDrawable = new AbilityButtonDrawable(icon);
		newDrawable.outline.setColor(color.r, color.g, color.b, .6f);
		return newDrawable;
	}

}
