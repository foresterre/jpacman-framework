package nl.tudelft.jpacman.level;

import java.util.Map;

import io.vavr.collection.List;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.ghost.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostColor;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * Factory that creates levels and units.
 *
 * @author Jeroen Roosen 
 */
public class LevelFactory {

    private static final int GHOSTS = 4;
    private static final int BLINKY = 0;
    private static final int INKY = 1;
    private static final int PINKY = 2;
    private static final int CLYDE = 3;

    /**
     * The default value of a pellet.
     */
    private static final int PELLET_VALUE = 10;

    /**
     * The sprite store that provides sprites for units.
     */
    private final PacManSprites sprites;

    /**
     * Used to cycle through the various ghost types.
     */
    private int ghostIndex;

    /**
     * The factory providing ghosts.
     */
    private final GhostFactory ghostFact;

    /**
     * Creates a new level factory.
     *
     * @param spriteStore
     *            The sprite store providing the sprites for units.
     * @param ghostFactory
     *            The factory providing ghosts.
     */
    public LevelFactory(PacManSprites spriteStore, GhostFactory ghostFactory) {
        this.sprites = spriteStore;
        this.ghostIndex = -1;
        this.ghostFact = ghostFactory;
    }

    /**
     * Creates a new level from the provided data.
     *
     * @param board
     *            The board with all ghosts and pellets occupying their squares.
     * @param ghosts
     *            A list of all ghosts on the board.
     * @return A new level for the board.
     */
    public Level createLevel(Board board, List<Ghost> ghosts, List<Pellet> pellets, Player player) {
        return new Level(player, board, ghosts, pellets);
    }

    /**
     * Creates a new ghost.
     *
     * @return The new ghost.
     */
    Ghost createGhost(Square square) {
        ghostIndex++;
        ghostIndex %= GHOSTS;
        switch (ghostIndex) {
            case BLINKY:
                return ghostFact.createBlinky(square, Direction.EAST);
            case INKY:
                return ghostFact.createInky(square, Direction.EAST);
            case PINKY:
                return ghostFact.createPinky(square, Direction.EAST);
            case CLYDE:
                return ghostFact.createClyde(square, Direction.EAST);
            default:
                return new RandomGhost(square, Direction.EAST, sprites.getGhostSprite(GhostColor.RED));
        }
    }

    /**
     * Creates a new pellet.
     *
     * @return The new pellet.
     */
    public Pellet createPellet(Square square) {
        return new Pellet(square, PELLET_VALUE, sprites.getPelletSprite());
    }

    /**
     * Implementation of an NPC that wanders around randomly.
     *
     * @author Jeroen Roosen
     */
    private static final class RandomGhost extends Ghost {

        /**
         * The suggested delay between moves.
         */
        private static final long DELAY = 175L;

        /**
         * Creates a new random ghost.
         *
         * @param ghostSprite
         *            The sprite for the ghost.
         */
        RandomGhost(Square square, Direction direction, Map<Direction, Sprite> ghostSprite) {
            super(square, direction, ghostSprite, (int) DELAY, 0);
        }

        @Override
        public Direction nextMove(Entities entities) {
            return randomMove();
        }

        @Override
        public Ghost movedTo(Square square, Direction direction) {
            return new RandomGhost(square, direction, sprites);
        }
    }
}
