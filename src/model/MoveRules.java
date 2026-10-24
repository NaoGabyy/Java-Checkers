/* Name: MoveRules
 * Description: This interface provides the rules used by the computer player
 * to find and evaluate moves.
 */

package model;

import java.awt.Point;
import java.util.List;

/**
 * The {@code MoveRules} interface provides the checkers rules that a
 * {@link ComputerPlayer} needs to decide on a move.
 */
public interface MoveRules {

	/**
	 * Gets the end points of the regular moves (not skips) of a checker.
	 *
	 * @param board			the board to check against.
	 * @param startIndex	the index of the checker.
	 * @return the end points of the moves.
	 */
	public List<Point> getMoves(Board board, int startIndex);

	/**
	 * Gets the end points of the skips of a checker.
	 *
	 * @param board			the board to check against.
	 * @param startIndex	the index of the checker.
	 * @return the end points of the skips.
	 */
	public List<Point> getSkips(Board board, int startIndex);

	/**
	 * Checks if a checker cannot be skipped by the opponent.
	 *
	 * @param board		the board to check against.
	 * @param checker	the point of the checker.
	 * @return true if the checker is safe.
	 */
	public boolean isSafe(Board board, Point checker);
}
