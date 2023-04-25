package model;

public class Promotion extends Move {
	private Piece promotedTo;

	public Promotion(Piece moved, Square start, Piece taken, Square end, int round) {
		super(moved, start, taken, end);
		this.promotedTo = null;
	}

	public String getDoc() {
		checkPromotedTo();
		return super.getDoc() + "(" + promotedTo.getType() + ")";
	}

	public String getPrintOut() {
		checkPromotedTo();
		return getDoc() + " Successfully promoted to " + promotedTo.getName() + "!";
	}

	public String getDescript() {
		checkPromotedTo();
		String s = "";
		if (super.isWhite)
			s += "White ";
		else
			s += "Black ";
		s += "Pawn promoted to ";
		s += promotedTo.getName() + "!";

		return s;
	}

	public boolean notQuiet() {
		return true;
	}

	public void undo(Chess chess) {
		if (promotedTo == null) {
			movedPiece.moveTo(startPosition);
		} else {
			chess.takeOffBoard(promotedTo);
			chess.putBackToBoard(movedPiece, startPosition);
		}
		if (capturedPiece != null)
			chess.putBackToBoard(capturedPiece, lastPosition);
	}

	public void performMove(Chess chess) {
		if (capturedPiece != null)
			chess.takeOffBoard(capturedPiece);
		if (promotedTo == null) {
			movedPiece.moveTo(lastPosition);
		} else {
			chess.takeOffBoard(movedPiece);
			chess.putBackToBoard(promotedTo, lastPosition);
		}
	}

	public void setPromoteTo(Class<? extends Piece> promotToClass) {
		promotedTo = getPromotedPiece(promotToClass);
	}

	private Piece getPromotedPiece(Class<? extends Piece> promotToClass) {
		if (promotToClass.equals(Queen.class))
			return new Queen(this.isWhite, this.lastPosition, this.movedPiece.chess);
		else if (promotToClass.equals(Knight.class))
			return new Knight(this.isWhite, this.lastPosition, this.movedPiece.chess);
		if (promotToClass.equals(Rook.class))
				return new Rook(this.isWhite, this.lastPosition, this.movedPiece.chess);
		if (promotToClass.equals(Bishop.class))
			return new Bishop(this.isWhite, this.lastPosition, this.movedPiece.chess);
		throw new RuntimeException("Invalid type of piece to promote to");
	}

	private void checkPromotedTo() {
		if (promotedTo == null)
			throw new RuntimeException("Specify which piece to promote to");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Promotion) {
			Promotion x = (Promotion) obj;
			return x.promotedTo == x.promotedTo && super.equals(obj);
		}
		return false;
	}

}
