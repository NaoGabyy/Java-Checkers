/* Name: HumanPlayer
 * Author: Devon McGrath
 * Description: This class represents a human player (i.e. a user) that can
 * interact with the system.
 */

package model;

/**
 * The {@code HumanPlayer} class represents a user of the checkers game that
 * can update the game by clicking on tiles on the board.
 */
public class HumanPlayer extends Player {

	@Override
	public boolean isHuman() {
		return true;
	}

}
