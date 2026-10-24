/* Name: AppFactory
 * Description: This class creates and connects the objects of the checkers
 * application.
 */

package ui;

import network.CheckersNetworkHandler;
import network.ConnectionListener;
import network.Session;
import network.SocketGameStateSender;

/**
 * The {@code AppFactory} class is the composition root of the application.
 * It is the only place that decides which concrete classes are used and how
 * they are connected.
 */
public class AppFactory {

	/**
	 * Creates a checkers window with all of its dependencies.
	 *
	 * @return a new checkers window ready to be displayed.
	 */
	public static CheckersWindow createWindow() {

		// Create the components
		CheckerBoard board = new CheckerBoard(null, new SocketGameStateSender());
		OptionPanel opts = new OptionPanel(null);
		ConnectionListener listener1 = new ConnectionListener(0);
		ConnectionListener listener2 = new ConnectionListener(0);
		Session session1 = new Session(listener1, null, null, -1);
		Session session2 = new Session(listener2, null, null, -1);
		CheckersWindow window = new CheckersWindow(
				board, opts, session1, session2);

		// Connect the components that need the window
		board.setWindow(window);
		opts.setWindow(window);

		// Setup the network listeners
		listener1.setConnectionHandler(
				new CheckersNetworkHandler(true, window, board, opts));
		listener2.setConnectionHandler(
				new CheckersNetworkHandler(false, window, board, opts));

		return window;
	}
}
