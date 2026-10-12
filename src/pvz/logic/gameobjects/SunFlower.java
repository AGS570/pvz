package pvz.logic.gameobjects;

import pvz.logic.Game;
import pvz.view.Messages;
import pvz.logic.Position;

public class SunFlower {

	private static int COST = 50;
	private int health;
	private int fr;
	private Game game;
	private Position pos;
	private int sunPoints;
	private int cycles;
	
	public SunFlower(Position pos, Game game) {
		this.health = 1;
		this.game = game;
		this.pos = pos;
		this.fr = 1;
		sunPoints = 10;
		cycles = 2;
	}
	
	
	public static  String getDescription() {
		// TODO Auto-generated method stub
		return Messages.SUNFLOWER_DESCRIPTION;
	}
	public String getIcon() {
		return Messages.SUNFLOWER_ICON.formatted(this.health);
	}
	
	public int getSun() {
		// TODO Auto-generated method stub
		if(cycles == 0) {
			cycles = 2;
			return this.sunPoints;
		}
		else {
			cycles--;
			return 0;
		}
	}
	public boolean isInPosition(Position pos2) {
		// TODO Auto-generated method stub
		if (pos.getCol() == pos2.getCol() & pos.getRow() == pos2.getRow()) {
			return true;
		}
		return false;
	}
	public static int getCost() {
		return COST;
	}


	public void receiveInteraction(int damage) {
		// TODO Auto-generated method stub
		this.health -= damage;
	}


	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isAlive() {
		return this.health>0;
	}
}
