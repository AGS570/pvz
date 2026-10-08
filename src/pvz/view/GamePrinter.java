package pvz.view;
import pvz.logic.Position;
import static pvz.view.Messages.PROMPT;
import static pvz.view.Messages.debug;
import static pvz.view.Messages.error;
import static pvz.utils.StringUtils.*;

import pvz.logic.Game;
import pvz.utils.Position;
import pvz.utils.StringUtils;

import java.util.Scanner;

/**
 * View component of the game (the V in MVC).
 *
 * <p>Renders the current game state as a formatted string: a status header
 * (cycle count, sun coins, remaining zombies) followed by the ASCII board grid.
 * Also produces the end-of-game message distinguishing player win, player quit,
 * and zombie win outcomes.
 *
 * <p>This class is read-only with respect to {@link Game}: it queries game state
 * through the public API and never modifies it. All string literals and format
 * templates are sourced from {@link Messages}.
 */
public class GamePrinter implements GameView {

	private static final String SPACE = " ";
	
	private static final String CELL_BORDER_CHAR = "─";

	private static final String VERTICAL_DELIMITER = "|";

	private static final String NEW_LINE = System.lineSeparator();

	private static final int MARGIN_SIZE = 2;
	
	private static final String MARGIN = repeat(SPACE, MARGIN_SIZE);

	private static final int CELL_SIZE = 8;

	private static final String CELL_BORDER = repeat(CELL_BORDER_CHAR, CELL_SIZE);

	private static final String ROW_BORDER = SPACE + repeat(CELL_BORDER + SPACE, Game.NUM_COLS);

	private static final String INDENTED_ROW_BORDER = String.format("%n%s%s%n", MARGIN, ROW_BORDER);

	private Game game;
	private Scanner scanner;

	public GamePrinter(Game game) {
		this.game = game;
		this.scanner = new Scanner(System.in);
	}

	/**
	 * Builds a string that represent the game status: cycles, suncoins, remaining zombies.
	 * 
	 * @return the string that represents the game status.
	 */
	private String getInfo() {
		StringBuilder buffer = new StringBuilder();

		// 1. Añadimos el número de ciclos
		buffer.append(String.format(Messages.NUMBER_OF_CYCLES, game.getCycles()));
				
		// 2. Añadimos los soles del jugador
		buffer.append(String.format(Messages.NUMBER_OF_COINS, game.getSuncoins()));
				
		// 3. Añadimos los zombis que faltan por salir
		buffer.append(String.format(Messages.REMAINING_ZOMBIES, game.getRemainingZombies()));

		return buffer.toString();
	}

	/**
	 * Prints complete representation (status+board) of the game.
	 */
	@Override
	public void showGame() {
		StringBuilder str = new StringBuilder();

		// Game Status
		str.append(getInfo());

		// Paint game board
		str.append(INDENTED_ROW_BORDER);

		for (int row = 0; row < Game.NUM_ROWS; row++) {
			str.append(MARGIN).append(VERTICAL_DELIMITER);
			for (int col = 0; col < Game.NUM_COLS; col++) {
				str.append(StringUtils.centre(game.positionToString(new Position(row, col)), CELL_SIZE)).append(VERTICAL_DELIMITER);
			}
			str.append(INDENTED_ROW_BORDER);
		}

		System.out.println(str);
	}

	/**
	 * Prints the message to be printed once the game has finished.
	 */
	@Override
	public void showEndMessage() {
		StringBuilder buffer = new StringBuilder(Messages.GAME_OVER);
		// TODO fill your code
		System.out.println(buffer);
	}

	@Override
	public void showError(String message) {
		System.out.println(error(message));
	}

	@Override
	public void showMessage(String message) {
		System.out.println(message);
	}

	@Override
	public String[] getPrompt() {
		System.out.print(PROMPT);
		String line = scanner.nextLine();
		String[] words = line.toLowerCase().trim().split("\\s+");

		System.out.println(debug(line));

		return words;
	}
}
