package logic;

public class Position {

	private int fil;
	private int col;
	public Position(int row, int col) {
		// TODO Auto-generated constructor stub
		this.fil = row;
		this.col= col;
	}
	
	public String toString() {
		return fil + " " + col;
	}

}
