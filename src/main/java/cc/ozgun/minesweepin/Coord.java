package cc.ozgun.minesweepin;

import static cc.ozgun.minesweepin.Constants.COL_SIZE;
import static cc.ozgun.minesweepin.Constants.ROW_SIZE;

public class Coord {
	int row, col;

	public Coord(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public Coord(long value) {
		this.row = (int) (value / COL_SIZE);
		this.col = (int) (value % COL_SIZE);
	}

	boolean isValid() {
		return !(row < 0 || col < 0 || row >= ROW_SIZE || col >= COL_SIZE);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coord other = (Coord) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

}
