/* Name: UpdateCommandHandler
 * Description: This class handles the update command.
 */

package network;

import static network.CheckersNetworkHandler.RESPONSE_ACCEPTED;
import static network.CheckersNetworkHandler.RESPONSE_DENIED;

import model.NetworkPlayer;
import ui.CheckerBoard;
import ui.CheckersWindow;

/**
 * The {@code UpdateCommandHandler} class handles the update command from a
 * connected client. The update commands is used by the other connected client
 * to update the game state after a move was made. If both players on this
 * client are network players, then the state if forwarded to the other player
 * (effectively making this client a router).
 */
public class UpdateCommandHandler implements CommandHandler {

	/** The flag indicating if the command is for player 1 or not. */
	private boolean isPlayer1;

	/** The checkers window. */
	private CheckersWindow window;

	/** The checker board from the checkers window. */
	private CheckerBoard board;

	public UpdateCommandHandler(boolean isPlayer1, CheckersWindow window,
			CheckerBoard board) {
		this.isPlayer1 = isPlayer1;
		this.window = window;
		this.board = board;
	}

	@Override
	public String handle(CommandRequest request) {

		// Only a connected client can update the board
		String newState = (request.isMatch()? request.getData() : "");

		// New state is invalid
		if (newState.isEmpty()) {
			return RESPONSE_DENIED;
		}

		// Update the current client's game state
		this.board.setGameState(false, newState, null);
		if (!board.getCurrentPlayer().isHuman()) {
			board.update();
		}

		// Check if both players are network players
		// If so, forward the game state (i.e. this client acts as a router)
		if (isPlayer1 &&
				board.getPlayer2() instanceof NetworkPlayer) {
			board.sendGameState(window.getSession2());
		} else if (!isPlayer1 &&
				board.getPlayer1() instanceof NetworkPlayer) {
			board.sendGameState(window.getSession1());
		}

		return RESPONSE_ACCEPTED;
	}
}
