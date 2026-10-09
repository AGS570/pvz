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
	ZombiesManager zombieMngr;
	Level level;
	
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
		this.level = level;
		this.rand = new Random();
		this.zombieMngr = new ZombiesManager(this,level,rand);
	}

	public void update() {
		this.ciclos++;
		
		for(int i =0; i < SunFlowers.size();i++) {
			SunFlowers.getSun();
		}
		this.zombieMngr.addZombie();
	}

	public boolean hasGameFinished() {
		// TODO Auto-generated method stub
		return false;
	}
	public Object getRemainingZombies() {
		// TODO Auto-generated method stub
		return this.Zombies.size();
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
		return "";
		
	}
	public Object getSuncoins() {
		// TODO Auto-generated method stub
		return this.soles;
	}
	public Object getCycles() {
		// TODO Auto-generated method stub
		return this.ciclos;
	}
	
	public void sun(int sunPoints) {
		this.soles += sunPoints;
	}
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	public boolean addPeaShooter(int int1, int int2) {
		Position pos = new Position(int1,int2);
		// TODO Auto-generated method stub
		if(pos.isValid(NUM_ROWS, NUM_COLS)) {
			return this.PeaShooters.insert(new PeaShooter(pos,this));}
		else return false;
	}
	public boolean addSunFlower(int int1, int int2) {
		// TODO Auto-generated method stub
		Position pos = new Position(int1,int2);
		if(pos.isValid(NUM_ROWS, NUM_COLS)) {
			return this.SunFlowers.insert(new SunFlower(pos,this));}
		else return false;
	}
	public boolean addZombie() {
		// TODO Auto-generated method stub
		return this.zombieMngr.addZombie();
		
	}
	
}
