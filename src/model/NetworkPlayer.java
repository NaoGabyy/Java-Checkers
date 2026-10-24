/* Name: NetworkPlayer
 * Author: Devon McGrath
 * Description: This class represents a network player, who may be on a
 * different host.
 */

package model;

/**
 * The {@code NetworkPlayer} class is a dummy player used so that the game
 * can be updated properly from the corresponding client.
 */
public class NetworkPlayer extends Player {

	@Override
	public boolean isHuman() {
		return false;
	}

	/**
	 * Network players are always remote, as the game state is updated when
	 * their client sends the updated game.
	 */
	@Override
	public boolean isRemote() {
		return true;
	}

}
