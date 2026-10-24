/* Name: PlayerRegistry
 * Description: This class maps player type names to factories that create
 * the corresponding players.
 */

package model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The {@code PlayerRegistry} class keeps the available types of players. New
 * types of players can be added with {@link #register(String, Supplier)}
 * without modifying the classes that create players.
 */
public class PlayerRegistry {

	/** The name of the type of player used when a name is not registered. */
	public static final String DEFAULT_TYPE = "Human";

	/** The factories for each type of player, in registration order. */
	private final Map<String, Supplier<Player>> factories =
			new LinkedHashMap<>();

	/**
	 * Creates a registry with the default types of players: human, computer
	 * and network.
	 *
	 * @return a new registry with the default types of players.
	 */
	public static PlayerRegistry createDefault() {
		PlayerRegistry registry = new PlayerRegistry();
		registry.register(DEFAULT_TYPE, HumanPlayer::new);
		registry.register("Computer", ComputerPlayer::new);
		registry.register("Network", NetworkPlayer::new);
		return registry;
	}

	/**
	 * Registers a new type of player. If the name is already registered, its
	 * factory is replaced.
	 *
	 * @param name		the name of the type of player.
	 * @param factory	creates a new instance of the type of player.
	 */
	public void register(String name, Supplier<Player> factory) {
		factories.put(name, factory);
	}

	/**
	 * Gets the names of the registered types of players.
	 *
	 * @return the names in registration order.
	 */
	public String[] getNames() {
		return factories.keySet().toArray(new String[0]);
	}

	/**
	 * Creates a new instance of the type of player with the specified name.
	 *
	 * @param name	the name of the type of player.
	 * @return a new player of the specified type, or a {@link HumanPlayer} if
	 * the name is not registered.
	 */
	public Player create(String name) {
		Supplier<Player> factory = factories.get(name);
		if (factory == null) {
			return new HumanPlayer();
		}
		return factory.get();
	}
}
