/* Name: NetworkRequest
 * Description: This class represents a request received from a remote
 * checkers client.
 */

package network;

import java.net.Socket;

/**
 * The {@code NetworkRequest} class interprets the message sent by a remote
 * checkers client. The first line holds the command, the second line the
 * session ID and the third line the data of the command.
 */
public class NetworkRequest {

	/** The command requested, in upper case. */
	private final String command;

	/** The session ID (or other value) on the second line. */
	private final String sid;

	/** The data on the third line. */
	private final String data;

	public NetworkRequest(String command, String sid, String data) {
		this.command = command;
		this.sid = sid;
		this.data = data;
	}

	/**
	 * Reads and interprets the request sent through the socket.
	 *
	 * @param socket	the socket the remote client is connected with.
	 * @return the request that was read.
	 */
	public static NetworkRequest read(Socket socket) {
		return parse(ConnectionListener.read(socket));
	}

	/**
	 * Interprets a request message. Missing lines are treated as empty.
	 *
	 * @param message	the message sent by the remote client.
	 * @return the request in the message.
	 */
	public static NetworkRequest parse(String message) {
		String[] lines = message.replace("\r\n", "\n").split("\n");
		String command = lines[0].split(" ")[0].toUpperCase();
		String sid = lines.length > 1? lines[1] : "";
		String data = lines.length > 2? lines[2] : "";
		return new NetworkRequest(command, sid, data);
	}

	public String getCommand() {
		return command;
	}

	public String getSid() {
		return sid;
	}

	public String getData() {
		return data;
	}
}
