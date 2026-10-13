package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pvz.control.Level;
import pvz.logic.Game;
import pvz.logic.gameobjects.Sunflower;
import pvz.utils.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Sunflower.
 * <p>
 * These tests call sunflower.update() directly — they do NOT go through
 * game.update() — so zombie spawning has no effect on the results.
 * <p>
 * Coin-generation timing (confirmed by integration test 00-easy_25):
 *   The internal cooldown counter starts at 0 and COOLDOWN = 3.
 *   The counter increments each update until it reaches 3, then resets to 1.
 *   So: update 1→counter 1, update 2→2, update 3→3, update 4→GENERATE (counter=1).
 *   After the first generation, the period is exactly 3 updates.
 */
class SunflowerTest {

    private Game game;
    private Position position;
    private Sunflower sunflower;

    @BeforeEach
    void setUp() {
        game = new Game(0L, Level.EASY);
        position = new Position(0, 0);
        sunflower = new Sunflower(position, game);
    }

    // ------------------------------------------------------------------ //
    //  Initial state                                                       //
    // ------------------------------------------------------------------ //

    @Test
    void initiallyAlive() {
        assertTrue(sunflower.isAlive());
    }

    @Test
    void isInPosition_matchesConstructorPosition() {
        assertTrue(sunflower.isInPosition(position));
    }

    @Test
    void isInPosition_differentPosition_returnsFalse() {
        assertFalse(sunflower.isInPosition(new Position(1, 1)));
    }

    @Test
    void cost_is20() {
        assertEquals(20, Sunflower.COST);
    }

    // ------------------------------------------------------------------ //
    //  Damage and survival                                                 //
    // ------------------------------------------------------------------ //

    @Test
    void receiveZombieAttack_reducesHealth() {
        // Sunflower has 1 health; any positive damage kills it.
        // We verify isAlive() flips to false after taking lethal damage.
        sunflower.receiveDamage(1);
        assertFalse(sunflower.isAlive());
    }

    @Test
    void receiveZombieAttack_belowZero_notAlive() {
        sunflower.receiveDamage(99);
        assertFalse(sunflower.isAlive());
    }

    @Test
    void noDamage_stillAlive() {
        sunflower.receiveDamage(0);
        assertTrue(sunflower.isAlive());
    }

    // ------------------------------------------------------------------ //
    //  Coin generation timing                                              //
    // ------------------------------------------------------------------ //

    @Test
    void firstCoinGeneration_happensAfterCooldownUpdates() {
        int initialCoins = game.getCoins();

        int updatesNeeded = 0;
        for (int i = 0; i < 10; i++) {
            sunflower.update();
            updatesNeeded++;
            if (game.getCoins() > initialCoins) break;
        }

        assertTrue(game.getCoins() > initialCoins,
                "Sunflower should have generated coins within 10 updates");
        assertEquals(initialCoins + 10, game.getCoins(),
                "Sunflower should generate exactly 10 coins per cycle");
    }

    @Test
    void subsequentCoinGeneration_periodIs3Updates() {
        // Reset to a clean, known state before measuring the period:
        // the unbounded search above may advance the counter by an unknown amount.
        game = new Game(0L, Level.EASY);
        sunflower = new Sunflower(position, game);
        int coins = game.getCoins();
        int i = 0;
        while (game.getCoins() == coins && i < 10) { sunflower.update(); i++; }
        // First generation happened at update i.

        int coinsAfterFirst = game.getCoins();
        int updatesSinceFirst = 0;
        while (game.getCoins() == coinsAfterFirst && updatesSinceFirst < 5) {
            sunflower.update();
            updatesSinceFirst++;
        }

        assertEquals(3, updatesSinceFirst,
                "Sunflower should generate coins every 3 updates after the first");
        assertEquals(coinsAfterFirst + 10, game.getCoins());
    }

    @Test
    void deadSunflower_doesNotGenerateCoins() {
        sunflower.receiveDamage(1); // kill sunflower
        assertFalse(sunflower.isAlive());

        int coinsBefore = game.getCoins();
        for (int i = 0; i < 10; i++) sunflower.update();

        assertEquals(coinsBefore, game.getCoins(),
                "Dead sunflower must not generate coins");
    }

    // ------------------------------------------------------------------ //
    //  Icon format                                                         //
    // ------------------------------------------------------------------ //

    @Test
    void getIcon_containsHealthValue() {
        String icon = sunflower.getIcon();
        assertNotNull(icon);
        // Icon should reflect current health (format "S[01]" or similar)
        assertTrue(icon.contains("S") || icon.contains("s"),
                "Icon should identify the plant type");
    }
}
