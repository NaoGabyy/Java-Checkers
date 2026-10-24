/* Name: CommandHandler
 * Description: This interface handles one command sent by a remote client.
 */

package network;

/**
 * The {@code CommandHandler} interface handles one type of command received
 * by the {@link CheckersNetworkHandler}. New commands can be supported by
 * registering a new handler with
 * {@link CheckersNetworkHandler#registerCommand(String, CommandHandler)}.
 */
public interface CommandHandler {

	/**
	 * Handles the command and creates the response to send to the remote
	 * client.
	 *
	 * @param request	the request received from the remote client.
	 * @return the response to send, starting with
	 * {@link CheckersNetworkHandler#RESPONSE_ACCEPTED} or
	 * {@link CheckersNetworkHandler#RESPONSE_DENIED}.
	 */
	public String handle(CommandRequest request);
}
