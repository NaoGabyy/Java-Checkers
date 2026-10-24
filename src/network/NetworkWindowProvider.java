/* Name: NetworkWindowProvider
 * Description: This interface provides the network windows of each player.
 */

package network;

import ui.NetworkWindow;

/**
 * The {@code NetworkWindowProvider} interface provides access to the windows
 * used to configure the network options of player 1 and player 2.
 */
public interface NetworkWindowProvider {

	/**
	 * Gets the network window of player 1.
	 *
	 * @return the network window of player 1.
	 */
	public NetworkWindow getNetworkWindow1();

	/**
	 * Gets the network window of player 2.
	 *
	 * @return the network window of player 2.
	 */
	public NetworkWindow getNetworkWindow2();
}
