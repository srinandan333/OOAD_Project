// Dependency Inversion Principle: depends on abstractions like Piece, Move

package model;

import controller.DrawManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class Chess {
	private int time;
	private Board board;
	ArrayList<Piece> white;
	ArrayList<Piece> black;
	private Record records;

	private Collection<Square> list;
	private DrawManager drawManager;
	private Piece chosen;

	public Chess() {
		time = 0;
		records = new Record();
		drawManager = new DrawManager();
		board = new Board();
		white = new ArrayList<Piece>();
		black = new ArrayList<Piece>();
		list = new ArrayList<Square>();

		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				Square t = board.spotAt(i, j);
				int y = t.getY();
				if (y == 1) {
					white.add(startSet(t.getX(), true, t));
				} else if (y == 2) {
					white.add(new Pawn(true, t, this));
				} else if (y == 7) {
					black.add(new Pawn(false, t, this));
				} else if (y == 8) {
					black.add(startSet(t.getX(), false, t));
				}
				list.add(t);
			}
		}
		Collections.sort(white);
		Collections.sort(black);
	}

	private Piece startSet(int x, boolean c, Square p) {
		if (x == 1 || x == 8)
			return new Rook(c, p, this);
		else if (x == 2 || x == 7)
			return new Knight(c, p, this);
		else if (x == 3 || x == 6)
			return new Bishop(c, p, this);
		else if (x == 4)
			return new Queen(c, p, this);
		else if (x == 5)
			return new King(c, p, this);
		else
			return null;
	}

	public boolean getWhoseTurn() {
		return time % 2 == 0;
	}

	public int getRound() {
		return time / 2 + 1;
	}

	public boolean hasEnd() {
		return records.hasEnd();
	}

	public Board getBoard() {
		return board;
	}

	public Square spotAt(int file, int rank) {
		return board.spotAt(file, rank);
	}

	public Record getRecords() {
		return records;
	}

	@Override
	public String toString() {
		return board.toString();
	}

	public ArrayList<Piece> possibleMovers(Class<? extends Piece> type, Square end) {
		ArrayList<Piece> possible = new ArrayList<Piece>();
		ArrayList<Piece> set;
		if (getWhoseTurn())
			set = white;
		else
			set = black;

		for (int j = 0; j < set.size(); j++) {
			Piece i = set.get(j);
			if (i.isType(type) && i.canGo(end))
				possible.add(i);
		}
		return possible;
	}

	public boolean giveAwayKing(Move move) {
		move.performMove(this);
		boolean giveAway = checkOrNot(!move.getWhoseTurn());
		move.undo(this);
		return giveAway;
	}

	public boolean checkOrNot(boolean attacker) {
		if (attacker)
			return isAttacked(true, black.get(0).getSpot());
		else
			return isAttacked(false, white.get(0).getSpot());
	}

	public boolean isAttacked(boolean whiteOrBlack, Square square) {
		ArrayList<Piece> attacker;
		if (whiteOrBlack)
			attacker = white;
		else
			attacker = black;

		for (int j = 0; j < attacker.size(); j++) {
			Piece i = attacker.get(j);
			if (i.canAttack(square) != null)
				return true;
		}
		return false;
	}

	boolean checkMate(boolean checked) {
		ArrayList<Piece> inCheck;
		if (checked)
			inCheck = white;
		else
			inCheck = black;
		for (int j = 0; j < inCheck.size(); j++) {
			Piece i = inCheck.get(j);
			Iterator<Square> iter = board.iterator();
			while (iter.hasNext()) {
				Square p = iter.next();
				if (i.canGo(p)) {
					return false;
				}
			}
		}
		return true;
	}

	boolean impossibleCheckMate() {
		if (this.white.size() == 1 && this.black.size() == 1) {
			return true;
		}
		final char kingAndBishop[] = { 'K', 'B' };
		final char kingAndKnight[] = { 'K', 'N' };
		if (this.white.size() == 1) {
			if (containsPieces(this.black, kingAndBishop) || containsPieces(this.black, kingAndKnight)) {
				return true;
			}
		} else if (this.black.size() == 1) {
			if (containsPieces(this.white, kingAndBishop) || containsPieces(this.white, kingAndKnight)) {
				return true;
			}
		} else if (containsPieces(this.white, kingAndBishop) && containsPieces(this.black, kingAndBishop)) {
			boolean bishopColor = false;
			for (int i = 0; i < this.white.size(); i++) {
				Piece p = this.white.get(i);
				if (p instanceof Bishop) {
					bishopColor = ((Bishop) p).getBishopType();
				}
			}

			for (int i = 0; i < this.black.size(); i++) {
				Piece p = this.black.get(i);
				if (p instanceof Bishop) {
					return ((Bishop) p).getBishopType() == bishopColor;
				}
			}
		}

		return false;
	}


	private boolean containsPieces(ArrayList<Piece> pieces, char[] pieceTypes) {
		if (pieces.size() == pieceTypes.length) {
			for (int i = 0; i < pieces.size(); i++) {
				boolean found = false;
				for (int j = 0; j < pieceTypes.length; j++) {
					if (pieces.get(i).isType(getPieceClass(pieceTypes[j]))) {
						found = true;
						break;
					}
				}
				if (!found)
					return false;
			}
			return true;
		}

		return false;
	}


	public Draw canClaimDraw() {
		if (isFiftySilentMove())
			return Draw.FIFTY_MOVE;
		if (isThreeFoldRepetition())
			return Draw.REPETITION;
		return null;
	}

	private boolean isThreeFoldRepetition() {
//		circleLoop: for (int circle = 4; circle < 50 && circle * 2 > time; circle += 2) {
//			for (int j = 1; j <= circle; j++) {
//				if (!records.get(time - j).equals(records.get(time - j - circle))) {
//					continue circleLoop;
//				}
//			}
//			return true;
//		}
		return false;
	}

	private boolean isFiftySilentMove() {
		if (time > 50) {
			for (int i = time - 50; i < time; i++) {
				if (records.get(i).notQuiet()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean canEnPassant(Square end) {
		Move move = lastMove();
		if (move == null)
			return false;
		return move.canEnPassant(end);
	}

		public Move canShortCastling(King k) {
		if (canNotShortCastling(k.getY(), !k.getWhiteOrBlack()))
			return null;
		return new Castling(k, k.getSpot(), spotAt(7, k.getY()), (Rook) (spotAt(8, k.getY()).getPiece()),
				spotAt(8, k.getY()), getRound());
	}

	public Move canLongCastling(King k) {
		if (canNotLongCastling(k.getY(), !k.getWhiteOrBlack()))
			return null;
		return new Castling(k, k.getSpot(), spotAt(3, k.getY()), (Rook) (spotAt(1, k.getY()).getPiece()),
				spotAt(1, k.getY()), getRound());
	}

	private boolean canNotLongCastling(int y, boolean attack) {
		return spotAt(2, y).isOccupied() || spotAt(3, y).isOccupied() || spotAt(4, y).isOccupied()
				|| isAttacked(attack, spotAt(5, y)) || isAttacked(attack, spotAt(3, y))
				|| isAttacked(attack, spotAt(4, y)) || records.hasMoved(spotAt(1, y), Rook.class, time)
				|| records.hasMoved(spotAt(5, y), King.class, time);
	}

	private boolean canNotShortCastling(int y, boolean attack) {
		return spotAt(6, y).isOccupied() || spotAt(7, y).isOccupied() || isAttacked(attack, spotAt(5, y))
				|| isAttacked(attack, spotAt(6, y)) || isAttacked(attack, spotAt(7, y))
				|| records.hasMoved(spotAt(8, y), Rook.class, time) || records.hasMoved(spotAt(5, y), King.class, time);
	}

	public String lastMoveOutPrint() {
		Move move = lastMove();
		if (move == null)
			return null;
		return move.getPrintOut();
	}

	public String lastMoveDiscript() {
		if (records.hasEnd())
			return records.getEndGameDescript();
		Move move = lastMove();
		if (move == null)
			return "The Game hasn't started yet";
		return move.getDescript();
	}

	public Move lastMove() {
		if (time == 0)
			return null;
		return records.get(time - 1);
	}

	// modifiers

	public void takeOffBoard(Piece taken) {
		Square p = taken.getSpot();
		if (p == null)
			return;
		taken.getSpot().setOccupied(null);
		if (taken.getWhiteOrBlack())
			white.remove(taken);
		else
			black.remove(taken);
	}

	public void putBackToBoard(Piece taken, Square spot) {
		if (taken != null) {
			if (taken.getWhiteOrBlack()) {
				white.add(taken);
			} else {
				black.add(taken);
			}
			taken.moveTo(spot);
		}
	}

	public boolean undoLastMove() {
		Move lastMove = lastMove();
		if (lastMove == null)
			return false;
		lastMove.undo(this);
		records.removeLast();
		time--;
		return true;
	}

	public void makeMove(Move move) {
		move.performMove(this);
		records.add(move);
		time++;

		// check end game situations
		if (checkOrNot(!getWhoseTurn())) {
			if (checkMate(getWhoseTurn())) {
				move.note = MoveNote.CHECKMATE;
				if (getWhoseTurn())
					endGame(Win.BLACKCHECKMATE);
				else
					endGame(Win.WHITECHECKMATE);
				return;
			}
			move.note = MoveNote.CHECK;
		} else {
			if (checkMate(getWhoseTurn()) || impossibleCheckMate()) {
				endGame(Draw.STALEMATE);
				return;
			}
		}
	}

	public void endGame(EndGame endgame) {
		records.endGame(endgame);
	}

	public Move interpreteMoveCommand(String moveCommand) throws InvalidMoveException {
		Move move = null;
		if (moveCommand.startsWith("O")) {
			King king;
			if (getWhoseTurn()) {
				king = (King) white.get(0);
			} else {
				king = (King) black.get(0);
			}
			if (moveCommand.equals("O-O")) {
				move = canShortCastling(king);
			} else if (moveCommand.equals("O-O-O")) {
				move = canLongCastling(king);
			} else {
				throw new InvalidMoveException(moveCommand, InvalidMoveException.invalidFormat);
			}
			if (move != null) {
				return move;
			} else {
				throw new InvalidMoveException(moveCommand, InvalidMoveException.castleNotAllowed);
			}
		}

		IMovePatternMatcher m = new MovePatternMatcher(moveCommand);
		if (m.matches()) {
			Class<? extends Piece> type = m.getGroup(1) == null ? Pawn.class : getPieceClass(m.getGroup(1).charAt(0));
			Square start = null;
			if ((m.getGroup(2) != null) && (m.getGroup(3) != null)) {
				start = board.getSquare(m.getGroup(2) + m.getGroup(3));
			}
			Square end = board.getSquare(m.getGroup(5));
			if (start != null) {
				Piece movedPiece = start.getPiece();
				if (movedPiece == null) {
					throw new InvalidMoveException(moveCommand, InvalidMoveException.pieceNotPresent);
				} else if (!(movedPiece.isType(type))) {
					throw new InvalidMoveException(moveCommand, InvalidMoveException.incorrectPiece);
				}
				move = movedPiece.getMove(end);
			} else {
				ArrayList<Piece> possible = possibleMovers(type, end);
				if (possible.size() == 0) {
					throw new InvalidMoveException(moveCommand, InvalidMoveException.impossibleMove);
				} else if (possible.size() == 1) {
					move = possible.get(0).getMove(end);
				} else {
					throw new InvalidMoveException(moveCommand, InvalidMoveException.ambiguousMove);
				}
			}
			if (move instanceof Promotion) {
				Promotion promotion = (Promotion) move;
				if (m.getGroup(6) == null)
					throw new InvalidMoveException(moveCommand, InvalidMoveException.promotionTo);
				Class<? extends Piece> promotToClass = getPieceClass(m.getGroup(6).charAt(1));
				promotion.setPromoteTo(promotToClass);
			}
			return move;
		} else {
			throw new InvalidMoveException(moveCommand, InvalidMoveException.invalidFormat);
		}
	}

	public static Class<? extends Piece> getPieceClass(char character) {
		switch (Character.toUpperCase(character)) {
		case 'P':
			return Pawn.class;
		case 'R':
			return Rook.class;
		case 'N':
			return Knight.class;
		case 'B':
			return Bishop.class;
		case 'Q':
			return Queen.class;
		case 'K':
			return King.class;
		default:
			return Piece.class;
		}
	}

  public DrawManager getDrawManager() {
		return this.drawManager;
	}

	public Piece getChosen() {
		return chosen;
	}

	public void setChosen(Piece chosen) {
		this.chosen = chosen;
	}
}