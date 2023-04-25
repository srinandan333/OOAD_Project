package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class SquareLabel extends JLabel {
	public static final int SQUARE_WIDTH = 55;
	public static final Font FONT_PIECE = new Font("Serif", Font.PLAIN, 40);
	public static final Color HIGHLIGHT_COLOR = Color.yellow;

	private int x;
	private int y;
	private boolean isWhite;
	private Color originalColor;
	private boolean highLight;
	private BufferedImage image;
	private ISpriteProvider symbolProvider;

	public SquareLabel() {
		super("", JLabel.CENTER);
		setPreferredSize(new Dimension(SquareLabel.SQUARE_WIDTH, SquareLabel.SQUARE_WIDTH));
		setFont(FONT_PIECE);
	}

	public SquareLabel(int i, int j, IChessViewerControl chess, boolean whiteOrBlack, ISpriteProvider symbolProvider) {
		this();
		if (whiteOrBlack) {
			x = i;
			y = 8 - j;
		} else {
			x = 9 - i;
			y = 1 + j;
		}
		isWhite = whiteOrBlack;
		if ((i + j) % 2 != 0)
			originalColor = Color.white;
		else
			originalColor = Color.gray;
		this.symbolProvider = symbolProvider;

		setBackground(originalColor);
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		setOpaque(true);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				chess.click(x, y, isWhite);
			}
		});
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public boolean isHighLight() {
		return highLight;
	}

	protected void highLight() {
		highLight = true;
		setBackground(HIGHLIGHT_COLOR);
	}

	protected void deHighLight() {
		highLight = false;
		setBackground(originalColor);
	}

	public void upDatePiece(char type, boolean wb) {
		image = getSymbol(type, wb);
	}

	public BufferedImage getSymbol(char type, boolean whiteOrBlack) {
		int color;
		if (whiteOrBlack)
			color = 0;
		else
			color = 67;
		return symbolProvider.imageAt(getXIndex(type), color, SQUARE_WIDTH, SQUARE_WIDTH);
	}

	private int getXIndex(char type) {
		switch (type) {
		case 'P':
			return 333;
		case 'R':
			return 268;
		case 'B':
			return 135;
		case 'N':
			return 201;
		case 'Q':
			return 67;
		case 'K':
			return 0;
		default:
			throw new RuntimeException("Invalid type of Chess piece" + type);
		}
	}

	public void clearLabel() {
		image = null;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(image, null, 0, 0);
		}
	}

}
