package solver;
import java.util.Scanner;

public class MainSolver {
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Game game = new Game(in);
		game.runIgnoranceCheck();
		game.displayBoard();
		for (int i = 1;; i++) {
			System.out.println("==================================================");
			System.out.println("Turn " + i + ":");
			game.getNextRecommendedMove();
			game.getFlip(in);
			game.runIgnoranceCheck();
			game.displayBoard();
		}
	}
}
