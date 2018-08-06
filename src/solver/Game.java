package solver;

import java.util.Scanner;

public class Game {
	
	// 5x5 arr of tiles
	// Naming Convention:
	// Rows - 12345
	// Cols - ABCDE
	
	Tile[][] board;
	static int NUM_TILES = 5;
	
	static LineStat[] rowStats = new LineStat[NUM_TILES];
	static LineStat[] colStats = new LineStat[NUM_TILES];
	
	public static final String[] ROW_NAMES = {"1", "2", "3", "4", "5"};
	public static final String[] COL_NAMES = {"1", "2", "3", "4", "5"};
	
	public Game(Scanner in) {
		
		board = new Tile[NUM_TILES][NUM_TILES];
		
		// Input style:
		// 5 1
		// implies total point value 5 and 1 bomb
		for (int i = 0; i < NUM_TILES; i++) {
			System.out.print("Enter the stats for row " + ROW_NAMES[i] + ": ");
			
			int pointTotal = in.nextInt();
			int numBombs = in.nextInt();
			
			rowStats[i] = new LineStat(numBombs, pointTotal);
		}
		
		for (int i = 0; i < NUM_TILES; i++) {
			System.out.print("Enter the stats for column " + COL_NAMES[i] + ": ");
			
			int pointTotal = in.nextInt();
			int numBombs = in.nextInt();
			
			colStats[i] = new LineStat(numBombs, pointTotal);
		}
		
		for (int r = 0; r < NUM_TILES; r++) {
			for (int c = 0; c < NUM_TILES; c++) {
				board[r][c] = new Tile(ROW_NAMES[r] + COL_NAMES[c]);
			}
		}
		
	}
	
	public void runIgnoranceCheck() {
		
		System.out.println("Running Ignorance Check...");
		
		boolean needsReiteration = true;
		
		do {
			boolean onesFound = findOnes();
			boolean bombsFound = findBombs();
			needsReiteration = onesFound || bombsFound;
		}
		while(needsReiteration);
		
		// rows
		for (int r = 0; r < NUM_TILES; r++) {
			LineStat rowStat = rowStats[r];
			if (!rowStat.isViable()) {
				markRow(r);
				for (int c = 0; c < NUM_TILES; c++) {
					board[r][c].canBeIgnored = true;
				}
			}
		}
		
		// cols
		for (int c = 0; c < NUM_TILES; c++) {
			LineStat colStat = colStats[c];
			if (!colStat.isViable()) {
				markCol(c);
				for (int r = 0; r < NUM_TILES; r++) {
					board[r][c].canBeIgnored = true;
				}
			}
		}
		
		System.out.println("Ignorance Check Complete!");
		
	}
	
	public void getNextRecommendedMove() {
		
		System.out.println("==================================================");
		System.out.println("Evaluating next best move...");
		int bestRow = -1;
		int bestCol = -1;
		int bestBombProbability = 100;
		boolean nextMoveFound = false;
		
		
		
		outer:
		for (int r = 0; r < NUM_TILES; r++) {
			inner:
			for (int c = 0; c < NUM_TILES; c++) {
				if (board[r][c].canBeIgnored || board[r][c].isFlipped) {
					continue inner;
				}
				
				int rowProbability = rowStats[r].getNumBombs() * 100 / (rowStats[r].getNumFlippable() + rowStats[r].getNumBombs());
				int colProbability = colStats[c].getNumBombs() * 100 / (colStats[c].getNumFlippable() + colStats[c].getNumBombs());
				int probability = rowProbability == 100 || colProbability == 100 
						? 100 
						: rowProbability * colProbability / 100;
				
				if (probability < bestBombProbability) {
					bestBombProbability = probability;
					bestRow = r;
					bestCol = c;
					nextMoveFound = true;
				}
			}
		}
		
		if (!nextMoveFound) {
			System.out.println("No more viable moves exist. Exiting...");
			System.exit(0);
		}
		
		System.out.println("Evaluation Complete! The best choice is:");
		System.out.println("Row: " + (bestRow + 1));
		System.out.println("Col: " + (bestCol + 1));
		System.out.println("Bomb Probability: " + bestBombProbability);
		
	}
	
	private boolean findOnes() {
		
		System.out.println("Marking cards of guarenteed value 1...");
		boolean onesFound = false;
		
		for (int r = 0; r < NUM_TILES; r++) {
			if (rowStats[r].getNumBombs() == 0 && !rowStats[r].isViable()) {
				for (int c = 0; c < NUM_TILES; c++) {
					if (board[r][c].flip(1, r, c)) {
						onesFound = true;
					}
				}
			}
		}
		
		for (int c = 0; c < NUM_TILES; c++) {
			if (colStats[c].getNumBombs() == 0 && !colStats[c].isViable()) {
				for (int r = 0; r < NUM_TILES; r++) {
					if (board[r][c].flip(1, r, c)) {
						onesFound = true;
					}
				}
			}
		}
		
		return onesFound;
		
	}
	
	private boolean findBombs() {
		
		System.out.println("Marking additionally found bombs...");
		boolean bombsFound = false;
		// rows
		for (int r = 0; r < NUM_TILES; r++) {
			if (rowStats[r].getNumFlippable() == 0) {
				for (int c = 0; c < NUM_TILES; c++) {
					if (board[r][c].flip(0, r, c)) {
						bombsFound = true;
					}
				}
			}
		}
		
		// cols
		for (int c = 0; c < NUM_TILES; c++) {
			if (colStats[c].getNumFlippable() == 0) {
				for (int r = 0; r < NUM_TILES; r++) {
					if (board[r][c].flip(0, r, c)) {
						bombsFound = true;
					}
				}
			}
		}
		
		System.out.println("Marking Complete!");
		return bombsFound;
	}
	
	// order: row col state
	public void getFlip(Scanner in) {
		
		System.out.print("Enter flip: ");
		
		int val = in.nextInt();
		
		int state = (val % 10);
		val /= 10;
		
		int col = (val % 10) - 1;
		val /= 10;
		
		int row = (val % 10) - 1;
		
		board[row][col].flip(state, row, col);
	
	}
	
	private void markRow(int row) {
		
		int numFlippable = rowStats[row].getNumFlippable();
		int totalPoints = rowStats[row].getPointTotal();
		int numIgnorable = rowStats[row].getNumIgnorable();
		
		int singleCardMax = totalPoints - numFlippable + 1;
		//int singleCardMin = totalPoints / numFlippable;
		
		// eliminate 3 and 2 as possibilities, if they exist
		for (int c = 0; c < NUM_TILES; c++) {
			for (int i = 3; i > singleCardMax; i--) {
				board[row][c].setFalse(i);
			}
		}
		
		// eliminate 1 as a possibility, if it exists
		// relies on having ignorable tiles
		int extraUsable = totalPoints;
		for (int c = 0; c < NUM_TILES; c++) {
			extraUsable -= board[row][c].state > 0 ? board[row][c].state : 0;
		}
		
		int min23 = extraUsable / 2;
		int min2 = extraUsable % 2;
		
		if (min23 + min2 >= numFlippable - numIgnorable) {
			//mark possibility for unignorable tiles
			for (int c = 0; c < NUM_TILES; c++) {
				if (!board[row][c].canBeIgnored) {
					board[row][c].setFalse(0);
					board[row][c].setFalse(1);
				}
			}
		}
		
	}
	
	private void markCol(int col) {
		
		int numFlippable = colStats[col].getNumFlippable();
		int totalPoints = colStats[col].getPointTotal();
		int numIgnorable = colStats[col].getNumIgnorable();
		
		int singleCardMax = totalPoints - numFlippable + 1;
		// int singleCardMin = totalPoints / numFlippable;
		
		// eliminate 3 and 2 as possibilities, if they exist
		for (int r = 0; r < NUM_TILES; r++) {
			for (int i = 3; i > singleCardMax; i--) {
				board[r][col].setFalse(i);
			}
		}
		
		// eliminate 1 as a possibility, if it exists
		int extraUsable = totalPoints;
		for (int r = 0; r < NUM_TILES; r++) {
			extraUsable -= board[r][col].state > 0 ? board[r][col].state : 0;
		}
		
		int min23 = extraUsable / 2;
		int min2 = extraUsable % 2;
		
		//TODO: ensure the ignorable counter is incremented only when necessary
		if (min23 + min2 >= numFlippable - numIgnorable) {
			//mark possibility for unignorable tiles
			for (int r = 0; r < NUM_TILES; r++) {
				if (!board[r][col].canBeIgnored) {
					board[r][col].setFalse(0);
					board[r][col].setFalse(1);
				}
			}
		}
		
	}
	
	public void displayBoard() {
		
		System.out.println("==================================================");
		System.out.println("Board: ");
		
		// column stats
		System.out.print("     ");
		
		// point totals
		for (int i = 0; i < NUM_TILES; i++) {
			
			System.out.print("|");
			System.out.print("PT:" 
			               + (colStats[i].getPointTotal() < 10 ? " " : "") 
			               + colStats[i].getPointTotal());
			
		}
		
		System.out.println();
		System.out.print("     ");
		
		// bombs
		for (int i = 0; i < NUM_TILES; i++) {
			
			System.out.print("|");
			System.out.print("VB: ");
			
			int numBombs = colStats[i].getNumBombs();
			
			System.out.print("" + numBombs);
		
		}
		
		System.out.println();
		System.out.print("     ");
		
		// column flips
		for (int i = 0; i < NUM_TILES; i++) {
			System.out.print("|");
			System.out.print("#F: ");
			System.out.print(colStats[i].getNumFlippable());
		}
		
		System.out.println();
		
		// print rows one at a time
		for (int i = 0; i < NUM_TILES; i++) {
			
			System.out.println("-----+-----+-----+-----+-----+-----");
			
			// points row (top)
			System.out.print("PT:" 
			               + (rowStats[i].getPointTotal() < 10 ? " " : "") 
			               + rowStats[i].getPointTotal());
			for (int j = 0; j < NUM_TILES; j++) {
				System.out.print("|");
				System.out.print((board[i][j].possibilities[0] 
						      && !board[i][j].isFlipped 
						      && !board[i][j].canBeIgnored ? "B" : " "));
				System.out.print("   ");
				System.out.print((board[i][j].possibilities[1] 
						      && !board[i][j].isFlipped 
						      && !board[i][j].canBeIgnored ? "1" : " "));
			}
			System.out.println();
			
			// bombs row (mid)
			System.out.print("VB: " + rowStats[i].getNumBombs());
			for (int j = 0; j < NUM_TILES; j++) {
				System.out.print("|  ");
				System.out.print((board[i][j].isFlipped) ? board[i][j].state : (board[i][j].canBeIgnored ? "X" : " "));
				System.out.print("  ");
			}
			System.out.println();
			
			// number of row flips (bot)
			System.out.print("#F: " 
			               + rowStats[i].getNumFlippable());
			for (int j = 0; j < NUM_TILES; j++) {
				System.out.print("|");
				System.out.print(board[i][j].possibilities[2] 
						     && !board[i][j].isFlipped 
						     && !board[i][j].canBeIgnored ? "2" : " ");
				System.out.print("   ");
				System.out.print(board[i][j].possibilities[3] 
						     && !board[i][j].isFlipped 
						     && !board[i][j].canBeIgnored ? "3" : " ");
			}
			
			System.out.println();
		
		}
	}
	
}