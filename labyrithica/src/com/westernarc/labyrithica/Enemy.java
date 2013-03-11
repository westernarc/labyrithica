package com.westernarc.labyrithica;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Enemy extends Entity {
	
	public Enemy(TYPE type, Labyrithica game) {
		super("enemy_" + type, game);
		this.type = type;
		switch(type) {
			default:
				sprite = new Sprite(new Texture(Gdx.files.internal("img/enemy.png")));
		}
		sprite = new Sprite(new Texture(Gdx.files.internal("img/enemy.png")));
		this.game = game;
		
		VIT = 10;
		SPD = 10;
		WIS = 10;
	}
	
	public enum TYPE {Rat, Bee, Newt, Wolf, Direwolf, Bandit, Warrior, Centaur, Minotaur, Goliath, Devil}
	TYPE type;
	
	@Override
	public void update() {
		super.update();
		if(VIT <= 0) {
			ready = false;
			if(!dead) {
				dead = true;
				sprite = new Sprite(new Texture(Gdx.files.internal("img/blood.png")));
			}
		}
		if(ready) {
			//TODO Random movement now
			double behaviorDeterminant = Math.random();
			if(behaviorDeterminant < 0.25 && game.getTile(getCeilX() + 1,(int)y).type != Tile.TYPE.Wall) {
				targetX = x + 1;
				ready = false;
				moving = true;
			} else if(behaviorDeterminant < 0.5 && game.getTile(getFloorX() - 1,(int)y).type != Tile.TYPE.Wall) {
				targetX = x - 1;
				ready = false;
				moving = true;
			} else if(behaviorDeterminant < 0.75 && game.getTile((int)x,getCeilY() + 1).type != Tile.TYPE.Wall) {
				targetY = y + 1;
				ready = false;
				moving = true;
			} else if(game.getTile((int)x,getFloorY() - 1).type != Tile.TYPE.Wall) {
				targetY = y - 1;
				ready = false;
				moving = true;
			}
			//If it moves into the player it attacks
			if(targetX == Math.floor(game.player.x) && targetY == Math.floor(game.player.y)) {
				ready = true;
				moveTo(0,0);
				System.out.println("Enemy attack");
			}
			//If it moves into an enemy don't move
			Iterator<Enemy> enemyItr = game.enemies.iterator();
			while(enemyItr.hasNext()) {
				Enemy currentEnemy = enemyItr.next();
				if(targetX == Math.floor(currentEnemy.x) && targetY == Math.floor(currentEnemy.x)) {
					ready = true;
					moveTo(0,0);
				}
			}
		}
	}
}
