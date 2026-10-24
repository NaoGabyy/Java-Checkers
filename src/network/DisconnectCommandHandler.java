/* Name: DisconnectCommandHandler
 * Description: This class handles the disconnect command.
 */

package network;

import static network.CheckersNetworkHandler.RESPONSE_ACCEPTED;
import static network.CheckersNetworkHandler.RESPONSE_DENIED;

import ui.CheckersWindow;
import ui.OptionPanel;

/**
 * The {@code DisconnectCommandHandler} class handles a request from a
 * connected client to disconnect.
 */
public class DisconnectCommandHandler implements CommandHandler {

	/** The flag indicating if the command is for player 1 or not. */
	private boolean isPlayer1;

	/** The checkers window. */
	private CheckersWindow window;

	/** The option panel in the checkers window. */
	private OptionPanel opts;

	public DisconnectCommandHandler(boolean isPlayer1, CheckersWindow window,
			OptionPanel opts) {
		this.isPlayer1 = isPlayer1;
		this.window = window;
		this.opts = opts;
	}

	@Override
	public String handle(CommandRequest request) {

		// Disconnect if SID match
		if (request.isMatch()) {
			if (isPlayer1) {
				window.getSession1().setSid(null);
				this.opts.getNetworkWindow1().setCanUpdateConnect(true);
			} else {
				window.getSession2().setSid(null);
				this.opts.getNetworkWindow2().setCanUpdateConnect(true);
			}
			return RESPONSE_ACCEPTED + "\nClient has been disconnected.";
		} else {
			return RESPONSE_DENIED + "\nError: cannot disconnect if not connected.";
		}
	}
}
