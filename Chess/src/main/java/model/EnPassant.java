package model;

public class EnPassant extends Move {
	private Square pawnPos;

	public EnPassant(Piece moved, Square start, Piece taken, Square end, int round) {
		super(moved, start, taken, end);
		pawnPos = taken.getSpot();
	}

	public String getDescript() {
		String s = "";
		if (this.isWhite)
			s += "White";
		else
			s += "Black";
		s += " Pawn";
		s += " moves to " + lastPosition.toString();
		s += " catches pawn on " + pawnPos.toString() + " by en passant";
		s += note.getDescriptEnd();
		return s;
	}

	public void undo(Chess chess) {
		movedPiece.moveTo(startPosition);
		if (capturedPiece != null) {
			chess.putBackToBoard(capturedPiece, pawnPos);
		}
	}

	public void performMove(Chess chess) {
		if (capturedPiece != null)
			chess.takeOffBoard(capturedPiece);
		movedPiece.moveTo(lastPosition);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EnPassant) {
			EnPassant x = (EnPassant) obj;
			return pawnPos == x.pawnPos && super.equals(obj);
		}
		return false;
	}

}
