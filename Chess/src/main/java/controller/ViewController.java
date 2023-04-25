package controller;

import java.util.ArrayList;

import model.Chess;
import model.Draw;
import model.InvalidMoveException;
import model.Move;
import model.Pawn;
import model.Piece;
import model.Promotion;
import model.Square;
import model.Win;
import view.IChessViewer;
import view.IChessViewerControl;

public abstract class ViewController implements IChessViewerControl {
	protected Chess chess;

	public ViewController() {
		chess = new Chess();
		setChosen(null);
	}

	public void restart() {
		chess = new Chess();
		setChosen(null);
		IChessViewer white = chooesView(true);
		IChessViewer black = chooesView(false);
		restartView(white);
		if (black != white)
			restartView(black);
		updateChessBoard();
	}

	private void restartView(IChessViewer view) {
		view.deHighLightWholeBoard();
		view.setStatusLabelText("       Welcome to Chess Game         ");
		view.printOut("Start a new game");
	}

	private void repaintAll(IChessViewer view) {
		for (Square sq : chess.getBoard()) {
			if (sq.isOccupied()) {
				view.upDatePiece(sq.getX(), sq.getY(), sq.getPiece().getType(), sq.getPiece().getWhiteOrBlack());
			} else {
				view.clearLabel(sq.getX(), sq.getY());
			}
		}
		view.repaint();
	}


	public void undo(boolean isWhite) {
		IChessViewer view = chooesView(isWhite);
		clearHightLight(view);
		if (!chess.undoLastMove())
			view.printOut("The Game is yet to start");
		else
			view.printOut("Undo the previous move");
		repaintAll(view);
		updateStatusLabel();
	}

	protected void updateChessBoard() {
		IChessViewer white = chooesView(true);
		IChessViewer black = chooesView(false);
		repaintAll(white);
		if (black != white)
			repaintAll(black);
		updateStatusLabel();
	}

	public void click(int file, int rank, boolean whiteOrBlack) {
		IChessViewer clickedView = chooesView(whiteOrBlack);
		if (chess.hasEnd()) {
			clickedView.printOut("Game over! Restart to start a new game");
			return;
		}
		if (clickedView != chooesView(chess.getWhoseTurn())) {
			clickedView.printOut("Please wait for your opponent to move");
			return;
		}
		Square spot = chess.spotAt(file, rank);
		if (getChosen() != null) {
			Move move = getChosen().getMove(spot);
			if (move == null) {
				clickedView.cleanTemp();
			} else {
				if (move instanceof Promotion) {
					Promotion promotion = (Promotion) move;
					String promoteTo = clickedView.getPromoteTo();
					promotion.setPromoteTo(Chess.getPieceClass(promoteTo.charAt(0)));
				}
				chess.makeMove(move);
				updateGuiAfterMove(move);
			}
			clearHightLight(clickedView);
		} else {
			if (spot.occupiedBy(chess.getWhoseTurn())) {
				setChosen(spot.getPiece());
				ArrayList<Square> reachable = getChosen().getReachableSquares();
				reachable.add(spot);
				for (Square sqr : reachable) {
					clickedView.highLight(sqr.getX(), sqr.getY());
				}

				if (spot.getPiece().isType(Pawn.class))
					clickedView.printTemp(spot.toString());
				else
					clickedView.printTemp(spot.getPiece().getType() + spot.toString());
			};
		}
		updateChessBoard();
	}

	public String getRecords() {
		return chess.getRecords().toString();
	}

	@Override
	public boolean hasEnd() {
		return chess.hasEnd();
	}

	@Override
	public void resign(boolean isWhite) {
		IChessViewer view = chooesView(isWhite);
		clearHightLight(view);
		Draw canClaimDraw = chess.canClaimDraw();
		if (canClaimDraw != null) {
			view.printOut("You can ask for a draw");
			chess.endGame(canClaimDraw);
			return;
		}
		if (chess.getWhoseTurn()) {
			chess.endGame(Win.WHITERESIGN);
		} else {
			chess.endGame(Win.BLACKESIGN);
		}
		updateStatusLabel();
	}

	private void clearHightLight(IChessViewer view) {
		setChosen(null);
		view.deHighLightWholeBoard();
	}

	public void askForDraw(boolean whiteOrBlack) {
		Draw canClaimDraw = chess.canClaimDraw();
		if (canClaimDraw == null) {
			IChessViewer request = chooesView(whiteOrBlack);
			IChessViewer response = chooesView(!whiteOrBlack);
			if (this.chess.getDrawManager().canAskFordraw(whiteOrBlack)) {
				response.printOut(side(whiteOrBlack) + " is asking for a draw");
				if (response.askForDraw()) {
					chess.endGame(Draw.AGREEMENT);
				} else {
					chess.getDrawManager().setRightToRequestDraw(whiteOrBlack);
					request.printOut("Request declined");
				}
			} else {
				request.printOut("You cannot request for a draw again.");
			}
		} else {
			chess.endGame(canClaimDraw);
		}
		updateStatusLabel();
	}

		@Override
	public boolean makeMove(boolean isWhite, String moveCommand) {
		IChessViewer view = chooesView(isWhite);
		clearHightLight(view);
		Move move = null;
		try {
			move = chess.interpreteMoveCommand(moveCommand);
			chess.makeMove(move);
			updateGuiAfterMove(move);
			updateChessBoard();
			return true;
		} catch (InvalidMoveException e) {
			switch (e.type) {
			case InvalidMoveException.invalidFormat:
				view.printOut("Invalid command");
				break;
			case InvalidMoveException.ambiguousMove:
				view.printOut("Ambiguous move");
				break;
			case InvalidMoveException.castleNotAllowed:
				view.printOut("Invalid castling");
				break;
			case InvalidMoveException.impossibleMove:
				view.printOut("Invalid move");
				break;
			case InvalidMoveException.incorrectPiece:
				view.printOut("Incorrect piece present at the starting position"
						+ "\n R(Root), N(Knight), B(Bishop), Q(Queen), K(King), omission for pawn");
				break;
			case InvalidMoveException.pieceNotPresent:
				view.printOut("No piece present at the starting position");
				break;
			case InvalidMoveException.promotionTo:
				view.printOut("Specify which piece you want to promote to");
				break;
			default:
				throw new RuntimeException(e);
			}
			return false;
		}
	}

	public abstract IChessViewer chooesView(boolean whiteOrBlack);

	public void updateStatusLabel() {
		IChessViewer whiteView = chooesView(true);
		IChessViewer blackView = chooesView(false);
		whiteView.setStatusLabelText(chess.lastMoveDiscript());
		if (whiteView != blackView)
			blackView.setStatusLabelText(chess.lastMoveDiscript());
	}


	protected abstract void updateGuiAfterMove(Move move);

	protected static String side(boolean whoseTurn) {
		return whoseTurn ? "White" : "Black";
	}

	public void close() {
		chooesView(false).close();
		chooesView(true).close();
	}

	public Piece getChosen() {
		return chess.getChosen();
	}

	public void setChosen(Piece chosen) {
		this.chess.setChosen(chosen);
	}
}
