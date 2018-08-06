package solver;

public class LineStat {
	
	private int numBombs;
	private int originalNumBombs;
	private int pointTotal;
	private int baseValue;
	private int numFlippable;
	private int numIgnorable;
	int extraValue;
	
	public LineStat(int numBombs, int pointTotal) {
		this.numBombs = numBombs;
		this.originalNumBombs = numBombs;
		this.pointTotal = pointTotal;
		this.baseValue = 5 - numBombs;
		this.numFlippable = baseValue;
		this.extraValue = 0;
	}
	
	public int getNumBombs() {
		return numBombs;
	}
	
	public int getOriginalNumBombs() {
		return originalNumBombs;
	}
	
	public int getNumIgnorable() {
		return numIgnorable;
	}
	
	public void addIgnorable() {
		numIgnorable++;
	}
	
	public int getBaseValue() {
		return baseValue;
	}
	
	public int getPointTotal() {
		return pointTotal;
	}
	
	public boolean isViable() {
		return pointTotal - extraValue > baseValue;
	}
	
	public void addFlip() {
		numFlippable--;
	}
	
	public int getNumFlippable() {
		return numFlippable;
	}
	
	public void removeBomb() {
		numBombs--;
	}
	
}
