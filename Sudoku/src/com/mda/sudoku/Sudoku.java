package com.mda.sudoku;

public class Sudoku {
	private int[][] grid;
	private boolean showSolve;

	public Sudoku() {
		showSolve = false;
		initGrid();
	}

	public Sudoku(Sudoku other) {
		grid = new int[9][9];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				this.grid[i][j] = other.grid[i][j];
			}
		}
	}

	public Sudoku(int[][] grid) {
		this.grid = new int[9][9];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				this.grid[i][j] = grid[i][j];
			}
		}
	}

	public void initGrid() {
		grid = new int[9][9];

		grid[0][0] = 5;
		grid[1][0] = 3;
		grid[4][0] = 7;
		grid[0][1] = 6;
		grid[3][1] = 1;
		grid[4][1] = 9;
		grid[5][1] = 5;
		grid[1][2] = 9;
		grid[2][2] = 8;
		grid[7][2] = 6;
		grid[0][3] = 8;
		grid[4][3] = 6;
		grid[8][3] = 3;
		grid[0][4] = 4;
		grid[3][4] = 8;
		grid[5][4] = 3;
		grid[8][4] = 1;
		grid[0][5] = 7;
		grid[4][5] = 2;
		grid[8][5] = 6;
		grid[1][6] = 6;
		grid[6][6] = 2;
		grid[7][6] = 8;
		grid[3][7] = 4;
		grid[4][7] = 1;
		grid[5][7] = 9;
		grid[8][7] = 5;
		grid[4][8] = 8;
		grid[7][8] = 7;
		grid[8][8] = 9;
	}

	public void print() {
		System.out.print("    ");
		for (int i = 0; i < grid.length; i++)
			System.out.print(i + " ");
		System.out.println();
		System.out.print("    ");
		for (int i = 0; i < grid.length; i++)
			System.out.print("! ");
		System.out.println();

		for (int y = 0; y < grid[0].length; y++) {
			System.out.print(y + "-> ");
			for (int x = 0; x < grid.length; x++) {
				String v = String.valueOf(grid[x][y]);
				if (v.endsWith("0")) v = ".";
				System.out.print(v + (y == grid[x].length ? "" : "|"));
			}
			System.out.println();
		}
	}

	public void solve() {
		for (int n = 0; n < 3; n++)
			for (int i = 1; i < 10; i++) {
				blockSolve(i);
			}

		for (int n = 0; n < 3; n++)
			for (int i = 1; i < 10; i++) {
				rowSolve(i);
			}

		for (int n = 0; n < 4; n++)
			for (int i = 1; i < 10; i++) {
				colSolve(i);
			}
	}

	public void solveBlacktracking() {
		solveBlacktracking(0, 0);
	}

	private boolean solveBlacktracking(int x, int y) {
		if (x == 9) {
			x = 0;
			if (++y == 9) return true;
		}
		if (!isEmpty(x, y)) return solveBlacktracking(x + 1, y);

		for (int val = 1; val <= 9; ++val) {
			if (isValid(val, x, y)) {
				put(val, x, y);

				if (solveBlacktracking(x + 1, y)) return true;
			}
		}

		put(0, x, y);
		return false;
	}
	

	public boolean blockSolve(int value) {
		boolean ret = false;

		for (int n = 0; n < 9; n++) {
			int bx = n % 3, by = n / 3;
			int nbValid = 0, xValid = 0, yValid = 0;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					int x = i + bx * 3, y = j + by * 3;
					if (isValid(value, x, y)) {
						nbValid++;
						xValid = x;
						yValid = y;
					}
				}
			}
			// If it can only goes in one position we put it on
			if (nbValid == 1) {
				put(value, xValid, yValid);
				ret = true;
				if (showSolve) System.out.println("Block solver v:" + value + " at " + xValid + "," + yValid);
			}
		}
		return ret;
	}

	public boolean rowSolve(int value) {
		boolean ret = false;

		for (int y = 0; y < grid[0].length; y++) {
			int nbValid = 0, xValid = 0;
			for (int x = 0; x < grid.length; x++) {
				if (isValid(value, x, y)) {
					nbValid++;
					xValid = x;
				}
			}
			// If it can only goes in one position we put it on
			if (nbValid == 1) {
				put(value, xValid, y);
				ret = true;
				if (showSolve) System.out.println("Row solver v:" + value + " at " + xValid + "," + y);
			}
		}

		return ret;
	}

	public boolean colSolve(int value) {
		boolean ret = false;

		for (int x = 0; x < grid.length; x++) {
			int nbValid = 0, yValid = 0;
			for (int y = 0; y < grid[0].length; y++) {
				if (isValid(value, x, y)) {
					nbValid++;
					yValid = y;
				}
			}
			// If it can only goes in one position we put it on
			if (nbValid == 1) {
				put(value, x, yValid);
				ret = true;
				if (showSolve) System.out.println("Col solver v:" + value + " at " + x + "," + yValid);
			}
		}

		return ret;
	}

	
	public void put(int value, int x, int y) {
		grid[x][y] = value;
	}

	public boolean isValid(int value, int x, int y) {
		return isValidRow(value, y) && isValidCol(value, x) && isValidBlock(value, x, y);
	}

	public boolean isValidBlock(int value, int x, int y) {
		if (!checkBounds(x, y)) return false;

		int bx = (x / 3) * 3;
		int by = (y / 3) * 3;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (grid[bx + i][by + j] == value) return false;
			}
		}

		return true;
	}

	public boolean isValidRow(int value, int y) {
		if (!checkBounds(0, y)) return false;

		for (int i = 0; i < grid[0].length; i++)
			if (value == grid[i][y]) return false;

		return true;
	}

	public boolean isValidCol(int value, int x) {
		if (!checkBounds(x, 0)) return false;

		for (int i = 0; i < grid.length; i++)
			if (value == grid[x][i]) return false;

		return true;
	}

	public boolean isEmpty(int x, int y) {
		return grid[x][y] == 0;
	}

	public boolean checkBounds(int x, int y) {
		if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) return false;
		return true;
	}
}
