/* Name: CommandRequest
 * Description: This class contains the information of a command received from
 * a remote client.
 */

package network;

import java.net.Socket;

/**
 * The {@code CommandRequest} class contains the information a
 * {@link CommandHandler} needs to handle a command.
 */
public class CommandRequest {

	/** The value on the second line (the session ID or the port). */
	private final String sid;

	/** The value on the third line, or an empty string if there is none. */
	private final String data;

	/** The flag indicating if the session ID matches the session. */
	private final boolean match;

	/** The socket the remote client used to connect. */
	private final Socket socket;

	public CommandRequest(String sid, String data, boolean match,
			Socket socket) {
		this.sid = sid;
		this.data = data;
		this.match = match;
		this.socket = socket;
	}

	public String getSid() {
		return sid;
	}

	public String getData() {
		return data;
	}

	public boolean isMatch() {
		return match;
	}

	public Socket getSocket() {
		return socket;
	}
}
