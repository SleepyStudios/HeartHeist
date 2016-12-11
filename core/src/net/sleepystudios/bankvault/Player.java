package net.sleepystudios.bankvault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import net.sleepystudios.bankvault.proc.ProcObject;

public class Player {
	float x, y;
	OrthographicCamera camera;
	MapHandler mh;
	Sprite sprite;
	Rectangle box;
	
	public Player(OrthographicCamera camera, MapHandler mh) {
		this.camera = camera;
		this.mh = mh;
		
		sprite = new Sprite(new Texture("player.png"));
		move(x, y);
	}
	
	public void render(SpriteBatch batch) {
		sprite.draw(batch);
		
		// movement
		float speed = 120f * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if(!isBlocked(x, y+speed)) move(x, y + speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
        	 if(!isBlocked(x-speed, y)) move(x - speed, y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        	 if(!isBlocked(x, y-speed)) move(x, y - speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        	 if(!isBlocked(x+speed, y)) move(x + speed, y);
        }
	}
	
	private boolean isBlocked(float x, float y) {
		updateHitBox(x, y);
		if(x<0 || x>mh.getWidth()-mh.getTileSize() || y<0 || y>mh.getHeight()-mh.getTileSize()) return true;
		
		for(Rectangle r : mh.rects) {
			if(Intersector.overlaps(box, r)) return true;
		}
		
		for(ProcObject o : mh.procObjs) {
			if(o.rect!=null && Intersector.overlaps(box, o.rect)) return true;
		}
		
		return false;
	}
	
	private void move(float x, float y) {
		updateHitBox(x, y);
		
		this.x = x; 
		this.y = y;
		sprite.setPosition(x, y);
		updateCam();
	}
	
	int OX = 0, OY = 0, FW = 32, FH = 32;
	public void updateHitBox(float x, float y) {
		box = new Rectangle(x+OX, y+OY, FW-(OX*2), FH-(OY*2));
	}
	
	private void updateCam() {
    	// get the map properties to find the height/width, etc
    	int mapPixelWidth = mh.getWidth();
    	int mapPixelHeight = mh.getHeight();
    	
    	float minCameraX = camera.zoom * (camera.viewportWidth / 2);
        float maxCameraX = (mapPixelWidth) - minCameraX;
        float minCameraY = camera.zoom * (camera.viewportHeight / 2);
        float maxCameraY = (mapPixelHeight) - minCameraY;
        
        camera.position.set(Math.min(maxCameraX, Math.max(x, minCameraX)), Math.min(maxCameraY, Math.max(y, minCameraY)), 0);
    }
}
