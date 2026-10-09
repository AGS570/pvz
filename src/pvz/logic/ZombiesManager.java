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
			this.zombies.insert(new Zombie(p,this.game));
		}
		return canAdd;
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
	
	// TODO fill your code
}
