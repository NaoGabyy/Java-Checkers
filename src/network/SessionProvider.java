/* Name: SessionProvider
 * Description: This interface provides the network sessions of each player.
 */

package network;

/**
 * The {@code SessionProvider} interface provides access to the network
 * sessions of player 1 and player 2.
 */
public interface SessionProvider {

	/**
	 * Gets the network session of player 1.
	 *
	 * @return the session of player 1.
	 */
	public Session getSession1();

	/**
	 * Gets the network session of player 2.
	 *
	 * @return the session of player 2.
	 */
	public Session getSession2();
}
