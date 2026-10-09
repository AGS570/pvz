package pvz.logic;

import java.util.Random;

import pvz.control.Level;
import pvz.logic.Position;
import pvz.logic.gameobjects.PeaShooter;
import pvz.logic.gameobjects.PeaShooterList;
import pvz.logic.gameobjects.SunFlower;
import pvz.logic.gameobjects.SunflowerList;
import pvz.logic.gameobjects.Zombie;
import pvz.logic.gameobjects.ZombieList;

public class Game {
	public static final int NUM_ROWS = 4;
	public static final int NUM_COLS = 8;
	
	
	int soles ;
	int ciclos ;
	Random rand;
	ZombieList Zombies;
	SunflowerList SunFlowers;
	PeaShooterList PeaShooters;

	
	//constructoras----------------------
	public Game() {
		soles = 50;
		ciclos = 0;
		this.PeaShooters = new PeaShooterList();
		this.SunFlowers = new SunflowerList();
		this.Zombies = new ZombieList();
	}
	public Game(long seed, Level level) {
		// TODO Auto-generated constructor stub
		soles = 50;
		ciclos = 0;
		this.PeaShooters = new PeaShooterList();
		this.SunFlowers = new SunflowerList();
		this.Zombies = new ZombieList();
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
		String ret;
		ret = PeaShooters.PositionToString(position);
		if(ret != null) return ret;
		ret = SunFlowers.PositionToString(position);
		if(ret != null) return ret;
		ret = SunFlowers.PositionToString(position);
		if(ret != null) return ret;
		
		return ret;
	}
	public Object getSuncoins() {
		// TODO Auto-generated method stub
		return this.soles;
	}
	public Object getCycles() {
		// TODO Auto-generated method stub
		return this.ciclos;
	}
	public void sun() {
		this.soles +=20;
	}
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	public boolean addPeaShooter(int int1, int int2) {
		// TODO Auto-generated method stub
		Position pos = new Position(int1,int2);
		return this.PeaShooters.insert(new PeaShooter(pos,this));
	}
	public boolean addSunFlower(int int1, int int2) {
		// TODO Auto-generated method stub
		Position pos = new Position(int1,int2);
		return this.SunFlowers.insert(new SunFlower(pos,this));
	}
	public boolean addZombie(int int1, int int2) {
		// TODO Auto-generated method stub
		Position pos = new Position(int1,int2);
		return this.Zombies.insert(new Zombie(pos,this));
		
	}
	
}
