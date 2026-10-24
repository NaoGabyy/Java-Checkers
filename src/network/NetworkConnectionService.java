/* Name: NetworkConnectionService
 * Description: This class listens, connects and disconnects the network
 * sessions of this client.
 */

package network;

/**
 * The {@code NetworkConnectionService} class is responsible for the network
 * protocol used to listen for, connect to and disconnect from remote checkers
 * clients. It does not interact with the user interface.
 */
public class NetworkConnectionService {

	/** The minimum port that can be used. */
	public static final int MIN_PORT = 1025;

	/** The maximum port that can be used. */
	public static final int MAX_PORT = 65535;

	/**
	 * Checks if a port is in the range of ports that can be used.
	 *
	 * @param port	the port to check.
	 * @return true if the port is between {@value #MIN_PORT} and
	 * {@value #MAX_PORT}.
	 */
	public boolean isValidPort(int port) {
		return port >= MIN_PORT && port <= MAX_PORT;
	}

	/**
	 * Checks if this client can listen on a port.
	 *
	 * @param port	the port to check.
	 * @return true if the port is available.
	 */
	public boolean isPortAvailable(int port) {
		return ConnectionListener.available(port);
	}

	/**
	 * Starts listening on the specified port for the session, stopping the
	 * previous listener if it was on a different port.
	 *
	 * @param s		the session to listen for.
	 * @param port	the port to listen on.
	 */
	public void listen(Session s, int port) {

		// Update the server if necessary
		if (s.getListener().getPort() != port) {
			s.getListener().stopListening();
		}
		s.getListener().setPort(port);
		s.getListener().listen();
	}

	/**
	 * Tries to start a session with a remote client. If the remote client
	 * accepts, the session is updated and the game state of the remote client
	 * is requested.
	 *
	 * @param s			the session to connect.
	 * @param srcPort	the port this client is listening on.
	 * @param destHost	the remote host.
	 * @param destPort	the remote port.
	 * @param isPlayer1	the flag indicating if the session is for player 1.
	 * @return the result of the connection attempt.
	 */
	public ConnectResult connect(Session s, int srcPort, String destHost,
			int destPort, boolean isPlayer1) {

		// Connect to the proposed host
		Command connect = new Command(Command.COMMAND_CONNECT,
				srcPort + "", isPlayer1? "1" : "0");
		String response = connect.send(destHost, destPort);

		// No response
		if (response.isEmpty()) {
			return new ConnectResult(ConnectResult.Status.NO_RESPONSE, "", "");
		}

		// It was a valid client, but refused to connect
		else if (response.startsWith(CheckersNetworkHandler.RESPONSE_DENIED)) {
			String[] lines = response.split("\n");
			String errMsg = lines.length > 1? lines[1] : "";
			return new ConnectResult(ConnectResult.Status.DENIED, errMsg, "");
		}

		// The connection was accepted by the checkers client
		else if (response.startsWith(CheckersNetworkHandler.RESPONSE_ACCEPTED)){

			// Update the session
			s.setDestinationHost(destHost);
			s.setDestinationPort(destPort);

			// Update the SID
			String[] lines = response.split("\n");
			String sid = lines.length > 1? lines[1] : "";
			s.setSid(sid);

			// Get the new game state
			Command get = new Command(Command.COMMAND_GET, sid, null);
			response = get.send(destHost, destPort);
			lines = response.split("\n");
			String state = lines.length > 1? lines[1] : "";
			return new ConnectResult(ConnectResult.Status.ACCEPTED, "", state);
		}

		// General error, maybe the user tried a web server and
		// the response is an HTTP response
		else {
			return new ConnectResult(
					ConnectResult.Status.NOT_CHECKERS_CLIENT, "", "");
		}
	}

	/**
	 * Checks if the session is connected to a remote client.
	 *
	 * @param s	the session to check.
	 * @return true if the session has a session ID.
	 */
	public boolean isConnected(Session s) {
		String sid = s.getSid();
		return sid != null && !sid.isEmpty();
	}

	/**
	 * Disconnects the session from its remote client.
	 *
	 * @param s	the session to disconnect.
	 */
	public void disconnect(Session s) {

		// Send the request
		Command disconnect = new Command(
				Command.COMMAND_DISCONNECT, s.getSid());
		disconnect.send(
				s.getDestinationHost(), s.getDestinationPort());

		// Update the session
		s.setSid(null);
	}

	/**
	 * The {@code ConnectResult} class represents the result of an attempt to
	 * connect to a remote client.
	 */
	public static class ConnectResult {

		/** The possible outcomes of a connection attempt. */
		public enum Status {
			NO_RESPONSE, DENIED, ACCEPTED, NOT_CHECKERS_CLIENT
		}

		/** The outcome of the connection attempt. */
		private final Status status;

		/** The error message sent by the remote client if it was denied. */
		private final String errorMessage;

		/** The game state of the remote client if it was accepted. */
		private final String gameState;

		public ConnectResult(Status status, String errorMessage,
				String gameState) {
			this.status = status;
			this.errorMessage = errorMessage;
			this.gameState = gameState;
		}

		public Status getStatus() {
			return status;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public String getGameState() {
			return gameState;
		}
	}
}
