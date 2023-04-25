// Dependency Inversion Principle: Depends on abstract class

package model;

public class RegularMove extends Move {

	public RegularMove(Piece movedPiece, Square startPosition, Piece capturedPiece, Square lastPosition) {
		super(movedPiece, startPosition, capturedPiece, lastPosition);
	}

	public String getDescript() {
		String description = "";
		if (this.isWhite)
			description += "White";
		else
			description += "Black";
		description += " " + movedPiece.getName();
		if (capturedPiece == null)
			description += " moves to " + lastPosition.toString();
		else {
			description += " catches ";
			if (this.isWhite)
				description += "black ";
			else
				description += "white ";
			description += capturedPiece.getName();
			description += " on " + lastPosition.toString();
		}
		description += note.getDescriptEnd();
		return description;
	}

	public void undo(Chess chess) {
		movedPiece.moveTo(startPosition);
		if (capturedPiece != null)
			chess.putBackToBoard(capturedPiece, lastPosition);
	}

	public void performMove(Chess chess) {
		if (capturedPiece != null)
			chess.takeOffBoard(capturedPiece);
		movedPiece.moveTo(lastPosition);
	}

}
