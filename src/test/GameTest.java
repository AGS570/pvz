package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pvz.logic.Game;
import pvz.control.Level;
import pvz.utils.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Game via its public API.
 * <p>
 * Seed 0 + EASY (frequency 0.1): with Random(0), the first nextDouble() is ~0.73,
 * so no zombie spawns in the first cycle. This keeps early-cycle tests deterministic.
 */
class GameTest {

    // Fixed seed gives predictable random behavior for zombie spawning.
    private static final long SEED = 0L;
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(SEED, Level.EASY);
    }

    // ------------------------------------------------------------------ //
    //  Initial state                                                       //
    // ------------------------------------------------------------------ //

    @Test
    void initialCoins_are50() {
        assertEquals(50, game.getCoins());
    }

    @Test
    void initialCycles_are0() {
        assertEquals(0, game.getCycles());
    }

    @Test
    void initialRemainingZombies_matchLevel() {
        assertEquals(Level.EASY.getNumberOfZombies(), game.getRemainingZombies());
    }

    @Test
    void gameNotFinished_initially() {
        assertFalse(game.hasGameFinished());
    }

    @Test
    void playerNotQuitting_initially() {
        assertFalse(game.playerQuits());
    }

    // ------------------------------------------------------------------ //
    //  Plant placement — cost deduction                                    //
    // ------------------------------------------------------------------ //

    @Test
    void addSunflower_byLongName_deductsCoins() {
        game.addGameObject("sunflower", new Position(0, 0));
        assertEquals(30, game.getCoins());
    }

    @Test
    void addSunflower_byShortName_deductsCoins() {
        game.addGameObject("s", new Position(0, 0));
        assertEquals(30, game.getCoins());
    }

    @Test
    void addPeashooter_byLongName_deductsCoins() {
        game.addGameObject("peashooter", new Position(0, 0));
        assertEquals(0, game.getCoins());
    }

    @Test
    void addPeashooter_byShortName_deductsCoins() {
        game.addGameObject("p", new Position(0, 0));
        assertEquals(0, game.getCoins());
    }

    // ------------------------------------------------------------------ //
    //  Plant placement — position occupancy                               //
    // ------------------------------------------------------------------ //

    @Test
    void addSunflower_positionIsOccupied() {
        Position pos = new Position(1, 2);
        game.addGameObject("s", pos);
        assertFalse(game.isEmpty(pos));
    }

    @Test
    void addPeashooter_positionIsOccupied() {
        Position pos = new Position(2, 3);
        game.addGameObject("p", pos);
        assertFalse(game.isEmpty(pos));
    }

    @Test
    void emptyPosition_isReportedEmpty() {
        assertTrue(game.isEmpty(new Position(0, 0)));
    }

    // ------------------------------------------------------------------ //
    //  Insufficient coins — plant not placed, coins unchanged             //
    // ------------------------------------------------------------------ //

    @Test
    void addSunflower_insufficientCoins_positionRemainsEmpty() {
        game.addGameObject("p", new Position(0, 0)); // costs 50 → coins = 0
        Position pos = new Position(0, 1);
        game.addGameObject("s", pos);                // costs 20, but 0 coins
        assertTrue(game.isEmpty(pos));
    }

    @Test
    void addSunflower_insufficientCoins_coinsUnchanged() {
        game.addGameObject("p", new Position(0, 0)); // coins = 0
        game.addGameObject("s", new Position(0, 1));
        assertEquals(0, game.getCoins());
    }

    @Test
    void addPeashooter_insufficientCoins_positionRemainsEmpty() {
        game.addGameObject("s", new Position(0, 0)); // coins = 30
        game.addGameObject("s", new Position(0, 1)); // coins = 10
        Position pos = new Position(0, 2);
        game.addGameObject("p", pos);                // costs 50, only 10 coins
        assertTrue(game.isEmpty(pos));
    }

    @Test
    void addPeashooter_insufficientCoins_coinsUnchanged() {
        game.addGameObject("s", new Position(0, 0));
        game.addGameObject("s", new Position(0, 1));
        game.addGameObject("p", new Position(0, 2));
        assertEquals(10, game.getCoins());
    }

    // ------------------------------------------------------------------ //
    //  isInsideBoard                                                     //
    // ------------------------------------------------------------------ //

    @Test
    void isInsideBoard_topLeftCorner() {
        assertTrue(game.isInsideBoard(new Position(0, 0)));
    }

    @Test
    void isInsideBoard_bottomRightCorner() {
        assertTrue(game.isInsideBoard(new Position(Game.NUM_ROWS - 1, Game.NUM_COLS - 1)));
    }

    @Test
    void isInsideBoard_negativeRow() {
        assertFalse(game.isInsideBoard(new Position(-1, 0)));
    }

    @Test
    void isInsideBoard_negativeColumn() {
        assertFalse(game.isInsideBoard(new Position(0, -1)));
    }

    @Test
    void isInsideBoard_rowEqualsNumRows_isOutside() {
        assertFalse(game.isInsideBoard(new Position(Game.NUM_ROWS, 0)));
    }

    @Test
    void isInsideBoard_colEqualsNumCols_isOutside() {
        assertFalse(game.isInsideBoard(new Position(0, Game.NUM_COLS)));
    }

    // ------------------------------------------------------------------ //
    //  checkGameObject                                                     //
    // ------------------------------------------------------------------ //

    @Test
    void checkGameObject_sunflowerLongName() {
        assertTrue(game.checkGameObject("sunflower"));
    }

    @Test
    void checkGameObject_sunflowerShortName() {
        assertTrue(game.checkGameObject("s"));
    }

    @Test
    void checkGameObject_peashooterLongName() {
        assertTrue(game.checkGameObject("peashooter"));
    }

    @Test
    void checkGameObject_peashooterShortName() {
        assertTrue(game.checkGameObject("p"));
    }

    @Test
    void checkGameObject_caseInsensitive() {
        assertTrue(game.checkGameObject("SUNFLOWER"));
        assertTrue(game.checkGameObject("S"));
        assertTrue(game.checkGameObject("PEASHOOTER"));
        assertTrue(game.checkGameObject("P"));
    }

    @Test
    void checkGameObject_unknownName_returnsFalse() {
        assertFalse(game.checkGameObject("zombie"));
        assertFalse(game.checkGameObject("tree"));
        assertFalse(game.checkGameObject(""));
        assertFalse(game.checkGameObject("peashoot")); // common typo
    }

    // ------------------------------------------------------------------ //
    //  update — cycle counter                                              //
    // ------------------------------------------------------------------ //

    @Test
    void update_incrementsCycles() {
        game.update();
        assertEquals(1, game.getCycles());
        game.update();
        assertEquals(2, game.getCycles());
    }

    // ------------------------------------------------------------------ //
    //  Sunflower coin generation through the game loop                    //
    // ------------------------------------------------------------------ //

    @Test
    void sunflower_generatesCoins_after3GameCycles() {
        // Confirmed by integration test 00-easy_25:
        // Sunflower placed at cycle 2 (via "add" command, which runs one update).
        // Coins increase from 30 to 40 when the board shows cycle 5.
        // That means 3 additional update() calls after placement trigger the first generation.
        // (Internal cooldown counter: 0→1→2→3→generate, 4 updates total from object creation,
        //  but the first update runs in the same cycle as placement, leaving 3 more to go.)
        game.addGameObject("s", new Position(0, 0)); // coins = 30, counter starts at 0
        game.update(); // counter 0→1
        game.update(); // counter 1→2
        game.update(); // counter 2→3
        game.update(); // counter 3==COOLDOWN → generate, counter=1
        assertTrue(game.getCoins() >= 40,
                "Sunflower should have generated 10 coins after 4 update cycles");
    }

    @Test
    void sunflower_generatesCoins_periodically() {
        game.addGameObject("s", new Position(0, 0)); // coins = 30

        // 4 updates for the first generation (see sunflower_generatesCoins_after3GameCycles)
        for (int i = 0; i < 4; i++) game.update();
        int coinsAfterFirst = game.getCoins();
        assertTrue(coinsAfterFirst >= 40);

        // 3 more cycles → second generation
        for (int i = 0; i < 3; i++) game.update();
        assertTrue(game.getCoins() >= coinsAfterFirst + 10);
    }

    // ------------------------------------------------------------------ //
    //  quit / reset                                                        //
    // ------------------------------------------------------------------ //

    @Test
    void quit_setsGameFinished() {
        game.quit();
        assertTrue(game.hasGameFinished());
    }

    @Test
    void quit_setsPlayerQuits() {
        game.quit();
        assertTrue(game.playerQuits());
    }

    @Test
    void reset_restoresCoins() {
        game.addGameObject("s", new Position(0, 0));
        game.reset();
        assertEquals(50, game.getCoins());
    }

    @Test
    void reset_restoresCycles() {
        game.update();
        game.update();
        game.reset();
        assertEquals(0, game.getCycles());
    }

    @Test
    void reset_restoresRemainingZombies() {
        game.reset();
        assertEquals(Level.EASY.getNumberOfZombies(), game.getRemainingZombies());
    }

    @Test
    void reset_clearsPlants() {
        Position pos = new Position(0, 0);
        game.addGameObject("s", pos);
        game.reset();
        assertTrue(game.isEmpty(pos));
    }

    @Test
    void reset_clearQuitFlag() {
        game.quit();
        game.reset();
        assertFalse(game.playerQuits(),
                "reset() should clear the quit flag so the game can continue");
        assertFalse(game.hasGameFinished(),
                "reset() should make hasGameFinished() return false");
    }
}
