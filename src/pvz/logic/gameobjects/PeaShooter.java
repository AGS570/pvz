package pvz.logic.gameobjects;

import pvz.logic.Game;
import pvz.logic.Position;
import pvz.view.Messages;

public class PeaShooter {

	private static int COST = 50;
	private int health;
	private int fr;
	private Game game;
	private Position pos;
	private int cost;
	private int shtFr;
	
	public PeaShooter(Position pos, Game game) {
		this.health = 3;
		this.game = game;
		this.pos = pos;
		this.fr = 1;
		cost = 50;
		this.shtFr = 1;
	}
	
	public PeaShooter(int int1, int int2, Game game) {
		// TODO Auto-generated constructor stub
		Position p = new Position(int1,int2);
		this.health = 3;
		this.game = game;
		this.fr = 1;	
		cost = 50;
		this.shtFr = 1;
	}

	public void update() {
		
	}
	public static String getDescription() {
		// TODO Auto-generated method stub
		return Messages.PEASHOOTER_DESCRIPTION;
	}

	public boolean isInPosition(Position pos2) {
		// TODO Auto-generated method stub
		if (pos.getCol() == pos2.getCol() & pos.getRow() == pos2.getRow()) {
			return true;
		}
		return false;
	}
	
	public String getIcon() {
		return Messages.PEASHOOTER_ICON.formatted(this.health);
	}
	
	public static int getCost() {
		return COST;
	}

	public void receiveInteraction(int damage) {
		// TODO Auto-generated method stub
		this.health -= damage;
	}
	
	public boolean isAlive() {
		return this.health>0;
	}

}
