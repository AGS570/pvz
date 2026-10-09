package pvz.logic.gameobjects;

import pvz.logic.Game;
import pvz.view.Messages;
import pvz.logic.Position;

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
	public SunFlower(int int1, int int2) {
		// TODO Auto-generated constructor stub
		
	}
	public static Object getDescription() {
		// TODO Auto-generated method stub
		return Messages.SUNFLOWER_ICON ;
	}

	public void update() {
		this.game.sun();
	}
}
