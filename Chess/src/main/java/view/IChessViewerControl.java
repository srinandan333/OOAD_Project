package view;

public interface IChessViewerControl
{
	boolean hasEnd();

	void click(int file, int rank, boolean whiteOrBlack);

	String getRecords();

	void restart();

	void resign(boolean isWhite);

	void askForDraw(boolean isWhite);

	void undo(boolean isWhite);

	boolean makeMove(boolean isWhite, String move);
}
