/* Name: CheckersNetworkHandler
 * Author: Devon McGrath
 * Description: This class handles connections between two clients. It receives
 * connections and sends responses.
 */

package network;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import ui.CheckerBoard;
import ui.CheckersWindow;
import ui.OptionPanel;

/**
 * The {@code CheckersNetworkHandler} class handles incoming connections from
 * remote checkers clients. It decides whether connections should be accepted
 * and perform an action on this client. It sends two responses: accepted and
 * denied. Each response starts with the corresponding string
 * {@link #RESPONSE_ACCEPTED} or {@link #RESPONSE_DENIED}. Each command is
 * handled by the {@link CommandHandler} registered for it.
 */
public class CheckersNetworkHandler implements ActionListener {

	/** The start of a response that was accepted. */
	public static final String RESPONSE_ACCEPTED = "ACCEPTED";

	/** The start of a response that was denied. */
	public static final String RESPONSE_DENIED = "DENIED";

	/** The flag indicating if this handler is handling a connection to player
	 * 1 or not. */
	private boolean isPlayer1;

	/** The checkers window. */
	private CheckersWindow window;

	/** The checker board from the checkers window. */
	private CheckerBoard board;

	/** The option panel in the checkers window. */
	private OptionPanel opts;

	/** The handlers for each command, by command name. */
	private Map<String, CommandHandler> commands;

	public CheckersNetworkHandler(boolean isPlayer1, CheckersWindow window,
			CheckerBoard board, OptionPanel opts) {
		this.isPlayer1 = isPlayer1;
		this.window = window;
		this.board = board;
		this.opts = opts;
		this.commands = new HashMap<>();

		// Register the standard commands
		registerCommand(Command.COMMAND_UPDATE,
				new UpdateCommandHandler(isPlayer1, window, board));
		registerCommand(Command.COMMAND_CONNECT,
				new ConnectCommandHandler(isPlayer1, window, opts));
		registerCommand(Command.COMMAND_GET,
				new GetStateCommandHandler(board));
		registerCommand(Command.COMMAND_DISCONNECT,
				new DisconnectCommandHandler(isPlayer1, window, opts));
	}

	/**
	 * Registers the handler for a command. If the command already has a
	 * handler, it is replaced.
	 *
	 * @param command	the name of the command (case insensitive).
	 * @param handler	the handler for the command.
	 */
	public void registerCommand(String command, CommandHandler handler) {
		commands.put(command.toUpperCase(), handler);
	}

	/**
	 * Handles a new connection from the {@link ConnectionListener}.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// Invalid event
		if (e == null || !(e.getSource() instanceof ConnectionHandler)) {
			return;
		}

		// Get the data from the connection
		ConnectionHandler handler = (ConnectionHandler) e.getSource();
		String data = ConnectionListener.read(handler.getSocket());
		data = data.replace("\r\n", "\n");

		// Unable to handle
		if (window == null || board == null || opts == null) {
			sendResponse(handler, "Client error: invalid network handler.");
			return;
		}

		Session s1 = window.getSession1(), s2 = window.getSession2();

		// Determine if a valid user
		String[] lines = data.split("\n");
		String cmd = lines[0].split(" ")[0].toUpperCase();
		String sid = lines.length > 1? lines[1] : "";
		String response = "";
		boolean match = false;
		if (isPlayer1) {
			match = sid.equals(s1.getSid());
		} else {
			match = sid.equals(s2.getSid());
		}

		// Handle the command with its registered handler
		CommandHandler command = commands.get(cmd);
		if (command != null) {
			String extra = lines.length > 2? lines[2] : "";
			response = command.handle(new CommandRequest(
					sid, extra, match, handler.getSocket()));
		}

		// Invalid command
		else {
			response = RESPONSE_DENIED + "\nJava Checkers - unknown "
					+ "command '" + cmd + "'";
		}

		// Send the response to whoever connected
		sendResponse(handler, response);
	}

	/**
	 * Sends a response to the connection handler's connection, if it is not
	 * closed.
	 *
	 * @param handler	the connection handler to send the response to.
	 * @param response	the response data to send.
	 */
	private static void sendResponse(ConnectionHandler handler,
			String response) {

		// Trivial cases
		if (handler == null) {
			return;
		}
		Socket s = handler.getSocket();
		if (s == null || s.isClosed()) {
			return;
		}
		if (response == null) {
			response = "";
		}

		// Write the response and close the connection
		try (OutputStream os = s.getOutputStream()) {
			os.write(response.getBytes());
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			// Close the socket
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
