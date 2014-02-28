package garnet;

import garnet.ability.action.Action;
import garnet.board.Factory;
import garnet.ui.UIStage;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class GarnetGame implements ApplicationListener {

	private Factory factory;
	private UIStage ui;
	@Override
	public void create() {		
		factory = new Factory();
		factory.loadMap();
		ui = new UIStage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		factory.getTurnSystem().setUIStage(ui);
		factory.getTurnSystem().startGame();
		Gdx.input.setInputProcessor(ui);
		

	}

	@Override
	public void dispose() {

	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		factory.getAnimationSystem().update(Gdx.graphics.getDeltaTime());
		factory.getRenderSystem().render(Gdx.graphics.getDeltaTime());
		ui.act();
		ui.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
