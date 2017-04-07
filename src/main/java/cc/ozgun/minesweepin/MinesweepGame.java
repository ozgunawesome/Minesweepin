package cc.ozgun.minesweepin;

import static cc.ozgun.minesweepin.Constants.COL_SIZE;
import static cc.ozgun.minesweepin.Constants.MINES;
import static cc.ozgun.minesweepin.Constants.ROW_SIZE;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MinesweepGame {

	private int[][] field = new int[ROW_SIZE][COL_SIZE];
	private boolean[][] open = new boolean[ROW_SIZE][COL_SIZE];

	private final Scanner scanner = new Scanner(System.in);
	private final Random random = new Random();

	boolean isMine(Coord c) {
		return field[c.row][c.col] < 0;
	}

	private void doAround(Coord coord, Predicate<Coord> predicate, Consumer<Coord> operation) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				Coord newCoord = new Coord(coord.row + i, coord.col + j);
				if (newCoord.isValid() && !coord.equals(newCoord) && predicate.test(newCoord)) {
					operation.accept(newCoord);
				}
			}
		}
	}

	MinesweepGame() {
		Set<Coord> set = random.longs(0, ROW_SIZE * COL_SIZE).boxed().distinct().limit(MINES).map(Coord::new)
				.collect(Collectors.toSet());
		assert set.size() == MINES;
		set.stream().forEach(c -> field[c.row][c.col] = -1);
		set.stream().forEach(c -> doAround(c, x -> !isMine(x), x -> field[x.row][x.col]++));
	}

	Coord inputFromConsole() {
		Coord coord = null;
		while (coord == null || !coord.isValid() || open[coord.row][coord.col]) {
			System.out.print("Enter row then column: ");
			coord = new Coord(scanner.nextInt() - 1, scanner.nextInt() - 1);
		}
		return coord;
	}

	private void revealNextSquare(Queue<Coord> queue) {
		Coord coord = queue.poll();
		open[coord.row][coord.col] = true;
		if (field[coord.row][coord.col] == 0) {
			doAround(coord, x -> !open[x.row][x.col], x -> queue.add(x));
		}
	}

	void playGame() {
		boolean lost = false;
		while (!lost) {
			Coord coord = inputFromConsole();
			Queue<Coord> queue = new LinkedList<>();
			queue.add(coord);
			while (!queue.isEmpty()) {
				revealNextSquare(queue);
			}
			lost = isMine(coord);
			print(lost);
		}
		System.out.println("You lost");
	}

	void print(boolean alreadyLost) {
		System.out.println("Game status: ");
		for (int j = 0; j <= COL_SIZE; j++) {
			System.out.print(String.format("%4d", j));
		}
		System.out.println();
		for (int i = 0; i < ROW_SIZE; i++) {
			System.out.print(String.format("%4d", i + 1));
			for (int j = 0; j < COL_SIZE; j++) {
				System.out.print(String.format("%4s", (alreadyLost || open[i][j])
						? (field[i][j] == 0 ? " " : (field[i][j] < 0 ? "X" : field[i][j])) : "?"));
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		new MinesweepGame().playGame();
	}
}
