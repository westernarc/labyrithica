package com.westernarc.labyrithica;

public class Tile extends Entity {
	public Tile(TYPE type, Labyrithica game) {
		super("tile"+type, game);
		this.type = type;
	}

	public enum TYPE {Wall, Floor, Stairs}
	public TYPE type;
}
