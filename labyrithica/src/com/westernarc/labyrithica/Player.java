package com.westernarc.labyrithica;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Entity {
	public Player() {
		super("player");
		sprite = new Sprite(new Texture(Gdx.files.internal("img/player.png")));
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
	public int x;
	public int y;
	//x and y destination of sprite
	public int targetX;
	public int targetY;
	
	public Sprite sprite;
	
	public boolean move(int x, int y) {
		targetX = x;
		targetY = y;
		if(this.x > x) this.x--; else if(this.x < x) this.x++;
		if(this.y > y) this.y--; else if(this.y < y) this.y++;
		if(this.x == x && this.y ==y) return true; else return false;
	}
	public void update() {
		System.out.println("Player update");
		move(targetX, targetY);
		sprite.setPosition(x,y);
	}
}
