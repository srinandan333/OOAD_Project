// Low coupling: All the subclasses are independent and depend on this abstract class

package model;

import java.util.ArrayList;

public abstract class Piece implements Comparable<Piece> {
	protected Chess chess;
	protected boolean isWhite;
	protected Square spot;

	public Piece(boolean c, Square p, Chess chess) {
		this.chess = chess;
		this.isWhite = c;
		moveTo(p);
	}

	public String getName() {
		String s = getClass().getName();
		return s.substring(s.lastIndexOf(".") + 1);
	}

	public Square getSpot() {
		return spot;
	}

	public boolean getWhiteOrBlack() {
		return this.isWhite;
	}

	public abstract char getType();

	public char getPENChar() {
		if (isWhite)
			return getType();
		else
			return Character.toLowerCase(getType());
	}

	public boolean isType(Class<? extends Piece> type) {
		return getClass() == type;
	}

	public abstract int getValue();

	public int compareTo(Piece a) {
		return (a.getValue() - getValue());
	}

	public int getX() {
		return spot.getX();
	}

	public int getY() {
		return spot.getY();
	}

	public void moveTo(Square p) {
		if (spot != null) {
			spot.setOccupied(null);
		}
		if (p != null) {
			p.setOccupied(this);
		}
		spot = p;
	}

	public boolean canGo(Square end) {
		return getMove(end) != null;
	}

	public Move getMove(Square end) {
		// cannot move to own piece
		if (end.occupiedBy(this.isWhite))
			return null;
		Move legalMove = legalPosition(end);
		if (legalMove == null)
			return null;
		if (chess.giveAwayKing(legalMove))
			return null;
		return legalMove;
	}

	public ArrayList<Square> getReachableSquares() {
		ArrayList<Square> list = new ArrayList<>();
		for (Square i : this.chess.getBoard())
			if (this.canGo(i))
				list.add(i);
		return list;
	}

	public Move canAttack(Square end) {
		if (spot == null)
			return null;
		return legalPosition(end);
	}

	public abstract Move legalPosition(Square end);

	public String toString() {
		return getName() + " at " + getSpot();
	}
}
