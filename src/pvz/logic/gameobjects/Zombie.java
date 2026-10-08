package logic.gameobjects;

import logic.Game;
import utils.Position;
import view.Messages;

public class Zombie {
	private int health;
	private int fr;
	private Game game;
	private Position pos;
	
	public Zombie(Position pos, Game game) {
		this.health = 3;
		this.game = game;
		this.pos = pos;
		this.fr = 1;
	}
	
	public static Object getDescription() {
		// TODO Auto-generated method stub
		return Messages.ZOMBIE_ICON ;
	}
}
