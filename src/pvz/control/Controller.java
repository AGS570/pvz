package pvz.control;

import pvz.logic.Game;
import pvz.view.GamePrinter;
import pvz.view.GameView;
import pvz.view.Messages;

/**
 * Input/output coordinator of the game (the C in MVC).
 *
 * <p>Owns the game loop: reads a line from stdin, parses it into a
 * command, validates parameters (plant type, position), delegates
 * state changes to {@link Game}, and triggers a board reprint via
 * {@link tp1.pvz.view.GameView>} when the cycle advances. It holds no game
 * state of its own; the source of truth is always {@link Game}.
 */
public class Controller {

	private final Game game;
	private final GameView view;

	public Controller(Game game) {
		this.game = game;
		this.view = new GamePrinter(game);
	}

	/**
	 * Runs the game logic.
	 */
	public void run() {
    view.showGame();
				while (!game.hasGameFinished()) {
					// Muestra "Command >" y lee lo que el usuario escribe, separándolo por espacios en un array[cite: 11].
					String[] words = view.getPrompt();
					
					// Extraemos la orden principal, la pasamos a minúsculas y evitamos errores si solo se pulsa Enter[cite: 98].
					String command = "";
					if (words.length > 0 && !words[0].isBlank()) {
						command = words[0].toLowerCase();
					}

					// Interruptores (booleanos) para saber si debemos avanzar el tiempo o salir del bucle[cite: 89].
					boolean cycleAdvanced = false;
					boolean exitGame = false;

					// 2. USER ACTION: Evaluamos el comando introducido por el usuario[cite: 1].
					switch (command) {
						case "":
						case "n":
						case "none":
							// El comando "none" (o Enter vacío) hace que pase el turno sin que el jugador haga nada[cite: 1].
							cycleAdvanced = true;
							break;
							
						case "e":
						case "exit":
							// El comando "exit" enciende el interruptor para forzar el final de la partida[cite: 1].
							exitGame = true;
							break;
						case "r":
						case "reset":
							this.game.reset();
							break;
						case"h":
						case "help":
							view.showError(Messages.HELP);
							break;
						case "l":
						case "list":
							//TODO
							view.showMessage(Messages.SUNFLOWER_DESCRIPTION + "\n" + Messages.PEASHOOTER_DESCRIPTION);
							break;
						case "a":
						case "add":
							if(words.length < 4) {
								if(words[1] == "peaShooter" || words[1]=="p") {
									this.game.addPeaShooter(Integer.parseInt(words[2]), Integer.parseInt(words[3]));
								}else if(words[1] == "SunFlower" || words[3]=="s") {
									this.game.addSunFlower(Integer.parseInt(words[2]), Integer.parseInt(words[3]));
								}else
									view.showError(Messages.INVALID_GAME_OBJECT);
							}else {
								view.showError(Messages.COMMAND_PARAMETERS_MISSING);
							}
						default:
							// Si no reconoce la orden, muestra un error. El tiempo no avanza[cite: 1].
							view.showError(Messages.UNKNOWN_COMMAND);
					}

					// Si el usuario escribió "exit", el interruptor rompe el bucle while inmediatamente.
					if (exitGame) {
						break;
					}

					// Si el comando introducido consume un ciclo de tiempo (como "none")...
					if (cycleAdvanced) {
						// 3. GAME ACTION y 4. UPDATE: Se avisa a la lógica (Game) para que actúen zombis y plantas[cite: 1].
						
						game.update();
						
						// 1. DRAW: Se vuelve a pintar el tablero para reflejar los movimientos y cambios[cite: 1].
						view.showGame();
					}
				}
				view.showEndMessage();
	}
}
