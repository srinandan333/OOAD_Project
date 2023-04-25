package view;

public interface IChessViewer
{
	void initializeViewController(IChessViewerControl controller);

	void printOut(String message);

	void printTemp(String temp);

	void cleanTemp();

	void setStatusLabelText(String str);

	void highLight(int file, int rank);

	void deHighLightWholeBoard();

	void repaint();

	void upDatePiece(int file, int rank, char pieceType, boolean whiteOrBlack);

	void clearLabel(int file, int rank);

	boolean askForDraw();

	String getPromoteTo();

	void close();
}