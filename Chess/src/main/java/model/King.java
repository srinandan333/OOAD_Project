package model;

public class King extends Piece {
	private final int VALUE = 100;

	public King(boolean c, Square Position, Chess chess) {
		super(c, Position, chess);
	}

	@Override
	public Move legalPosition(Square end) {
		if (Math.abs(spot.getX() - end.getX()) > 1 || Math.abs(spot.getY() - end.getY()) > 1)
			return null;
		if (spot.equals(end))
			return null;
		else {
			return new RegularMove(this, spot, end.getPiece(), end);
		}
	}

	@Override
	public Move getMove(Square end) {
		if (end.occupiedBy(super.isWhite)) 
			return null;

		Move legalMove = legalPosition(end);
		if (legalMove == null) {
			if (getX() == 5 && getY() == end.getY()) {
				if (end.getX() == 3) {
					return chess.canLongCastling(this);
				} else if (end.getX() == 7) {
					return chess.canShortCastling(this);
				}
			}
			return null;
		}

		if (chess.giveAwayKing(legalMove))
			return null;
		return legalMove;
	}

	@Override
	public int getValue() {
		return VALUE;
	}

	@Override
	public char getType() {
		return 'K';
	}
}