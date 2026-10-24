/* Name: MoveWeights
 * Description: This interface provides the weights used by the computer
 * player to evaluate moves.
 */

package model;

/**
 * The {@code MoveWeights} interface provides the weights a
 * {@link ComputerPlayer} uses to evaluate moves. A different strategy or
 * difficulty can be made with a new implementation, without modifying the
 * computer player.
 */
public interface MoveWeights {

	/**
	 * @return the weight of being able to skip.
	 */
	public double getSkip();

	/**
	 * @return the weight of being able to skip on next turn.
	 */
	public double getSkipOnNext();

	/**
	 * @return the weight associated with being safe then safe before and
	 * after.
	 */
	public double getSafeSafe();

	/**
	 * @return the weight associated with being safe then unsafe before and
	 * after.
	 */
	public double getSafeUnsafe();

	/**
	 * @return the weight associated with being unsafe then safe before and
	 * after.
	 */
	public double getUnsafeSafe();

	/**
	 * @return the weight associated with being unsafe then unsafe before and
	 * after.
	 */
	public double getUnsafeUnsafe();

	/**
	 * @return the weight of a checker being safe.
	 */
	public double getSafe();

	/**
	 * @return the weight of a checker being unsafe.
	 */
	public double getUnsafe();

	/**
	 * @return the factor used to multiply some weights when the checker being
	 * observed is a king.
	 */
	public double getKingFactor();
}
