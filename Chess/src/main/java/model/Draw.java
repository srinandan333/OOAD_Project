// Single Responsibility Principle: this class is only used for what happens if there's a draw
// Open Closed Principle: this class implements Endgame and extends its functionality
// High cohesion: this class has methods only related to draw

package model;

public class Draw implements EndGame{
	public static Draw STALEMATE = new Draw("Stalemate", "Draw by Stalemate.");
	public static Draw FIFTY_MOVE = new Draw("Quite" , "Draw by 50-move rule.");
	public static Draw REPETITION = new Draw("Repetition", "Draw by Threefold repetition.");
	public static Draw AGREEMENT = new Draw("Agreement to draw", "Draw by Agreement.");

	private final String descript;
	private final String printOut;
	
	private Draw(String descript,  String printOut) {
		this.descript = descript;
		this.printOut = printOut;
	}
	
	@Override
	public int getResult() {
		return 0;
	}
	
	@Override
	public String getDescript() {
		return descript;
	}

	@Override
	public String getPrintOut() {
		return printOut;
	}

	@Override
	public String getDoc() {
		if(this == AGREEMENT) {
			return "1/2-1/2 (agreement)";
		} else {
			return "1/2-1/2";
		}
	}

	
}
