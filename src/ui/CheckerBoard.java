/* Name: CheckerBoard
 * Author: Devon McGrath
 * Description: This class is the graphical user interface representation of
 * a checkers game. It is responsible for drawing the checker board and
 * allowing moves to be made. It does not provide a method to allow the user to
 * change settings of the game or restart it.
 */

package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.Timer;

import logic.MoveGenerator;
import model.Board;
import model.Game;
import model.HumanPlayer;
import model.NetworkPlayer;
import model.Player;
import network.Session;

/**
 * The {@code CheckerBoard} class is a graphical user interface component that
 * is capable of drawing any checkers game state. It also handles player turns.
 * For human players, this means interacting with and selecting tiles on the
 * checker board. For non-human players, this means using the logic implemented
 * by the specified player object itself is used.
 */
public class CheckerBoard extends JButton {

	private static final long serialVersionUID = -6014690893709316364L;
	
	/** The amount of milliseconds before a computer player takes a move. */
	private static final int TIMER_DELAY = 1000;

	/** The game of checkers that is being played on this component. */
	private Game game;

	/** Draws the game state on this component. */
	private BoardRenderer renderer;

	/** Sends the game state to the network players. */
	private GameNetworkSync networkSync;
	
	/** The player in control of the black checkers. */
	private Player player1;
	
	/** The player in control of the white checkers. */
	private Player player2;
	
	/** The last point that the current player selected on the checker board. */
	private Point selected;
	
	/** The flag to determine the colour of the selected tile. If the selection
	 * is valid, a green colour is used to highlight the tile. Otherwise, a red
	 * colour is used. */
	private boolean selectionValid;

	/** A convenience flag to check if the game is over. */
	private boolean isGameOver;
	
	/** The timer to control how fast a computer player makes a move. */
	private Timer timer;
	
	public CheckerBoard(CheckersWindow window) {
		this(window, new Game(), null, null);
	}
	
	public CheckerBoard(CheckersWindow window, Game game,
			Player player1, Player player2) {
		
		// Setup the component
		super.setBorderPainted(false);
		super.setFocusPainted(false);
		super.setContentAreaFilled(false);
		super.setBackground(Color.LIGHT_GRAY);
		this.addActionListener(new ClickListener());
		
		// Setup the game
		this.game = (game == null)? new Game() : game;
		this.renderer = new BoardRenderer();
		this.networkSync = new GameNetworkSync(window);
		setPlayer1(player1);
		setPlayer2(player2);
	}
	
	/**
	 * Checks if the game is over and redraws the component graphics.
	 */
	public void update() {
		runPlayer();
		this.isGameOver = game.isGameOver();
		repaint();
	}
	
	private void runPlayer() {
		
		// Nothing to do
		Player player = getCurrentPlayer();
		if (player == null || player.isHuman() ||
				player instanceof NetworkPlayer) {
			return;
		}
		
		// Set a timer to run
		this.timer = new Timer(TIMER_DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getCurrentPlayer().updateGame(game);
				timer.stop();
				updateNetwork();
				update();
			}
		});
		this.timer.start();
	}
	
	public void updateNetwork() {
		networkSync.updateNetwork(game, player1, player2);
	}
	
	public synchronized boolean setGameState(boolean testValue,
			String newState, String expected) {
		
		// Test the value if requested
		if (testValue && !game.getGameState().equals(expected)) {
			return false;
		}
		
		// Update the game state
		this.game.setGameState(newState);
		repaint();
		
		return true;
	}
	
	public void sendGameState(Session s) {
		networkSync.sendGameState(game, s);
	}
	
	/**
	 * Draws the current checkers game state.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		renderer.render(g, game.copy(), getWidth(), getHeight(),
				selected, selectionValid, isGameOver);
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = (game == null)? new Game() : game;
	}

	public CheckersWindow getWindow() {
		return networkSync.getWindow();
	}

	public void setWindow(CheckersWindow window) {
		networkSync.setWindow(window);
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = (player1 == null)? new HumanPlayer() : player1;
		if (game.isP1Turn() && !this.player1.isHuman()) {
			this.selected = null;
		}
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = (player2 == null)? new HumanPlayer() : player2;
		if (!game.isP1Turn() && !this.player2.isHuman()) {
			this.selected = null;
		}
	}
	
	public Player getCurrentPlayer() {
		return game.isP1Turn()? player1 : player2;
	}

	public Color getLightTile() {
		return renderer.getLightTile();
	}

	public void setLightTile(Color lightTile) {
		renderer.setLightTile(lightTile);
	}

	public Color getDarkTile() {
		return renderer.getDarkTile();
	}

	public void setDarkTile(Color darkTile) {
		renderer.setDarkTile(darkTile);
	}

	/**
	 * Handles a click on this component at the specified point. If the current
	 * player is not human, this method does nothing. Otherwise, the selected
	 * point is updated and a move is attempted if the last click and this one
	 * both are on black tiles.
	 * 
	 * @param x	the x-coordinate of the click on this component.
	 * @param y	the y-coordinate of the click on this component.
	 */
	private void handleClick(int x, int y) {
		
		// The game is over or the current player isn't human
		if (isGameOver || !getCurrentPlayer().isHuman()) {
			return;
		}
		
		Game copy = game.copy();
		
		// Determine what square (if any) was selected
		final int W = getWidth(), H = getHeight();
		final int DIM = W < H? W : H, BOX_SIZE = (DIM - 2 * BoardRenderer.PADDING) / 8;
		final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
		final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
		x = (x - OFFSET_X) / BOX_SIZE;
		y = (y - OFFSET_Y) / BOX_SIZE;
		Point sel = new Point(x, y);
		
		// Determine if a move should be attempted
		if (Board.isValidPoint(sel) && Board.isValidPoint(selected)) {
			boolean change = copy.isP1Turn();
			String expected = copy.getGameState();
			boolean move = copy.move(selected, sel);
			boolean updated = (move?
					setGameState(true, copy.getGameState(), expected) : false);
			if (updated) {
				updateNetwork();
			}
			change = (copy.isP1Turn() != change);
			this.selected = change? null : sel;
		} else {
			this.selected = sel;
		}
		
		// Check if the selection is valid
		this.selectionValid = isValidSelection(
				copy.getBoard(), copy.isP1Turn(), selected);
		
		update();
	}
	
	/**
	 * Checks if a selected point is valid in the context of the current
	 * player's turn.
	 * 
	 * @param b			the current board.
	 * @param isP1Turn	the flag indicating if it is player 1's turn.
	 * @param selected	the point to test.
	 * @return true if and only if the selected point is a checker that would
	 * be allowed to make a move in the current turn.
	 */
	private boolean isValidSelection(Board b, boolean isP1Turn, Point selected) {

		// Trivial cases
		int i = Board.toIndex(selected), id = b.get(i);
		if (id == Board.EMPTY || id == Board.INVALID) { // no checker here
			return false;
		} else if(isP1Turn ^ Board.isBlackChecker(id)) { // wrong checker
			return false;
		} else if (!MoveGenerator.getSkips(b, i).isEmpty()) { // skip available
			return true;
		} else if (MoveGenerator.getMoves(b, i).isEmpty()) { // no moves
			return false;
		}
		
		// Determine if there is a skip available for another checker
		List<Point> points = b.find(
				isP1Turn? Board.BLACK_CHECKER : Board.WHITE_CHECKER);
		points.addAll(b.find(
				isP1Turn? Board.BLACK_KING : Board.WHITE_KING));
		for (Point p : points) {
			int checker = Board.toIndex(p);
			if (checker == i) {
				continue;
			}
			if (!MoveGenerator.getSkips(b, checker).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * The {@code ClickListener} class is responsible for responding to click
	 * events on the checker board component. It uses the coordinates of the
	 * mouse relative to the location of the checker board component.
	 */
	private class ClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// Get the new mouse coordinates and handle the click
			Point m = CheckerBoard.this.getMousePosition();
			if (m != null) {
				handleClick(m.x, m.y);
			}
		}
	}
}
