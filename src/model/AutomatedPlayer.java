/* Name: AutomatedPlayer
 * Description: This class represents a player that makes its own moves
 * without user interaction.
 */

package model;

/**
 * The {@code AutomatedPlayer} class is an abstract class that represents a
 * player that updates the game itself when it is its turn, via
 * {@link #updateGame(Game)}.
 */
public abstract class AutomatedPlayer extends Player {

	@Override
	public boolean isHuman() {
		return false;
	}

	/**
	 * Updates the game state to take a move for the current player. If there
	 * is a move available that is multiple skips, it may be performed at once
	 * by this method or one skip at a time.
	 *
	 * @param game	the game to update.
	 */
	public abstract void updateGame(Game game);
}
