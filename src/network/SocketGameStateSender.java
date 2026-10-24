/* Name: SocketGameStateSender
 * Description: This class sends the game state to a remote client through a
 * socket.
 */

package network;

import ui.GameStateSender;

/**
 * The {@code SocketGameStateSender} class sends the game state with an
 * update {@link Command}, which is sent through a socket.
 */
public class SocketGameStateSender implements GameStateSender {

	@Override
	public void send(Session s, String gameState) {

		// Create the command and send it
		Command update = new Command(Command.COMMAND_UPDATE,
				s.getSid(), gameState);
		String host = s.getDestinationHost();
		int port = s.getDestinationPort();
		update.send(host, port);
	}
}
