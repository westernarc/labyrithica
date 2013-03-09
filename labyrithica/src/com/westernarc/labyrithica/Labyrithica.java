package com.westernarc.labyrithica;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Labyrithica implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private Sprite[] sprWall;
	
	//Width and height in number of tiles of each labyrinth floor
	final int CONST_LABYRINTH_WIDTH = 32;
	final int CONST_LABYRINTH_HEIGHT = 32;
	
	//Tiles sprite dimensions
	final int CONST_TILE_WIDTH = 32;
	final int CONST_TILE_HEIGHT = 32;
	
	//Current labyrinth floor
	int varCurrentLabyrinthFloor;
	
	//Labyrinth floor tiles.  2D Array that will store
	//which type of tile goes where on the map.
	enum FLOORTILE {Wall, Floor}
	FLOORTILE[][] varFloorTiles;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
		batch = new SpriteBatch();
		
		sprWall = new Sprite[CONST_LABYRINTH_WIDTH * CONST_LABYRINTH_HEIGHT];
		for(int i = 0; i < sprWall.length; i++) {
			sprWall[i] = new Sprite(new Texture(Gdx.files.internal("img/wall.png")));
		}
		
		varFloorTiles = new FLOORTILE[CONST_LABYRINTH_WIDTH][CONST_LABYRINTH_HEIGHT];
		createNewFloor();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		drawFloor();
		
		batch.end();
	}

	//Draw floor tiles. Requires batch to be started
	private void drawFloor() {
		for(int i = 0; i < CONST_LABYRINTH_WIDTH; i++) {
			for(int ii = 0; ii < CONST_LABYRINTH_HEIGHT; ii++) {
				//Draw each tile according to the type of floortype they are
				switch(varFloorTiles[i][ii]){
				case Floor:
						
					break;
				case Wall:
					sprWall[0].setPosition(i * CONST_TILE_WIDTH, ii * CONST_TILE_HEIGHT);
					sprWall[0].draw(batch);
					break;
				default:
					break;
				
				}
			}
		}
	}
	
	//Reinitialize the floor array with a new floor
	private void createNewFloor() {
		for(int i = 0; i < CONST_LABYRINTH_WIDTH; i++) {
			for(int ii = 0; ii < CONST_LABYRINTH_HEIGHT; ii++) {
				if(Math.random() > 0.5) {
					varFloorTiles[i][ii] = FLOORTILE.Wall;
				} else {
					varFloorTiles[i][ii] = FLOORTILE.Floor;
				}
			}
		}
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
