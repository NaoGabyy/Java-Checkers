/* Name: Player
 * Author: Devon McGrath
 * Description: This class represents a player of the system.
 */

package model;

/**
 * The {@code Player} class is an abstract class that represents a player in a
 * game of checkers.
 */
public abstract class Player {

	/**
	 * Determines how the game is updated. If true, the user must interact with
	 * the user interface to make a move. Otherwise, the game is updated by the
	 * player itself (see {@link AutomatedPlayer}) or by a remote client.
	 *
	 * @return true if this player represents a user.
	 */
	public abstract boolean isHuman();

	/**
	 * Determines if this player makes its moves on another client. If true,
	 * the game state must be sent to that client through its network session.
	 *
	 * @return true if this player is on a remote client.
	 */
	public boolean isRemote() {
		return false;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[isHuman=" + isHuman() + "]";
	}
}
