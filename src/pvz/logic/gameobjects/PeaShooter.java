package pvz.logic.gameobjects;

import pvz.logic.Game;
import pvz.logic.Position;
import pvz.view.Messages;

public class PeaShooter {

	private int health;
	private int fr;
	private Game game;
	private Position pos;
	
	public PeaShooter(Position pos, Game game) {
		this.health = 3;
		this.game = game;
		this.pos = pos;
		this.fr = 1;
	}
	
	public PeaShooter(int int1, int int2, Game game) {
		// TODO Auto-generated constructor stub
		Position p = new Position(int1,int2);
		this.health = 3;
		this.game = game;
		this.fr = 1;	
	}

	public static String getDescription() {
		// TODO Auto-generated method stub
		return Messages.PEASHOOTER_ICON;
	}

	
	

	public boolean isInPosition(Position pos2) {
		// TODO Auto-generated method stub
		if (pos.getCol() == pos2.getCol() & pos.getRow() == pos2.getRow()) {
			return true;
		}
		return false;
	}
	
	public String getIcon() {
		return getDescription().formatted(this.health);
	}
	
	

}
