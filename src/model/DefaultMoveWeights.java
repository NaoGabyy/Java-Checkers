/* Name: DefaultMoveWeights
 * Description: This class provides the default weights of the computer
 * player.
 */

package model;

/**
 * The {@code DefaultMoveWeights} class provides the original weights of the
 * {@link ComputerPlayer}.
 */
public class DefaultMoveWeights implements MoveWeights {

	/** The weight of being able to skip. */
	private static final double WEIGHT_SKIP = 25;

	/** The weight of being able to skip on next turn. */
	private static final double SKIP_ON_NEXT = 20;

	/** The weight associated with being safe then safe before and after. */
	private static final double SAFE_SAFE = 5;

	/** The weight associated with being safe then unsafe before and after. */
	private static final double SAFE_UNSAFE = -40;

	/** The weight associated with being unsafe then safe before and after. */
	private static final double UNSAFE_SAFE = 40;

	/** The weight associated with being unsafe then unsafe before and after. */
	private static final double UNSAFE_UNSAFE = -40;

	/** The weight of a checker being safe. */
	private static final double SAFE = 3;

	/** The weight of a checker being unsafe. */
	private static final double UNSAFE = -5;

	/** The factor used to multiply some weights when the checker being
	 * observed is a king. */
	private static final double KING_FACTOR = 2;

	@Override
	public double getSkip() {
		return WEIGHT_SKIP;
	}

	@Override
	public double getSkipOnNext() {
		return SKIP_ON_NEXT;
	}

	@Override
	public double getSafeSafe() {
		return SAFE_SAFE;
	}

	@Override
	public double getSafeUnsafe() {
		return SAFE_UNSAFE;
	}

	@Override
	public double getUnsafeSafe() {
		return UNSAFE_SAFE;
	}

	@Override
	public double getUnsafeUnsafe() {
		return UNSAFE_UNSAFE;
	}

	@Override
	public double getSafe() {
		return SAFE;
	}

	@Override
	public double getUnsafe() {
		return UNSAFE;
	}

	@Override
	public double getKingFactor() {
		return KING_FACTOR;
	}
}
