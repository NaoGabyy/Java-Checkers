/* Name: GameNetworkSync
 * Description: This class sends the game state to the connected network
 * players.
 */

package ui;

import java.util.ArrayList;
import java.util.List;

import model.Game;
import model.NetworkPlayer;
import model.Player;
import network.Command;
import network.Session;

/**
 * The {@code GameNetworkSync} class is responsible for keeping the remote
 * clients of network players synchronized with the local game state.
 */
public class GameNetworkSync {

	/** The window with the network sessions of each player. */
	private CheckersWindow window;

	public GameNetworkSync(CheckersWindow window) {
		this.window = window;
	}

	/**
	 * Sends the game state to the session of each network player.
	 *
	 * @param game		the game to send.
	 * @param player1	player 1 of the game.
	 * @param player2	player 2 of the game.
	 */
	public void updateNetwork(Game game, Player player1, Player player2) {

		// Get the relevant sessions to send to
		List<Session> sessions = new ArrayList<>();
		if (player1 instanceof NetworkPlayer) {
			sessions.add(window.getSession1());
		}
		if (player2 instanceof NetworkPlayer) {
			sessions.add(window.getSession2());
		}

		// Send the game update
		for (Session s : sessions) {
			sendGameState(game, s);
		}
	}

	/**
	 * Sends the game state to the client of the specified session.
	 *
	 * @param game	the game to send.
	 * @param s		the session to send the game state to.
	 */
	public void sendGameState(Game game, Session s) {

		if (s == null) {
			return;
		}

		// Create the command and send it
		Command update = new Command(Command.COMMAND_UPDATE,
				s.getSid(), game.getGameState());
		String host = s.getDestinationHost();
		int port = s.getDestinationPort();
		update.send(host, port);
	}

	public CheckersWindow getWindow() {
		return window;
	}

	public void setWindow(CheckersWindow window) {
		this.window = window;
	}
}
