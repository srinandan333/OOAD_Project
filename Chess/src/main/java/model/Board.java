// Creator : Creates squares and pieces

package model;

import java.util.Iterator;


public class Board implements Iterable<Square> {
	private Square[][] spots;

	public Board() {
		spots = new Square[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Square t = new Square(i, j);
				spots[i][j] = t;
			}
		}
	}


	public Square getSquare(String s) {
		if (s.length() != 2)
			return null;
		char x = s.charAt(0);
		char y = s.charAt(1);
		if (x < 'a' || x > 'h' || y < '1' || y > '8')
			return null;
		return spots[(int) (x - 'a')][7 - (int) (y - '1')];
	}

	public Square spotAt(int x, int y) {
		return spots[x - 1][8 - y];
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 8; i >= 1; i--) {
			for (int j = 1; j <= 8; j++) {
				Square square = spotAt(j, i);
				Piece piece = square.getPiece();
				if (piece == null) {
					sb.append("  ");
				} else {
					if (piece.getWhiteOrBlack()) {
						sb.append('*');
					} else {
						sb.append(' ');
					}
					sb.append(piece.getType());
				}
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	@Override
	public Iterator<Square> iterator() {
		return new BoardIterator();
	}

	private class BoardIterator implements Iterator<Square> {
		private int i = -1;

		@Override
		public boolean hasNext() {
			return i != 63;
		}

		@Override
		public Square next() {
			i++;
			return spots[i % 8][i / 8];
		}

	}
}
