/* Name: GameSyncTarget
 * Description: This interface represents a game that can be synchronized with
 * a remote client.
 */

package network;

import model.Game;
import model.Player;

/**
 * The {@code GameSyncTarget} interface provides the operations needed to
 * synchronize a game of checkers with remote clients.
 */
public interface GameSyncTarget {

	/**
	 * Gets the game of checkers being played.
	 *
	 * @return the current game.
	 */
	public Game getGame();

	/**
	 * Updates the game state.
	 *
	 * @param testValue	if true, the state is only updated if the current
	 * 					state is equal to the expected state.
	 * @param newState	the new game state.
	 * @param expected	the expected current game state.
	 * @return true if the game state was updated.
	 */
	public boolean setGameState(boolean testValue, String newState,
			String expected);

	/**
	 * Sends the current game state to the client of the specified session.
	 *
	 * @param s	the session to send the game state to.
	 */
	public void sendGameState(Session s);

	/**
	 * Updates the game after its state changed (e.g. lets a non-human player
	 * take its turn).
	 */
	public void update();

	/**
	 * Gets the player whose turn it is.
	 *
	 * @return the current player.
	 */
	public Player getCurrentPlayer();

	/**
	 * Gets player 1 (black checkers).
	 *
	 * @return player 1.
	 */
	public Player getPlayer1();

	/**
	 * Gets player 2 (white checkers).
	 *
	 * @return player 2.
	 */
	public Player getPlayer2();
}
