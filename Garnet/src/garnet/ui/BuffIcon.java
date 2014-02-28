package garnet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Pool.Poolable;

public class BuffIcon extends Image implements Poolable{
	String toolTip = "Default";
	
	public BuffIcon()
	{
		Texture texture = new Texture(Gdx.files.internal("data/icons/ghost.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 0, 32, 32);
		this.setDrawable(new TextureRegionDrawable(region));
	}
	
	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	public void reset() {
		toolTip = "";
		this.setVisible(false);
		this.remove();
	}
}
