package controller;

import model.Move;
import view.ChessViewer;
import view.IChessViewer;

public class SingleViewChessControl extends ViewController {
	private IChessViewer view;

	public SingleViewChessControl(IChessViewer view) {
		super();
		this.view = view;
		this.view.initializeViewController(this);
		updateChessBoard();
		this.view.setStatusLabelText("Welcome to Chess Game");
	}

	@Override
	public IChessViewer chooesView(boolean whiteOrBlack) {
		return view;
	}

	protected void updateGuiAfterMove(Move previousMove) {
		updateChessBoard();

		view.cleanTemp();
		view.printOut(chess.lastMoveOutPrint());
		view.printOut(side(!previousMove.getWhoseTurn()) + " to move");
	}

	public static void main(String[] args) {
		new SingleViewChessControl(new ChessViewer("Chess Game", true));
	}
}
