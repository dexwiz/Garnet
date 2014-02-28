package garnet.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import garnet.board.TargetingSystem.TargetState;
import garnet.gameobject.GameObject;
import garnet.gameobject.Render;
import garnet.util.Location;

public class RenderSystem {
	private ArrayList<Render> renders = new ArrayList<Render>();
	
	private int tileWidth, tileHeight;
	private int mapWidth, mapHeight;
	
	private OrthographicCamera camera  = new OrthographicCamera();
	private OrthogonalTiledMapRenderer  renderer;
	private SpriteBatch batch;

	private Location cursorLocation = new Location(0,0);
	private HashMap<Location, TargetState> highlights = new HashMap<Location, TargetState>();
	private TextureRegion targetRegion;
	private TextureRegion cusorRegion;

	private MapSystem mapSystem;
		
	public RenderSystem()
	{
		Texture targetTexture = new Texture(Gdx.files.internal("data/Highlights/selection.png"));
		targetTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		cusorRegion = new TextureRegion(targetTexture, 0, 0, 32, 32);
		
		Texture selectionTexture = new Texture(Gdx.files.internal("data/Highlights/selection.png"));
		selectionTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		targetRegion = new TextureRegion(selectionTexture, 0, 0, 32, 32);
	}
	
	public void render(float delta)
	{
		
//		camera.translate(0.01f, 0);
		camera.update();
		renderer.setView(camera);
		renderer.render();


		batch.setProjectionMatrix(camera.combined);
//		batch.setTransformMatrix(camera.view);
		batch.begin();
		batch.draw(cusorRegion, cursorLocation.x, cursorLocation.y);

		for(Map.Entry<Location, TargetState> highlight : highlights.entrySet())
		{
			switch (highlight.getValue())
			{
			case EFFECTED:

				batch.setColor(1, 0, 0, .3f);
				batch.draw(targetRegion, highlight.getKey().x*32, highlight.getKey().y*32);
				break;
			case INRANGE:
				batch.setColor(1,1,1, .3f);
				batch.draw(targetRegion, highlight.getKey().x*32, highlight.getKey().y*32);
				break;
			case TARGETABLE:
				batch.setColor(0, 0, 1, .3f);
				batch.draw(targetRegion, highlight.getKey().x*32, highlight.getKey().y*32);
				break;
			default:
				break;
			
			}
		}
		batch.setColor(Color.WHITE);
		
//		for(Render render : renders)
//		{
//			render.render(batch, delta);
//		}
		for(GameObject obj : mapSystem.getGameObjects())
		{
			obj.getRender().render(batch, delta);
		}
		
		batch.end();
	}
	
	/**
	 * Moves where the cursor is being rendered 
	 * @param location
	 */
	public void setCursorLocation(Location location)
	{
		cursorLocation.set(location.x*tileWidth, location.y*tileHeight);
	}
	
	public void addRender(Render render) {
		renders.add(render);
		
	}
	
	public Location screenToMap(int screenX, int screenY)
	{
		Vector3 touchPos = new Vector3(screenX, screenY, 0);
	    camera.unproject(touchPos);
		
		int x = (int) ((touchPos.x)/tileWidth);
		int y = (int) ((touchPos.y)/tileHeight);
		return new Location(x,y);
	}
	
	public void setTiledMap(TiledMap map)
	{
		batch = new SpriteBatch();
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		Vector2 screenCoord = new Vector2(screenWidth, screenHeight);
		MapProperties mapProperties = map.getProperties();
		
		//gets basic map properties
		mapWidth = mapProperties.get("width", Integer.class);
		mapHeight = mapProperties.get("height", Integer.class);
		tileWidth = mapProperties.get("tilewidth", Integer.class);
		tileHeight = mapProperties.get("tileheight", Integer.class);
		Vector2 mapSize = new Vector2(tileWidth * mapWidth, tileHeight * mapHeight);
		
		
		renderer = new OrthogonalTiledMapRenderer(map);
		renderer.setView(camera);
		camera.setToOrtho(false, screenWidth, screenHeight);

		camera.translate(mapSize.x * -.5f, (screenHeight - mapSize.x)*-.5f);
//		camera.translate(mapCoord);
		camera.update();
		Gdx.app.log("Camera", "Camrea now at " + camera.position);
	}

	public void setHighlights(HashMap<Location, TargetState> targetStates) {
		this.highlights = targetStates;
	}

	public void resetHighlights()
	{
		this.highlights.clear();
	}

	public void setMapSystem(MapSystem gameBoard) {
		this.mapSystem = gameBoard;		
	}

	
}
