package net.sleepystudios.bankvault;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;

import net.sleepystudios.bankvault.entities.Drone;
import net.sleepystudios.bankvault.entities.Player;
import net.sleepystudios.bankvault.proc.ProcObject;

public class BankVault extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	OrthographicCamera camera;
	MapHandler mh;
	Player p;
	ShapeRenderer sr;
	boolean showHitBoxes = true;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		// camera
        float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(w, h);
		mh = new MapHandler(new TmxMapLoader().load("map.tmx"));
		p = new Player(camera, mh);
		sr = new ShapeRenderer();
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		mh.render(camera);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		p.render(batch);
		
		for(ProcObject o : mh.procObjs) o.render(batch);
		
		for(Drone d : mh.drones) d.render(batch);
		
		if(showHitBoxes) renderBoxes();
		
		batch.end();
	}
	
	private void renderBoxes() {
		batch.end();
		
		sr.setProjectionMatrix(camera.combined);
		sr.begin(ShapeType.Line);
		sr.setColor(Color.RED);
		
		for(Rectangle r : mh.rects) sr.rect(r.x, r.y, r.width, r.height);
		for(ProcObject o : mh.procObjs) sr.rect(o.rect.x, o.rect.y, o.rect.width, o.rect.height);
		for(Drone d : mh.drones) sr.rect(d.box.x, d.box.y, d.box.width, d.box.height);
		sr.rect(p.box.x, p.box.y, 32, 32);
		
		sr.end();
		
		batch.begin();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode==Input.Keys.B) showHitBoxes = !showHitBoxes;
		if(keycode==Input.Keys.SPACE) mh.gen();
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	// generates a random number
    public static int rand(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
    public static float rand(float min, float max) {
        return min + new Random().nextFloat() * (max - min);
    }

    // random number that cannot be 0
    public static int randNoZero(int min, int max) {
        int r = rand(min, max);
        return r != 0 ? r : randNoZero(min, max);
    }
    public static float randNoZero(float min, float max) {
        float r = rand(min, max);
        return r != 0 ? r : randNoZero(min, max);
    }
}