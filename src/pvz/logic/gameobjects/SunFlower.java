package pvz.logic.gameobjects;

import pvz.logic.Game;
import pvz.view.Messages;
import pvz.logic.Position;

public class SunFlower {


	private int health;
	private int fr;
	private Game game;
	private Position pos;
	private int sunPoints;
	private int cycles;
	
	public SunFlower(Position pos, Game game) {
		this.health = 3;
		this.game = game;
		this.pos = pos;
		this.fr = 1;
		sunPoints = 10;
		cycles = 2;
	}
	
	
	public static  String getDescription() {
		// TODO Auto-generated method stub
		return Messages.SUNFLOWER_ICON;
	}
	public String getIcon() {
		return getDescription().formatted(this.health);
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
}
