package pvz.logic;






import java.util.Random;

import pvz.control.Level;
import pvz.logic.gameobjects.Zombie;
import pvz.logic.gameobjects.ZombieList;

/**
 * Manages the full lifecycle of zombies for a game session.
 *
 * <p>Responsibilities: deciding each cycle whether to spawn a new zombie
 * (probabilistically, subject to the remaining quota from {@link Level}),
 * delegating per-cycle updates and dead-removal to the underlying
 * {@link ZombieList}, and answering win/loss queries
 *
 */
public class ZombiesManager {

	private Game game;

	private Level level;

	private Random rand;

	private int remainingZombies;

	private ZombieList zombies;

	public ZombiesManager(Game game, Level level, Random rand) {
		this.game = game;
		this.level = level;
		this.rand = rand;
		this.remainingZombies = level.getNumberOfZombies();
		this.zombies = new ZombieList();
	}

	/**
	 * Checks if the game should add (if possible) a zombie to the game.
	 * 
	 * @return <code>true</code> if a zombie should be added to the game.
	 */
	private boolean shouldAddZombie() {
		return rand.nextDouble() < level.getZombieFrequency();
	}
	
	/**
	 * Return a random row within the board limits.
	 * 
	 * @return a random row.
	 */
	private int randomZombieRow() {
		return rand.nextInt(Game.NUM_ROWS);
	}
	
	public boolean addZombie() {
		if(this.remainingZombies > 0) {
			
			int row = randomZombieRow();
			
			return addZombie(row);
		}else return false;
	}

	public boolean addZombie(int row) {
		boolean canAdd = getRemainingZombies() > 0 && shouldAddZombie()
				&& isPositionEmpty(Game.NUM_COLS, row);

		if(canAdd) {
			// TODO fill your code
			this.remainingZombies--;
			Position p = new Position( row,Game.NUM_COLS-1);
			this.zombies.insert(new Zombie(p,this));
		}
		return canAdd;
	}

	public void update(){
		this.zombies.update();
	}
	
	
	


	private boolean isPositionEmpty(int numCols, int row) {
		// TODO Auto-generated method stub
		return this.game.isPositionEmpty(new Position(row,numCols));
	}

	// por hacer------------------------------------
	public int getRemainingZombies() {
		// TODO Auto-generated method stub
		return this.remainingZombies;
	}

	public String PositionToString(Position pos) {
		return this.zombies.PositionToString(pos);
	}

	public void receiveInteraction(Position Pos, int damage) {
		// TODO Auto-generated method stub
		this.zombies.receiveInteraction(Pos,damage);
	}
	
	// TODO fill your code
	public boolean zombiesRemain() {
		return (this.remainingZombies == 0 && this.zombies.isEmpty());
	}

	public boolean plantsAhead(Position nextPos) {
		// TODO Auto-generated method stub
		return this.game.plantsAhead(nextPos);
	}

	public void attack(Position nextPos, int damage) {
		// TODO Auto-generated method stub
		this.game.attack(nextPos, damage);
	}

	public boolean isPositionEmpty(Position nextPos) {
		// TODO Auto-generated method stub
		return this.game.isPositionEmpty(nextPos);
	}

	public void finishGame(boolean b) {
		// TODO Auto-generated method stub
		this.game.finishGame(b);
	}
}
