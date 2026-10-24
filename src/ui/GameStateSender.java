/* Name: GameStateSender
 * Description: This interface sends the game state to a remote client.
 */

package ui;

import network.Session;

/**
 * The {@code GameStateSender} interface sends the game state to the remote
 * client of a session, without the user interface knowing how it is sent.
 */
public interface GameStateSender {

	/**
	 * Sends the game state to the remote client of the session.
	 *
	 * @param s			the session to send the game state to.
	 * @param gameState	the game state to send.
	 */
	public void send(Session s, String gameState);
}
