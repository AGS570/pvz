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
	
	
	private int soles ;
	private int ciclos ;
	private Random rand;
	
	//ZombieList Zombies;
	SunflowerList SunFlowers;
	PeaShooterList PeaShooters;
	ZombiesManager zombieMngr;
	private Level level;
	private boolean isGameFinished;
	
	//constructoras----------------------
	public Game() {
		soles = 50;
		ciclos = 0;
		this.PeaShooters = new PeaShooterList();
		this.SunFlowers = new SunflowerList();
		//this.Zombies = new ZombieList();
		
		
	}
	public Game(long seed, Level level) {
		// TODO Auto-generated constructor stub
		soles = 50;
		ciclos = 0;
		this.PeaShooters = new PeaShooterList();
		this.SunFlowers = new SunflowerList();
		//this.Zombies = new ZombieList();
		this.level = level;
		this.rand = new Random(seed);
		this.zombieMngr = new ZombiesManager(this,level,rand);
		isGameFinished = false;
	}

	public void update() {
		this.ciclos++;
		
		
		for(int i =0; i < SunFlowers.size();i++) {
			this.soles += SunFlowers.getSun();
		}
		
		this.PeaShooters.update();
		this.SunFlowers.update();
		this.zombieMngr.update();
		
		this.zombieMngr.addZombie();
	}

	public boolean hasGameFinished() {
		// TODO Auto-generated method stub
		return this.isGameFinished;
	}
	public Object getRemainingZombies() {
		// TODO Auto-generated method stub
		return this.zombieMngr.getRemainingZombies();
	}
	public String positionToString(Position position) {
		// TODO Auto-generated method stub
		
			
		String ret;
		ret = PeaShooters.PositionToString(position);
		if(ret != null) return ret;
		ret = SunFlowers.PositionToString(position);
		if(ret != null) return ret;
		ret = zombieMngr.PositionToString(position);
		if(ret != null) return ret;
		return "";
		
	}
	public int getSuncoins() {
		// TODO Auto-generated method stub
		return this.soles;
	}
	public int getCycles() {
		// TODO Auto-generated method stub
		return this.ciclos;
	}
	
	
	public void reset() {
		
		//reseteamos valores
		this.soles=50;
		this.ciclos=0;
		
		//vaciar listas
		this.PeaShooters=new PeaShooterList();
		this.SunFlowers=new SunflowerList();
		
		this.zombieMngr=new ZombiesManager(this,this.level,this.rand);
		
		
	}
	
	public boolean addPeaShooter(int int1, int int2) {
		if(this.soles >= PeaShooter.getCost()) {	
		Position pos = new Position(int1,int2);
		this.soles -= PeaShooter.getCost();
		// TODO Auto-generated method stub
		if(pos.isValid(NUM_ROWS, NUM_COLS)) {
			return this.PeaShooters.insert(new PeaShooter(pos,this));}
		else return false;
		}else {
			return false;
		} 
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
	public boolean isPositionEmpty(Position pos) {
	    return PeaShooters.PositionToString(pos) == null
	        && SunFlowers.PositionToString(pos) == null
	        && zombieMngr.PositionToString(pos) == null;
	}
	
	public boolean canAffordPea() {
		if(this.soles >= PeaShooter.getCost()) return true;
		return false;
	}
	public boolean canAffordSun() {
		if(this.soles >= SunFlower.getCost()) return true;
		return false;
	}
	public boolean plantsAhead(Position pos) {
		// TODO Auto-generated method stub
		return PeaShooters.PositionToString(pos) != null
		       || SunFlowers.PositionToString(pos) != null;
	}
	
	public Position zombieInRange(Position pos) {
		Position nextPos;
		for (int i = pos.getCol()+1; i < NUM_COLS; i++) {
			nextPos = new Position(pos.getRow(),i);
			if(zombieMngr.PositionToString(new Position(nextPos.getRow(),nextPos.getCol())) != null) {
				return nextPos;
			}
		}
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void finishGame() {
		this.isGameFinished = true;
	}
	
	public void attack(Position nextPos, int damage) {
		// TODO Auto-generated method stub
		this.PeaShooters.receiveInteraction(nextPos,damage);
		this.SunFlowers.receiveInteraction(nextPos,damage);
	}
	public void shoot() {
		// TODO Auto-generated method stub
		
	}
}
