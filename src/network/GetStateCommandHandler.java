/* Name: GetStateCommandHandler
 * Description: This class handles the get state command.
 */

package network;

import static network.CheckersNetworkHandler.RESPONSE_ACCEPTED;
import static network.CheckersNetworkHandler.RESPONSE_DENIED;

import ui.CheckerBoard;

/**
 * The {@code GetStateCommandHandler} class handles a request from a connected
 * client for the current game state.
 */
public class GetStateCommandHandler implements CommandHandler {

	/** The checker board from the checkers window. */
	private CheckerBoard board;

	public GetStateCommandHandler(CheckerBoard board) {
		this.board = board;
	}

	@Override
	public String handle(CommandRequest request) {

		// Send the board if there was a SID match
		if (request.isMatch()) {
			return RESPONSE_ACCEPTED + "\n"
					+ board.getGame().getGameState();
		} else {
			return RESPONSE_DENIED;
		}
	}
}
