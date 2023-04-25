// single responsibility principle

package model;

public class Square {
	private int x;
	private int y;
	private String position;
	private Piece occupiedPiece;

	public Square(int i, int j) {
		x = i + 1;
		y = 8 - j;
		char col = (char) (97 + i);
		int row = 8 - j;
		position = "" + col + row;
		occupiedPiece = null;
	}

	public String toString() {
		return position;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Piece getPiece() {
		return occupiedPiece;
	}

	public boolean isOccupied() {
		return occupiedPiece != null;
	}

	public boolean occupiedBy(boolean color) {
		if (isOccupied())
			return color == (occupiedPiece.getWhiteOrBlack());
		else
			return false;
	}

	public void setOccupied(Piece piece) {
		occupiedPiece = piece;
	}

}