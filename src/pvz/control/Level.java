package pvz.control;

/**
 * Difficulty preset for a game session.
 *
 * <p>Each constant bundles the total zombie quota and the per-cycle spawn
 * probability. Higher difficulties spawn more zombies and spawn them more
 * frequently. These values are fixed at construction and never change during
 * a game.
 */
public enum Level {
	EASY(3,0.1), HARD(5,0.2), INSANE(10,0.3);
	// TODO fill your code
	private int numberOfZombies;
	
	private double zombieFrequency;

	// TODO fill your code

	private Level(int numberOfZombies, double zombieFrequency) {
		this.numberOfZombies = numberOfZombies;
		this.zombieFrequency = zombieFrequency;
	}
	/**
	 * Parse a string and return any matching level
	 * 
	 * @param inputString string to parse
	 * @return the parsed {@link Level} or <code>null</code> if none match.
	 */
	public static Level valueOfIgnoreCase(String inputString) {
		for (Level level : Level.values()) {
			if (level.name().equalsIgnoreCase(inputString)) {
				return level;
			}
		}
		return null;
	}

	/**
	 * Returns a string representation of all the levels joined with <code>separator</code>
	 * 
	 * @param separator String used as separator
	 * 
	 * @return the string resulted from joining all levels using <code>separator</code>
	 */
	public static String all(String separator) {
		StringBuilder buffer = new StringBuilder();
		int levelCount = 0;
		for (Level level : Level.values()) {
			if (levelCount > 0) {
				buffer.append(separator);
			}
			buffer.append(level.name());
			levelCount++;
		}
		return buffer.toString();
	}

	public int getNumberOfZombies() {
		// TODO Auto-generated method stub
		return this.numberOfZombies;
	}
	public double getZombieFrequency() {
		// TODO Auto-generated method stub
		return this.zombieFrequency;
	}
	
}





