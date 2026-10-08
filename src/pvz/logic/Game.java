package logic;

import java.util.Random;

import control.Level;
import utils.Position;

public class Game {
	public static final int NUM_ROWS = 4;
	public static final int NUM_COLS = 8;
	
	
	int soles ;
	int ciclos ;
	Random rand;
	
	//constructoras----------------------
	public Game() {
		soles = 50;
		ciclos = 0;

	}
	public Game(long seed, Level level) {
		// TODO Auto-generated constructor stub
	}

	public void update() {
		
	}

	public boolean hasGameFinished() {
		// TODO Auto-generated method stub
		return false;
	}
	public Object getRemainingZombies() {
		// TODO Auto-generated method stub
		return null;
	}
	public String positionToString(Position position) {
		// TODO Auto-generated method stub
		return null;
	}
	public Object getSuncoins() {
		// TODO Auto-generated method stub
		return this.soles;
	}
	public Object getCycles() {
		// TODO Auto-generated method stub
		return this.ciclos;
	}
	
}
