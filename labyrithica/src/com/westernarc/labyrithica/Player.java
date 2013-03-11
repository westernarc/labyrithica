package com.westernarc.labyrithica;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Entity {
	Texture[] frames;
	private int currentFrame;
	private float frameTime;
	
	public Player(Labyrithica game) {
		super("player", game);
		frames = new Texture[4];
		frames[0] = (new Texture(Gdx.files.internal("img/run1.png")));
		frames[1] = (new Texture(Gdx.files.internal("img/run2.png")));
		frames[2] = (new Texture(Gdx.files.internal("img/run3.png")));
		frames[3] = (new Texture(Gdx.files.internal("img/run4.png")));
		currentFrame = 0;
		frameTime = 0;
		sprite = new Sprite(frames[currentFrame]);
		
		VIT = 10;
		SPD = 10;
		WIS = 10;
	}

	public Sprite sprite;
	
	@Override
	public void update() {
		super.update();
	}
	public void updateAnim(float tpf) {
		frameTime += tpf;
		if(frameTime > 0.3f) {
			frameTime = 0;
			currentFrame++;
			if(currentFrame > 3) currentFrame = 0;
		}
		sprite.setTexture(frames[currentFrame]);
	}
}
