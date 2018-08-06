package solver;

public class Tile {
	
	// States:
	//  0 = Bomb
	//  1 = 1
	//  2 = 2
	//  3 = 3
	
	String name;
	int state;
	boolean[] possibilities;
	boolean isFlipped;
	boolean canBeIgnored;
	
	public Tile(String name) {
		
		this.name = name;
		
		state = -1;
		possibilities = new boolean[4];
		
		for (int i = 0; i < 4; i++) {
			possibilities[i] = true;
		}
		
		isFlipped = false;
		canBeIgnored = false;
	
	}
	
	public int getValue() {
		return state >= 0 ? state : 0;
	}
	
	public void setFalse(int val) {
		if (val > 3 || val < 0) {
			System.out.println("setFalse in Tile accepted out of bounds arg: " + val);
		}
		else {
			possibilities[val] = false;
		}
	}
	
	public boolean flip(int state, int row, int col) {
		
		if (isFlipped) {
			return false;
		}
		
		if (state == 0) {
			Game.rowStats[row].removeBomb();
			Game.colStats[col].removeBomb();
		}
		else {
			Game.rowStats[row].addFlip();
			Game.rowStats[row].extraValue += state - 1 > 0 ? state - 1 : 0;
			Game.colStats[col].addFlip();
			Game.colStats[col].extraValue += state - 1 > 0 ? state - 1 : 0;
		}
		
		if (state == 0 || state == 1) {
			Game.rowStats[row].addIgnorable();
			Game.colStats[col].addIgnorable();
		}
		
		this.isFlipped = true;
		this.state = state;
		
		for (int i = 0; i < 4; i++) {
			possibilities[i] = (i == state);
		}
		
		return true;
		
	}
	
	public Tile clone() {
		
		Tile clone = new Tile(this.name);
		
		clone.isFlipped = this.isFlipped;
		clone.state = this.state;
		
		for (int i = 0; i < 4; i++) {
			clone.possibilities[i] = this.possibilities[i];
		}
		
		return clone;
		
	}
	
}
