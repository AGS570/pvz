package logic.gameobjects;

import logic.Game;
import view.Messages;
import logic.Position;

public class SunFlower {


	private int health;
	private int fr;
	private Game game;
	private Position pos;
	
	public SunFlower(Position pos, Game game) {
		this.health = 3;
		this.game = game;
		this.pos = pos;
		this.fr = 1;
	}
	public static Object getDescription() {
		// TODO Auto-generated method stub
		return Messages.SUNFLOWER_ICON ;
	}

	public void update() {
		this.game.sun();
	}
}
