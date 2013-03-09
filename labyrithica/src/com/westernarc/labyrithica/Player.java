package com.westernarc.labyrithica;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Entity {
	public Player() {
		super("player");
		sprite = new Sprite(new Texture(Gdx.files.internal("img/player.png")));
		ready = true;
	}

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
	
	//x and y coordinates of sprite
	public float x;
	public float y;
	//x and y destination of sprite
	public float targetX;
	public float targetY;
	
	public Sprite sprite;
	
	public void moveTo(float tX, float tY) {
		if(ready) {
			targetX = Math.round(tX + x);
			targetY = Math.round(tY + y);
			ready = false;
		}
	}
	public void move(float x, float y) {
		if(this.x > x) this.x -= 0.1f; else if(this.x < x) this.x += 0.1f;
		if(this.y > y) this.y -= 0.1f; else if(this.y < y) this.y += 0.1f;
		if(Math.abs(this.x - targetX) <= 0.1f) {
			this.x = targetX;
		}
		if(Math.abs(this.y - targetY) <= 0.1f) {
			this.y = targetY;
		}
		if(this.y == targetY && this.x == targetX) ready = true;
	}
	@Override
	public void update() {
		System.out.println("[" + x + ", " + y + "] " + ready);
		move(targetX, targetY);
	}
	public int getFloorX() {
		return (int)Math.floor(x);
	}
	public int getFloorY() {
		return (int)Math.floor(y);
	}
	public int getCeilX() {
		return (int)Math.ceil(x);
	}
	public int getCeilY() {
		return (int)Math.ceil(y);
	}
}
