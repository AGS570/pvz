package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pvz.control.Level;
import pvz.logic.Game;
import pvz.logic.gameobjects.Zombie;
import pvz.utils.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Zombie.
 * <p>
 * These tests call zombie.update() directly — they do NOT call game.update() —
 * so zombie spawning side-effects have no impact on results.
 * <p>
 * Movement timing (confirmed by the reference implementation):
 *   MOVE_EVERY_CYCLES = 2. The internal counter starts at 0.
 *   Movement requires counter == 2, then resets to 0 (and immediately increments to 1).
 *   So: call 1 → counter 1, call 2 → counter 2, call 3 → MOVE (counter=1), call 4 → 2, call 5 → MOVE.
 *   First move: update call 3. Subsequent moves: every 2 update calls.
 */
class ZombieTest {

    private Game game;
    private Zombie zombie;
    private Position startPosition;

    @BeforeEach
    void setUp() {
        game = new Game(0L, Level.EASY);
        startPosition = new Position(0, 5);
        zombie = new Zombie(startPosition, game);
    }

    // ------------------------------------------------------------------ //
    //  Initial state                                                       //
    // ------------------------------------------------------------------ //

    @Test
    void initiallyAlive() {
        assertTrue(zombie.isAlive());
    }

    @Test
    void isInPosition_matchesConstructorPosition() {
        assertTrue(zombie.isInPosition(startPosition));
    }

    @Test
    void isInPosition_differentPosition_returnsFalse() {
        assertFalse(zombie.isInPosition(new Position(0, 4)));
        assertFalse(zombie.isInPosition(new Position(1, 5)));
    }

    // ------------------------------------------------------------------ //
    //  Damage and survival                                                 //
    // ------------------------------------------------------------------ //

    @Test
    void receivePlantAttack_reducesHealth() {
        // Zombie starts with 5 health; after 4 hits it should still be alive.
        zombie.receiveAttack(4);
        assertTrue(zombie.isAlive());
    }

    @Test
    void receivePlantAttack_5hits_kills() {
        zombie.receiveAttack(1);
        zombie.receiveAttack(1);
        zombie.receiveAttack(1);
        zombie.receiveAttack(1);
        zombie.receiveAttack(1);
        assertFalse(zombie.isAlive());
    }

    @Test
    void receivePlantAttack_4hits_stillAlive() {
        for (int i = 0; i < 4; i++) zombie.receiveAttack(1);
        assertTrue(zombie.isAlive());
    }

    @Test
    void receivePlantAttack_lethalInOneBlow() {
        zombie.receiveAttack(99);
        assertFalse(zombie.isAlive());
    }

    // ------------------------------------------------------------------ //
    //  Movement — timing                                                   //
    // ------------------------------------------------------------------ //

    @Test
    void update_call1_zombieDoesNotMove() {
        zombie.update();
        assertTrue(zombie.isInPosition(startPosition),
                "Zombie should not move after 1st update");
    }

    @Test
    void update_call2_zombieDoesNotMove() {
        zombie.update();
        zombie.update();
        assertTrue(zombie.isInPosition(startPosition),
                "Zombie should not move after 2nd update");
    }

    @Test
    void update_call3_zombieMovesLeft() {
        zombie.update();
        zombie.update();
        zombie.update();
        assertTrue(zombie.isInPosition(new Position(0, 4)),
                "Zombie should move one cell to the left on the 3rd update");
    }

    @Test
    void update_call4_zombieDoesNotMoveAgain() {
        zombie.update();
        zombie.update();
        zombie.update(); // move to (0, 4)
        zombie.update(); // one cycle of waiting
        assertTrue(zombie.isInPosition(new Position(0, 4)),
                "Zombie should not move on the 4th update");
    }

    @Test
    void update_call5_zombieMovesAgain() {
        for (int i = 0; i < 5; i++) zombie.update();
        assertTrue(zombie.isInPosition(new Position(0, 3)),
                "Zombie should move again on the 5th update (period = 2 after first move)");
    }

    @Test
    void update_movesEvery2Cycles_afterFirstMove() {
        // First move at update 3; then every 2 updates.
        for (int i = 0; i < 3; i++) zombie.update(); // moves to (0, 4)

        for (int step = 0; step < 3; step++) {
            Position posBeforeWait = zombie.isInPosition(new Position(0, 4 - step))
                    ? new Position(0, 4 - step) : null;

            zombie.update(); // wait cycle — must not move
            assertTrue(zombie.isInPosition(new Position(0, 4 - step)),
                    "Zombie should not move on the waiting cycle (step " + step + ")");

            zombie.update(); // move cycle
            assertTrue(zombie.isInPosition(new Position(0, 3 - step)),
                    "Zombie should move on the move cycle (step " + step + ")");
        }
    }

    // ------------------------------------------------------------------ //
    //  Movement — blocked by plant                                         //
    // ------------------------------------------------------------------ //

    @Test
    void update_plantToLeft_zombieDoesNotMove() {
        // Place a sunflower at (0, 4) — directly to the left of the zombie at (0, 5).
        game.addGameObject("s", new Position(0, 4));

        // After 3 updates the zombie would normally move, but the cell is occupied.
        // Note: the dead sunflower (killed by zombie attack during these cycles) still
        // occupies the position until game.update() calls removeDead(); here we only
        // call zombie.update(), so it remains blocking.
        for (int i = 0; i < 3; i++) zombie.update();

        assertTrue(zombie.isInPosition(startPosition),
                "Zombie should not move when the destination cell is occupied by a plant");
    }

    // ------------------------------------------------------------------ //
    //  Dead zombie                                                         //
    // ------------------------------------------------------------------ //

    @Test
    void deadZombie_update_doesNotMove() {
        zombie.receiveAttack(99); // kill
        assertFalse(zombie.isAlive());

        for (int i = 0; i < 5; i++) zombie.update();

        assertTrue(zombie.isInPosition(startPosition),
                "Dead zombie must not move");
    }

    @Test
    void deadZombie_update_doesNotThrow() {
        zombie.receiveAttack(99);
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 3; i++) zombie.update();
        });
    }

    // ------------------------------------------------------------------ //
    //  Icon format                                                         //
    // ------------------------------------------------------------------ //

    @Test
    void getIcon_containsZombieIdentifier() {
        String icon = zombie.getIcon();
        assertNotNull(icon);
        assertTrue(icon.contains("Z") || icon.contains("z"),
                "Icon should identify the game object type");
    }

    // ------------------------------------------------------------------ //
    //  Zombie attacks plant (observable via plant death)                   //
    // ------------------------------------------------------------------ //

    @Test
    void zombie_attacksPlantToItsLeft() {
        // Sunflower placed at (0, 4) has 1 health.
        // Zombie at (0, 5) attacks the cell to its left every cycle.
        // After the first update, the sunflower at (0, 4) should receive 1 damage
        // and die. We verify this by placing the sunflower and checking the icon
        // of the sunflower disappears from the board after the zombie attacks
        // and the game removes dead entities.

        game.addGameObject("s", new Position(0, 4)); // sunflower health = 1

        // zombie attacks (0, 4) on every update call
        zombie.update(); // sunflower takes 1 damage → health = 0

        // call game.update() to trigger removeDead() so the dead plant is removed
        game.update();

        assertTrue(game.isEmpty(new Position(0, 4)),
                "Plant should be dead and removed after zombie attack");
    }
}
