package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pvz.control.Controller;
import pvz.control.Level;
import pvz.logic.Game;
import pvz.logic.gameobjects.Peashooter;
import pvz.logic.gameobjects.Sunflower;
import pvz.view.Messages;
import pvz.utils.Position;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Controller command parsing and dispatch.
 *
 * Since parseUserInput() is private, tests are behavioral: they feed
 * commands through Controller.run() and assert observable output or game state.
 *
 * All test inputs end with "exit" so the game loop terminates.
 * Each test creates a fresh Game to avoid shared state.
 *
 * NOTE: these tests redirect System.out; they restore it after each test.
 */
class ControllerTest {

    private PrintStream originalOut;
    private InputStream originalIn;

    @BeforeEach
    void captureStreams() {
        originalOut = System.out;
        originalIn  = System.in;
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    // ------------------------------------------------------------------ //
    //  Helper                                                              //
    // ------------------------------------------------------------------ //

    /**
     * Runs the controller with the given input lines (each separated by '\n')
     * and returns everything written to System.out.
     */
    private String run(Game game, String input) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Controller controller = new Controller(game);
        controller.run();

        System.setOut(originalOut);
        System.setIn(originalIn);
        return buffer.toString();
    }

    private Game freshGame() {
        return new Game(0L, Level.EASY);
    }

    // ------------------------------------------------------------------ //
    //  help / h — does not advance the cycle                              //
    // ------------------------------------------------------------------ //

    @Test
    void helpCommand_longForm_printsHelp() {
        String out = run(freshGame(), "help\nexit\n");
        assertTrue(out.contains("Available commands"),
                "help should print the list of available commands");
    }

    @Test
    void helpCommand_shortForm_printsHelp() {
        String out = run(freshGame(), "h\nexit\n");
        assertTrue(out.contains("Available commands"));
    }

    @Test
    void helpCommand_caseInsensitive() {
        String out = run(freshGame(), "Help\nexit\n");
        assertTrue(out.contains("Available commands"));
    }

    @Test
    void helpCommand_doesNotAdvanceCycle() {
        Game game = freshGame();
        run(game, "help\nexit\n");
        assertEquals(0, game.getCycles(), "help must not advance the game cycle");
    }

    // ------------------------------------------------------------------ //
    //  list / l — does not advance the cycle                              //
    // ------------------------------------------------------------------ //

    @Test
    void listCommand_longForm_listsBothPlants() {
        String out = run(freshGame(), "list\nexit\n");
        // Descriptions use format "[S]unflower" / "[P]eashooter"
        assertTrue(out.contains("unflower"),
                "list should describe the Sunflower plant");
        assertTrue(out.contains("eashooter"),
                "list should describe the Peashooter plant");
    }

    @Test
    void listCommand_shortForm_listsBothPlants() {
        String out = run(freshGame(), "l\nexit\n");
        assertTrue(out.contains("unflower"));
        assertTrue(out.contains("eashooter"));
    }

    @Test
    void listCommand_doesNotAdvanceCycle() {
        Game game = freshGame();
        run(game, "list\nexit\n");
        assertEquals(0, game.getCycles());
    }

    // ------------------------------------------------------------------ //
    //  none / n / empty — advances the cycle                              //
    // ------------------------------------------------------------------ //

    @Test
    void noneCommand_longForm_advancesCycle() {
        Game game = freshGame();
        run(game, "none\nexit\n");
        assertEquals(1, game.getCycles());
    }

    @Test
    void noneCommand_shortForm_advancesCycle() {
        Game game = freshGame();
        run(game, "n\nexit\n");
        assertEquals(1, game.getCycles());
    }

    @Test
    void emptyInput_advancesCycle() {
        Game game = freshGame();
        run(game, "\nexit\n");
        assertEquals(1, game.getCycles());
    }

    @Test
    void multipleNoneCommands_advancesCyclesCorrectly() {
        Game game = freshGame();
        run(game, "none\nnone\nnone\nexit\n");
        assertEquals(3, game.getCycles());
    }

    // ------------------------------------------------------------------ //
    //  exit / e                                                            //
    // ------------------------------------------------------------------ //

    @Test
    void exitCommand_endsGame() {
        Game game = freshGame();
        run(game, "exit\n");
        assertTrue(game.playerQuits());
    }

    @Test
    void exitCommand_shortForm_endsGame() {
        Game game = freshGame();
        run(game, "e\n");
        assertTrue(game.playerQuits());
    }

    @Test
    void exitCommand_outputsGameOver() {
        String out = run(freshGame(), "exit\n");
        assertTrue(out.contains(Messages.GAME_OVER),
                "exit should print the game over message");
    }

    // ------------------------------------------------------------------ //
    //  add — valid plant placement                                         //
    // ------------------------------------------------------------------ //

    @Test
    void addSunflower_byLongName_deductsCoins() {
        Game game = freshGame();
        int expectedCoins = game.getCoins() - Sunflower.COST;
        run(game, "add sunflower 0 0\nexit\n");
        assertEquals(expectedCoins, game.getCoins()); // 50 - 20
    }

    @Test
    void addSunflower_byShortName_deductsCoins() {
        Game game = freshGame();
        int expectedCoins = game.getCoins() - Sunflower.COST;
        run(game, "add s 0 0\nexit\n");
        assertEquals(expectedCoins, game.getCoins());
    }

    @Test
    void addPeashooter_byLongName_deductsCoins() {
        Game game = freshGame();
        int expectedCoins = game.getCoins() - Peashooter.COST;
        run(game, "add peashooter 0 0\nexit\n");
        assertEquals(expectedCoins, game.getCoins()); // 50 - 50
    }

    @Test
    void addPeashooter_byShortName_deductsCoins() {
        Game game = freshGame();
        int expectedCoins = game.getCoins() - Peashooter.COST;
        run(game, "add p 0 0\nexit\n");
        assertEquals(expectedCoins, game.getCoins());
    }

    @Test
    void addCommand_advancesCycle() {
        Game game = freshGame();
        int expectedCycles = game.getCycles() + 1;
        run(game, "add s 0 0\nexit\n");
        assertEquals(expectedCycles, game.getCycles(), "Successful add must advance the cycle");
    }

    @Test
    void addCommand_shortFormAlias_works() {
        // 'a' is the short alias for 'add'
        Game game = freshGame();
        int expectedCoins = game.getCoins() - Sunflower.COST;
        run(game, "a s 0 0\nexit\n");
        assertEquals(expectedCoins, game.getCoins());
    }

    // ------------------------------------------------------------------ //
    //  add — invalid plant type → error, no coin deduction, no cycle      //
    // ------------------------------------------------------------------ //

    @Test
    void addCommand_invalidPlantType_printsError() {
        String out = run(freshGame(), "add zombie 0 0\nexit\n");
        assertTrue(out.toLowerCase().contains("invalid") || out.toLowerCase().contains("error"),
                "Invalid plant type should produce an error message");
    }

    @Test
    void addCommand_invalidPlantType_coinsUnchanged() {
        Game game = freshGame();
        int expectedCoins = game.getCoins();
        run(game, "add zombie 0 0\nexit\n");
        assertEquals(50, game.getCoins());
    }

    @Test
    void addCommand_invalidPlantType_cycleNotAdvanced() {
        Game game = freshGame();
        int expectedCycles = game.getCycles();
        run(game, "add zombie 0 0\nexit\n");
        assertEquals(expectedCycles, game.getCycles());
    }

    // ------------------------------------------------------------------ //
    //  add — invalid / out-of-bounds position → error, no coin deduction  //
    // ------------------------------------------------------------------ //

    @Test
    void addCommand_outOfBoundsPosition_printsError() {
        String out = run(freshGame(), "add s 8 0\nexit\n"); // col 8 is off-board
        assertTrue(out.toLowerCase().contains("invalid") || out.toLowerCase().contains("error"),
                "Out-of-bounds position should produce an error");
    }

    @Test
    void addCommand_outOfBoundsPosition_coinsUnchanged() {
        Game game = freshGame();
        int previousCoins = game.getCoins();
        run(game, "add s 8 0\nexit\n");
        assertEquals(previousCoins, game.getCoins());
    }

    @Test
    void addCommand_outOfBoundsPosition_cycleNotAdvanced() {
        Game game = freshGame();
        int previousCycles = game.getCycles();
        run(game, "add s 8 0\nexit\n");
        assertEquals(previousCycles, game.getCycles());
    }

    // ------------------------------------------------------------------ //
    //  add — occupied position → error, no coin deduction                 //
    // ------------------------------------------------------------------ //

    @Test
    void addCommand_occupiedPosition_coinsOnlyDeductedOnce() {
        Game game = freshGame();
        int previousCoins = game.getCoins() - Sunflower.COST;
        run(game, "add s 0 0\nadd s 0 0\nexit\n"); // second add at same pos
        // First add: 50 - 20 = 30. Second add should fail (position occupied).
        assertEquals(previousCoins, game.getCoins());
    }

    // ------------------------------------------------------------------ //
    //  add — missing parameters                                            //
    // ------------------------------------------------------------------ //

    @Test
    void addCommand_missingParameters_printsError() {
        String out = run(freshGame(), "add s\nexit\n"); // missing col and row
        assertTrue(out.toLowerCase().contains("missing") || out.toLowerCase().contains("parameter")
                        || out.toLowerCase().contains("error"),
                "Missing parameters should produce an error message");
    }

    @Test
    void addCommand_missingParameters_cycleNotAdvanced() {
        Game game = freshGame();
        int previousCycles = game.getCycles();
        run(game, "add s\nexit\n");
        assertEquals(previousCycles, game.getCycles());
    }

    // ------------------------------------------------------------------ //
    //  add — insufficient coins → no placement, no cycles advanced       //
    // ------------------------------------------------------------------ //

    @Test
    void addCommand_insufficientCoins_positionRemainsEmpty() {
        Game game = freshGame();
        // Buy a peashooter (50 coins) → coins = 0
        int expectedCoins = game.getCoins() - Peashooter.COST;
        run(game, "add p 0 0\nadd s 0 1\nexit\n"); // second add has no coins
        assertEquals(expectedCoins, game.getCoins());
        // The sunflower at (0, 1) should not have been placed, so its position is empty
        assertTrue(game.isEmpty(new Position(0, 1)));
    }

    @Test
    void addCommand_insufficientCoins_cycleNotAdvanced() {
        Game game = freshGame();
        int previousCycles = game.getCycles();
        int expectedCycles = previousCycles + 1;
        // Try to add a peashooter and a sunflower
        run(game, "add p 0 0\nadd s 0 1\nexit\n"); // second add has no coins
        assertEquals(expectedCycles, game.getCycles());
    }

    @Test
    void addCommand_insufficientCoins_printsError() {
        String out = run(freshGame(), "add p 0 0\nadd s 0 1\nexit\n"); // missing col and row
        assertTrue(out.toLowerCase().contains(Messages.NOT_ENOUGH_COINS.toLowerCase()),
            "A message warning about not having enough coins must be shown");
    }

    // ------------------------------------------------------------------ //
    //  reset — restores game state                                         //
    // ------------------------------------------------------------------ //

    @Test
    void resetCommand_restoresCoins() {
        Game game = freshGame();
        run(game, "add s 0 0\nreset\nexit\n");
        assertEquals(Game.INITIAL_COINS, game.getCoins());
    }

    @Test
    void resetCommand_restoresCycles() {
        Game game = freshGame();
        int expectedCycles = game.getCycles();
        run(game, "none\nreset\nexit\n");
        assertEquals(expectedCycles, game.getCycles());
    }

    @Test
    void resetCommand_shortForm_works() {
        Game game = freshGame();
        run(game, "add s 0 0\nr\nexit\n");
        assertEquals(Game.INITIAL_COINS, game.getCoins());
    }

    // ------------------------------------------------------------------ //
    //  Unknown command                                                     //
    // ------------------------------------------------------------------ //

    @Test
    void unknownCommand_printsError() {
        String out = run(freshGame(), "xyzzy\nexit\n");
        assertTrue(out.toLowerCase().contains("unknown") || out.toLowerCase().contains("error"),
                "Unknown command should produce an error");
    }

    @Test
    void unknownCommand_doesNotAdvanceCycle() {
        Game game = freshGame();
        run(game, "xyzzy\nexit\n");
        assertEquals(0, game.getCycles());
    }

    // ------------------------------------------------------------------ //
    //  Board is printed only when the cycle advances                       //
    // ------------------------------------------------------------------ //

    @Test
    void addCommand_onFailure_boardNotReprinted() {
        // Board print contains "Sun coins:". Count how many times it appears.
        // On an invalid add: the board should not be re-drawn (spec requirement).
        // On a valid add: the board IS redrawn once.
        Game game = freshGame();
        String out = run(game, "add zombie 0 0\nexit\n");

        // Count the number of "Number of cycles" headers (one per board draw)
        long draws = out.lines()
                .filter(line -> line.startsWith("Number of cycles:"))
                .count();
        // Only the initial draw and exit (final state is not redrawn after exit)
        // → exactly 1 draw (the initial printGame() at run() start).
        assertEquals(1, draws,
                "Board should not be redrawn when the add command fails");
    }

    @Test
    void addCommand_onSuccess_boardIsReprinted() {
        Game game = freshGame();
        String out = run(game, "add s 0 0\nexit\n");

        long draws = out.lines()
                .filter(line -> line.startsWith("Number of cycles:"))
                .count();
        // Initial draw + draw after successful add = 2
        assertEquals(2, draws,
                "Board should be redrawn after a successful add");
    }
}
