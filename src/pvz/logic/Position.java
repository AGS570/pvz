package pvz.logic;

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

	public int getRow() {
        return fil;
    }

    public int getCol() {
        return col;
    }
 
    public boolean isValid(int r, int c) {
    	if(this.fil < r && this.fil >= 0 && this.col < c && this.col >=0) return true;
    	return false;
    }
	
}
