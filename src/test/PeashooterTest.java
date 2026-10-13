package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pvz.control.Level;
import pvz.logic.Game;
import pvz.logic.gameobjects.Peashooter;
import pvz.utils.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Peashooter.
 * <p>
 * The peashooter's shoot() is private; its effect (damaging zombies) is tested
 * indirectly through game state. Calling update() when no zombie is in range
 * must not throw and must not change the peashooter itself.
 * <p>
 * The zombie-killing scenario is covered in GameTest to avoid needing
 * fine-grained access to the zombie list from outside Game.
 */
class PeashooterTest {

    private Game game;
    private Position position;
    private Peashooter peashooter;

    @BeforeEach
    void setUp() {
        game = new Game(0L, Level.EASY);
        position = new Position(0, 3);
        peashooter = new Peashooter(position, game);
    }

    // ------------------------------------------------------------------ //
    //  Initial state                                                       //
    // ------------------------------------------------------------------ //

    @Test
    void initiallyAlive() {
        assertTrue(peashooter.isAlive());
    }

    @Test
    void isInPosition_matchesConstructorPosition() {
        assertTrue(peashooter.isInPosition(position));
    }

    @Test
    void isInPosition_differentPosition_returnsFalse() {
        assertFalse(peashooter.isInPosition(new Position(0, 4)));
        assertFalse(peashooter.isInPosition(new Position(1, 3)));
    }

    @Test
    void cost_is50() {
        assertEquals(50, Peashooter.COST);
    }

    // ------------------------------------------------------------------ //
    //  Damage and survival                                                 //
    // ------------------------------------------------------------------ //

    @Test
    void receiveZombieAttack_reducesHealth() {
        // Peashooter starts with 3 health. After 1 hit it should still be alive.
        peashooter.receiveDamage(1);
        assertTrue(peashooter.isAlive());
    }

    @Test
    void receiveZombieAttack_3hits_kills() {
        peashooter.receiveDamage(1);
        peashooter.receiveDamage(1);
        peashooter.receiveDamage(1);
        assertFalse(peashooter.isAlive());
    }

    @Test
    void receiveZombieAttack_2hits_stillAlive() {
        peashooter.receiveDamage(1);
        peashooter.receiveDamage(1);
        assertTrue(peashooter.isAlive());
    }

    @Test
    void receiveZombieAttack_lethal_inOneBlow() {
        peashooter.receiveDamage(99);
        assertFalse(peashooter.isAlive());
    }

    @Test
    void noDamage_stillAlive() {
        peashooter.receiveDamage(0);
        assertTrue(peashooter.isAlive());
    }

    // ------------------------------------------------------------------ //
    //  Shooting behaviour                                                  //
    // ------------------------------------------------------------------ //

    @Test
    void update_noZombieInRange_doesNotThrow() {
        // Peashooter should handle the absence of zombies gracefully.
        assertDoesNotThrow(() -> peashooter.update());
    }

    @Test
    void update_noZombieInRange_peashooterRemainsAlive() {
        peashooter.update();
        assertTrue(peashooter.isAlive());
    }

    @Test
    void update_noZombieInRange_coinsUnchanged() {
        int coinsBefore = game.getCoins();
        peashooter.update();
        assertEquals(coinsBefore, game.getCoins());
    }

    @Test
    void deadPeashooter_update_doesNotThrow() {
        peashooter.receiveDamage(99);
        assertDoesNotThrow(() -> peashooter.update());
    }

    // ------------------------------------------------------------------ //
    //  Icon format                                                         //
    // ------------------------------------------------------------------ //

    @Test
    void getIcon_containsPeashooterIdentifier() {
        String icon = peashooter.getIcon();
        assertNotNull(icon);
        assertTrue(icon.contains("P") || icon.contains("p"),
                "Icon should identify the plant type");
    }
}
