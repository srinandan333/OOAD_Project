package model;

public class Queen extends Piece {
	private final int VALUE = 10;

	public Queen(boolean isWhite, Square position, Chess chess) {
		super(isWhite, position, chess);
	}
	
	@Override
	public Move legalPosition(Square end) {
		if (Bishop.legalPosition(spot, end, chess) || Rook.legalPosition(spot, end, chess))
			return new RegularMove(this, spot, end.getPiece(), end);
		return null;
	}

	@Override
	public int getValue() {
		return VALUE;
	}

	@Override
	public char getType() {
		return 'Q';
	}
}
