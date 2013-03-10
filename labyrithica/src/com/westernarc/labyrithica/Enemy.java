package com.westernarc.labyrithica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Enemy extends Entity {
	//Stats: Strength, Speed, Intelligence
	//Reduces chance to dodge
	//Increases physical damage, maximum HP, HP regen
	//Ability to hold weapons
	public int VIT;
	//Increases turn order, chance to dodge/block
	//Ability to equip armor
	public int SPD;
	//Gives higher cast success chance, ability regen,
	//ability effectiveness, maximum AP
	//Ability to learn abilities
	public int WIS;
	
	public Enemy(TYPE type, Labyrithica game) {
		super("enemy_" + type, game);
		switch(type) {
			default:
				sprite = new Sprite(new Texture(Gdx.files.internal("img/enemy.png")));
		}
		sprite = new Sprite(new Texture(Gdx.files.internal("img/enemy.png")));
		this.game = game;
	}
	
	public enum TYPE {Ant, Bee, Newt, Wolf, Direwolf, Bandit, Warrior, Centaur, Minotaur, Goliath, Devil}
	TYPE type;
	
	@Override
	public void update() {
		super.update();
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
		}
	}
}