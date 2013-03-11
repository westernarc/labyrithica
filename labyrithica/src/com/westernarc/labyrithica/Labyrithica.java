package com.westernarc.labyrithica;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.westernarc.labyrithica.Tile.TYPE;

public class Labyrithica implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private Sprite[] sprArrFloorTiles;
	
	private Sprite[] sprMenuIcons;
	
	//Dimensions of screen
	int SCREEN_WIDTH;
	int SCREEN_HEIGHT;
	
	//Width and height in number of tiles of each labyrinth floor
	final int CONST_LABYRINTH_WIDTH = 32;
	final int CONST_LABYRINTH_HEIGHT = 32;
	
	//Tiles sprite dimensions
	final int CONST_TILE_WIDTH = 64;
	final int CONST_TILE_HEIGHT = 64;
	
	//Current labyrinth floor
	int varCurrentLabyrinthFloor;
	
	//Labyrinth floor tiles.  2D Array that will store
	//which type of tile goes where on the map.
	Tile[][] varFloorTiles;
	Tile WALLTILE = new Tile(Tile.TYPE.Wall, this);
	
	ArrayList<Enemy> enemies;
	//Player object
	Player player;
	
	//Boolean that holds whos turn it is; the player's or the enemy's
	boolean varPlayerPhase;
	
	//Boolean that holds whether or not the menu is active
	boolean varMenuActive;
	
	//Boolean that holds whether or not menu  mode is active
	//When the center of the screen is pressed, 
	@Override
	public void create() {		
		SCREEN_WIDTH = Gdx.graphics.getWidth();
		SCREEN_HEIGHT = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		batch = new SpriteBatch();
		
		sprArrFloorTiles = new Sprite[16];
		sprArrFloorTiles[0] = new Sprite(new Texture(Gdx.files.internal("img/wall.png")));
		sprArrFloorTiles[1] = new Sprite(new Texture(Gdx.files.internal("img/floor.png")));
		sprArrFloorTiles[2] = new Sprite(new Texture(Gdx.files.internal("img/stairs.png")));
		
		//Menu layout is like this:
		//1 2 3
		//4   6
		//7 8 9
		sprMenuIcons = new Sprite[10];
		sprMenuIcons[2] = new Sprite(new Texture(Gdx.files.internal("img/menuN.png")));
		sprMenuIcons[4] = new Sprite(new Texture(Gdx.files.internal("img/menuW.png")));
		sprMenuIcons[6] = new Sprite(new Texture(Gdx.files.internal("img/menuE.png")));
		sprMenuIcons[8] = new Sprite(new Texture(Gdx.files.internal("img/menuS.png")));
		
		varFloorTiles = new Tile[CONST_LABYRINTH_WIDTH][CONST_LABYRINTH_HEIGHT];
		
		
		player = new Player(this);
		
		enemies = new ArrayList<Enemy>();
		
		createNewFloor();
		
		varPlayerPhase = true;
		varMenuActive = false;
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		handleInput();
		
		update();
		
		drawFloor();
		
		drawEnemy();
		
		drawPlayer();
		
		if(varMenuActive) {
			drawMenu();
		}
		batch.end();
	}

	//Draw floor tiles. Requires batch to be started
	private void drawFloor() {
		for(int x = 0; x < CONST_LABYRINTH_WIDTH; x++) {
			for(int y = 0; y < CONST_LABYRINTH_HEIGHT; y++) {
				//Draw each tile according to the type of floortype they are
				switch(varFloorTiles[x][y].type){
				case Floor:
					sprArrFloorTiles[1].setPosition(varFloorTiles[x][y].x * CONST_TILE_WIDTH, varFloorTiles[x][y].y * CONST_TILE_HEIGHT);
					if(varFloorTiles[x][y].discovered)
						sprArrFloorTiles[1].draw(batch, varFloorTiles[x][y].inSight ? 1 : 0.4f);	
					break;
				case Wall:
					sprArrFloorTiles[0].setPosition(varFloorTiles[x][y].x * CONST_TILE_WIDTH, varFloorTiles[x][y].y * CONST_TILE_HEIGHT);
					if(varFloorTiles[x][y].discovered)
						sprArrFloorTiles[0].draw(batch, varFloorTiles[x][y].inSight ? 1 : 0.4f);
					break;
				case Stairs:
					sprArrFloorTiles[2].setPosition(varFloorTiles[x][y].x * CONST_TILE_WIDTH, varFloorTiles[x][y].y * CONST_TILE_HEIGHT);
					if(varFloorTiles[x][y].discovered)
						sprArrFloorTiles[2].draw(batch, varFloorTiles[x][y].inSight ? 1 : 0.4f);
				default:
					break;
				}
			}
		}
	}
	
	private void drawPlayer() {
		//Player contains location in map coordinates.
		//Need to translate to screen coordinates.
		player.sprite.setPosition(player.x * CONST_TILE_WIDTH, player.y * CONST_TILE_HEIGHT);
		player.sprite.draw(batch);
	}
	
	private void drawEnemy() {
		Iterator<Enemy> enemyItr = enemies.iterator();
		while(enemyItr.hasNext()) {
			Enemy currentEnemy = enemyItr.next();
			currentEnemy.sprite.setPosition(currentEnemy.x * CONST_TILE_WIDTH, currentEnemy.y * CONST_TILE_HEIGHT);
			currentEnemy.sprite.draw(batch);
		}
	}
	
	private void drawMenu() {
		sprMenuIcons[2].setPosition(camera.position.x - sprMenuIcons[2].getWidth()/2, camera.position.y + SCREEN_HEIGHT/3 - sprMenuIcons[2].getWidth()/2);
		sprMenuIcons[2].draw(batch);
		sprMenuIcons[4].setPosition(camera.position.x - SCREEN_WIDTH / 3 - sprMenuIcons[2].getWidth()/2, camera.position.y - sprMenuIcons[2].getWidth()/2);
		sprMenuIcons[4].draw(batch);
		sprMenuIcons[6].setPosition(camera.position.x + SCREEN_WIDTH / 3 - sprMenuIcons[2].getWidth()/2, camera.position.y - sprMenuIcons[2].getWidth()/2);
		sprMenuIcons[6].draw(batch);
		sprMenuIcons[8].setPosition(camera.position.x - sprMenuIcons[2].getWidth()/2, camera.position.y - SCREEN_HEIGHT/3 - sprMenuIcons[2].getWidth()/2);
		sprMenuIcons[8].draw(batch);
	}
	//Reinitialize the floor array with a new floor
	private void createNewFloor() {
		for(int x = 0; x < CONST_LABYRINTH_WIDTH; x++) {
			for(int y = 0; y < CONST_LABYRINTH_HEIGHT; y++) {
				if(Math.random() > 0.8) {
					varFloorTiles[x][y] = new Tile(Tile.TYPE.Wall, this);
				} else {
					varFloorTiles[x][y] = new Tile(Tile.TYPE.Floor, this);
				}
				varFloorTiles[x][y].x = x;
				varFloorTiles[x][y].y = y;
			}
		}
		
		//Place stairs randomly
		int randomX = (int)(Math.random() * CONST_LABYRINTH_HEIGHT);
		int randomY = (int)(Math.random() * CONST_LABYRINTH_WIDTH);
		varFloorTiles[randomX][randomY].type = Tile.TYPE.Stairs;
		System.out.println("Stairs: " + randomX + " , " + randomY);
		
		enemies.clear();
		
		//Place monsters
		Enemy newEnemy = new Enemy(Enemy.TYPE.Rat, this);
		newEnemy.x = 5;
		newEnemy.y = 5;
		newEnemy.targetX = 5;
		newEnemy.targetY = 5;
		Enemy newEnemy2 = new Enemy(Enemy.TYPE.Rat, this);
		newEnemy2.x = 7;
		newEnemy2.y = 7;
		newEnemy2.targetX = 7;
		newEnemy2.targetY = 7;
		enemies.add(newEnemy);
		enemies.add(newEnemy2);
		
		//Update floor tiles
		for(int x = 0; x < CONST_LABYRINTH_WIDTH; x++) {
			for(int y = 0; y < CONST_LABYRINTH_HEIGHT; y++) {
				varFloorTiles[x][y].update();
			}
		}
	}
	
	private void handleInput() {
		if(Gdx.input.isKeyPressed(Keys.A)) {
			varPlayerPhase = false;
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			varPlayerPhase = true;
			player.ready = true;
			
		}
		if(Gdx.input.isTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			//Split the screen into 9 squares.
			//NW, N, NE, W, C, E, SW, S, SE
			//Split up like this:
			//1 2 3
			//4 5 6
			//7 8 9
			int pos = 0;
			//Top row
			if(y < SCREEN_HEIGHT / 3f) {
				if(x < SCREEN_WIDTH / 3f) pos = 1;
				else if(x > SCREEN_WIDTH * 2f / 3f) pos = 3;
				else pos = 2;
			}
			//Bottom row
			else if(y > SCREEN_HEIGHT * 2f / 3f){
				if(x < SCREEN_WIDTH / 3f) pos = 7;
				else if(x > SCREEN_WIDTH * 2f / 3f) pos = 9;
				else pos = 8;
			}
			//Middle row
			else {
				if(x < SCREEN_WIDTH / 3f) pos = 4;
				else if(x > SCREEN_WIDTH * 2f / 3f) pos = 6;
				else pos = 5;
			}

			if(varMenuActive) {
				switch(pos) {
				case 1: //NW
					break;
				case 2: //N
					
					break;
				case 3: //NE
					break;
				case 4: //W
					
					break;
				case 5: //C
					//Toggle menu view
					if(Gdx.input.justTouched()) {
						varMenuActive = !varMenuActive;
					}
					break;
				case 6: //E
					break;
				case 7: //SW
					break;
				case 8: //S
					if(varFloorTiles[player.getFloorX()][player.getFloorY()].type == TYPE.Stairs) {
						//Descend down stairs
						createNewFloor();
						varMenuActive = !varMenuActive;
						varCurrentLabyrinthFloor++;
						System.out.println("Descending to floor " + varCurrentLabyrinthFloor);
					} else {
						System.out.println("There are no stairs here...");
					}
					break;
				case 9: //SE
					break;
				default:
				}
			} else {
				switch(pos) {
				case 1: //NW
					break;
				case 2: //N
					if(player.getCeilY() < CONST_LABYRINTH_HEIGHT - 1 && varFloorTiles[player.getFloorX()][player.getCeilY() + 1].type != Tile.TYPE.Wall)
						player.moveTo(0, 0.7f);
					break;
				case 3: //NE
					break;
				case 4: //W
					if(player.getFloorX() > 0 && varFloorTiles[player.getFloorX() - 1][player.getFloorY()].type != Tile.TYPE.Wall)
						player.moveTo(-0.7f, 0);
					break;
				case 5: //C
					//Toggle menu view
					if(Gdx.input.justTouched()) {
						varMenuActive = !varMenuActive;
					}
					break;
				case 6: //E
					if(player.getCeilX() < CONST_LABYRINTH_WIDTH - 1 && varFloorTiles[player.getCeilX() + 1][player.getFloorY()].type != Tile.TYPE.Wall)
						player.moveTo(0.7f, 0);
					break;
				case 7: //SW
					break;
				case 8: //S
					if(player.getFloorY() > 0 && varFloorTiles[player.getFloorX()][player.getFloorY() - 1].type != Tile.TYPE.Wall)
						player.moveTo(0, -0.7f);
					break;
				case 9: //SE
					break;
				default:
				}
			}
			//Check for enemy collisions
			Iterator<Enemy> enemyItr = enemies.iterator();
			while(enemyItr.hasNext()) {
				Enemy currentEnemy = enemyItr.next();
				if(currentEnemy.x == Math.floor(player.targetX) && currentEnemy.y == Math.floor(player.targetY) && !currentEnemy.dead) {
					player.ready = true;
					player.moveTo(0, 0);
					System.out.println("You attacked the " + currentEnemy.type + " for 5 damage.");
					currentEnemy.VIT -= 5;
					if(currentEnemy.VIT <= 0) {
						System.out.println("The " + currentEnemy.type + " was killed!");
					}
				}
			}
		}
	}
	
	private void update() {
		float tpf = Gdx.graphics.getDeltaTime();
		player.updateAnim(tpf);
		if(varPlayerPhase) {
			//Update the player
			player.update();
			if(!player.moving && !player.ready) {
				varPlayerPhase = false;
				Iterator<Enemy> enemyItr = enemies.iterator();
				while(enemyItr.hasNext()) {
					Enemy currentEnemy = enemyItr.next();
					currentEnemy.ready = true;
				}
				//Update floor tiles
				for(int x = 0; x < CONST_LABYRINTH_WIDTH; x++) {
					for(int y = 0; y < CONST_LABYRINTH_HEIGHT; y++) {
						varFloorTiles[x][y].update();
					}
				}
			}
		}
		if(!varPlayerPhase){
			boolean allEnemiesDone = true;
			//Update enemies
			Iterator<Enemy> enemyItr = enemies.iterator();
			while(enemyItr.hasNext()) {
				Enemy currentEnemy = enemyItr.next();
				currentEnemy.sprite.setPosition(currentEnemy.x * CONST_TILE_WIDTH, currentEnemy.y * CONST_TILE_HEIGHT);
				currentEnemy.update();
				if(currentEnemy.ready || currentEnemy.moving) allEnemiesDone = false;
			}
			if(allEnemiesDone) {
				varPlayerPhase = true;
				player.ready = true;
			}
		}

		//Move the camera to be in position
		camera.position.set((int)player.sprite.getX(), (int)player.sprite.getY(), 0);
		camera.update(true);
		
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
	
	public Tile getTile(int x, int y) {
		try {
			return varFloorTiles[x][y];
		}catch(Exception e) {
			return WALLTILE;
		}
	}
	
}
