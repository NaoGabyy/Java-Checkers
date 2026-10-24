/* Name: ConnectCommandHandler
 * Description: This class handles the connect command.
 */

package network;

import static network.CheckersNetworkHandler.RESPONSE_ACCEPTED;
import static network.CheckersNetworkHandler.RESPONSE_DENIED;

import java.net.Socket;

import ui.CheckersWindow;
import ui.NetworkWindow;
import ui.OptionPanel;

/**
 * The {@code ConnectCommandHandler} class handles a request from a remote
 * client to connect to this one. A connection request can be satisfied for
 * the player if there is no connected client and the client is the correct
 * player (e.g. player 1 can't connect to player 1 on this client as the game
 * would not be able to be played).
 */
public class ConnectCommandHandler implements CommandHandler {

	/** The minimum number of characters in the session ID. */
	private static final int MIN_SID_LENGTH = 16;

	/** The max number of characters in the session ID. */
	private static final int MAX_SID_LENGTH = 64;

	/** The flag indicating if the command is for player 1 or not. */
	private boolean isPlayer1;

	/** The checkers window. */
	private CheckersWindow window;

	/** The option panel in the checkers window. */
	private OptionPanel opts;

	public ConnectCommandHandler(boolean isPlayer1, CheckersWindow window,
			OptionPanel opts) {
		this.isPlayer1 = isPlayer1;
		this.window = window;
		this.opts = opts;
	}

	@Override
	public String handle(CommandRequest request) {

		// Get the port that was passed (in the SID field)
		int port = -1;
		try {
			port = Integer.parseInt(request.getSid());
		} catch (NumberFormatException err) {}

		// Determine if the client attempting to connect is player 1
		String isP1 = request.getData();
		boolean remotePlayer1 = isP1.startsWith("1");

		// Handle the connect request
		return handleConnect(request.getSocket(), port, remotePlayer1);
	}

	/**
	 * Checks if the client connect request can be satisfied.
	 *
	 * @param s				the socket that the remote client used to connect.
	 * @param port			the port that the remote client sent in the request.
	 * @param remotePlayer1	the flag indicating if the remote player is player 1.
	 * @return the resulting response to send to the remote client.
	 */
	private String handleConnect(Socket s, int port, boolean remotePlayer1) {

		// Check if there is someone already connected
		Session s1 = window.getSession1(), s2 = window.getSession2();
		String sid1 = s1.getSid();
		String sid2 = s2.getSid();
		if ((isPlayer1 && sid1 != null && !sid1.isEmpty()) ||
				(!isPlayer1 && sid2 != null && !sid2.isEmpty())) {
			return RESPONSE_DENIED + "\nError: user already connected.";
		}

		// Check that it is a valid connection
		if (!(isPlayer1 ^ remotePlayer1)) {
			return RESPONSE_DENIED + "\nError: the other client is already "
					+ "player " + (remotePlayer1? "1." : "2.");
		}
		String host = s.getInetAddress().getHostAddress();
		if (host.equals("127.0.0.1")) {
			if ((isPlayer1 && port == s2.getSourcePort()) ||
					(!isPlayer1 && port == s1.getSourcePort())) {
				return RESPONSE_DENIED + "\nError: the client cannot connect "
						+ "to itself.";
			}
		}

		// Update the connection
		String sid = generateSessionID();
		Session session = isPlayer1? s1 : s2;
		NetworkWindow win = (isPlayer1?
				opts.getNetworkWindow1() : opts.getNetworkWindow2());
		session.setSid(sid);
		session.setDestinationHost(host);
		session.setDestinationPort(port);

		// Update the UI
		win.setDestinationHost(host);
		win.setDestinationPort(port);
		win.setCanUpdateConnect(false);
		win.setMessage("  Connected to " + host + ":" + port + ".");

		return RESPONSE_ACCEPTED + "\n" + sid + "\nSuccessfully connected.";
	}

	/**
	 * Generates a session ID of a random length with random characters. The
	 * session ID itself is guaranteed to be at least {@value #MIN_SID_LENGTH}
	 * characters long, but not longer than {@value #MAX_SID_LENGTH}.
	 *
	 * @return a randomly generated SID.
	 */
	private static String generateSessionID() {

		// Generate a string of random length
		String sid = "";
		int chars = (int) ((MAX_SID_LENGTH - MIN_SID_LENGTH) * Math.random())
				+ MIN_SID_LENGTH;
		for (int i = 0; i < chars; i ++) {

			// Generate a character in a random range
			int t = (int) (4 * Math.random());
			int min = 32, max = 48;
			if (t == 1) {
				min = 48;
				max = 65;
			} else if (t == 2) {
				min = 65;
				max = 97;
			} else if (t == 3) {
				min = 97;
				max = 125;
			}
			char randChar = (char) ((Math.random() * (max - min)) + min);
			sid += randChar;
		}

		return sid;
	}
}
