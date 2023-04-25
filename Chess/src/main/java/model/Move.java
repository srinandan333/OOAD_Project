// Open closed principle
// Information expert
// High cohesion

package model;

import java.util.Objects;

public abstract class Move {
	protected boolean isWhite;
	protected final Piece movedPiece;
	protected final Square startPosition;
	protected final Piece capturedPiece;
	protected final Square lastPosition;
	protected MoveNote note;

	public Move(Piece movedPiece, Square startPosition, Piece capturedPiece, Square lastPosition) {
		this.isWhite = movedPiece.getWhiteOrBlack();
		this.movedPiece = movedPiece;
		this.startPosition = startPosition;
		this.capturedPiece = capturedPiece;
		this.lastPosition = lastPosition;
		this.note = MoveNote.NONE;
	}

	public Square getStart() {
		return startPosition;
	}

	public boolean canEnPassant(Square p) {
		return movedPiece.isType(Pawn.class) && (startPosition.getX() == p.getX() && lastPosition.getX() == p.getX()
				&& (startPosition.getY() + lastPosition.getY()) == (p.getY() * 2));
	}

	public String toString() {
		return getPrintOut() + " " + getDescript();
	}

	public boolean getWhoseTurn() {
		return isWhite;
	}

	public String getDoc() {
		String doc = "";
		if (!movedPiece.isType(Pawn.class))
			doc += movedPiece.getType();
		doc += startPosition.toString();
		if (capturedPiece == null)
			doc += "-";
		else
			doc += "x";
		doc += lastPosition.toString();
		doc += note.getDocEnd();
		return doc;
	}

	public String getPrintOut() {
		return getDoc();
	}

	public boolean notQuiet() {
		return capturedPiece != null || movedPiece.isType(Pawn.class);
	}

	public abstract String getDescript();

	public abstract void performMove(Chess chess);

	public abstract void undo(Chess chess);

	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Move) {
			Move x = (Move) obj;
			return isWhite == x.isWhite && movedPiece.equals(x.movedPiece) && startPosition.equals(x.startPosition)
					&& lastPosition.equals(x.lastPosition) && Objects.equals(capturedPiece, x.capturedPiece);
		}
		return false;
	}
}
