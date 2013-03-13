package com.westernarc.labyrithica;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Entity {
	Texture[] frames;
	
	//Which foot to use during stepping
	private boolean stepSide;
	final boolean STEP_LEFT = false;
	final boolean STEP_RIGHT = true;
	
	public Player(Labyrithica game) {
		super("player", game);
		frames = new Texture[4];
		frames[0] = (new Texture(Gdx.files.internal("img/run1.png")));
		frames[1] = (new Texture(Gdx.files.internal("img/run2.png")));
		frames[2] = (new Texture(Gdx.files.internal("img/run3.png")));
		frames[3] = (new Texture(Gdx.files.internal("img/run4.png")));
		sprite = new Sprite(frames[0]);
		
		VIT = 10;
		SPD = 10;
		WIS = 10;
		
		stepSide = STEP_RIGHT;
	}

	public Sprite sprite;
	
	@Override
	public void update() {
		super.update();
	}
	public void updateAnim(float tpf) {
		if(moving) {
			if(stepSide)
				sprite.setTexture(frames[1]);
			else
				sprite.setTexture(frames[3]);
		} else {
			if(!sprite.getTexture().equals(frames[0])) {
				sprite.setTexture(frames[0]);
				stepSide = !stepSide;
			}
		}
	}
}
