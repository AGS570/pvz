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
	

	public String getDescription() {
		// TODO Auto-generated method stub
		return Messages.ZOMBIE_ICON + "["+ this.health + "]";
	}
	
	public boolean isInPosition(Position pos2) {
		// TODO Auto-generated method stub
		if (pos.getCol() == pos2.getCol() & pos.getRow() == pos2.getRow()) {
			return true;
		}
		return false;
	}
	
}
