package pvz.logic.gameobjects;

import pvz.logic.Game;
import pvz.logic.Position;
import pvz.view.Messages;

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
	
	public Zombie(int int1, int int2) {
		// TODO Auto-generated constructor stub
	}

	public static Object getDescription() {
		// TODO Auto-generated method stub
		return Messages.ZOMBIE_ICON ;
	}
}
