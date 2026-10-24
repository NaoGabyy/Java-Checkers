/* Name: StandardCheckersRules
 * Description: This class provides the standard checkers rules to the
 * computer player.
 */

package logic;

import java.awt.Point;
import java.util.List;

import model.Board;
import model.MoveRules;

/**
 * The {@code StandardCheckersRules} class implements {@link MoveRules} with
 * the standard checkers rules from {@link MoveGenerator} and
 * {@link MoveLogic}.
 */
public class StandardCheckersRules implements MoveRules {

	@Override
	public List<Point> getMoves(Board board, int startIndex) {
		return MoveGenerator.getMoves(board, startIndex);
	}

	@Override
	public List<Point> getSkips(Board board, int startIndex) {
		return MoveGenerator.getSkips(board, startIndex);
	}

	@Override
	public boolean isSafe(Board board, Point checker) {
		return MoveLogic.isSafe(board, checker);
	}
}
