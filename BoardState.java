public class BoardState {
	public static int[][] boardArr;
	public int width;
	public int height;

	public BoardState(int width, int height) {
		boardArr = new int[width][height];
		this.height = height;
		this.width = width;
	}
	public void resetBoard(){
		boardArr = new int[width][height];
	}
	public int checkEnd(int rw, int cl) {
		int r = 0, c = 0;
		int i;
		boolean ordi, AI;
		while (c < width - 4) {
			ordi = true;
			AI = true;
			for (i = 0; i < 5; i++) {
				if (boardArr[rw][c + i] != 1)
					ordi = false;
				if (boardArr[rw][c + i] != 2)
					AI = false;
			}
			if (ordi)
				return 1;
			if (AI)
				return 2;
			c++;
		}

		while (r < height - 4) {
			ordi = true;
			AI = true;
			for (i = 0; i < 5; i++) {
				if (boardArr[r + i][cl] != 1)
					ordi = false;
				if (boardArr[r + i][cl] != 2)
					AI = false;
			}
			if (ordi)
				return 1;
			if (AI)
				return 2;
			r++;
		}

		r = rw;
		c = cl;
		while (r > 0 && c > 0) {
			r--;
			c--;
		}
		while (r < height - 4 && c < width - 4) {
			ordi = true;
			AI = true;
			for (i = 0; i < 5; i++) {
				if (boardArr[r + i][c + i] != 1)
					ordi = false;
				if (boardArr[r + i][c + i] != 2)
					AI = false;
			}
			if (ordi)
				return 1;
			if (AI)
				return 2;
			r++;
			c++;
		}


		r = rw;
		c = cl;
		while (r < height - 1 && c > 0) {
			r++;
			c--;
		}

		while (r >= 4 && c < height - 4) {
			ordi = true;
			AI = true;
			for (i = 0; i < 5; i++) {
				if (boardArr[r - i][c + i] != 1)
					ordi = false;
				if (boardArr[r - i][c + i] != 2)
					AI = false;
			}
			if (ordi)
				return 1;
			if (AI)
				return 2;
			r--;
			c++;
		}
		return 0;
	}

	public int getPosition(int x, int y) {
		return boardArr[x][y];
	}

	public void setPosition(int x, int y, int player) {
		boardArr[x][y] = player;
	}

	// void pr(int[][] a) {
	// for (int i = 0; i < a.length; i++) {
	// for (int j = 0; j < a.length; j++) {
	// System.out.print(a[i][j] + "\t");
	// }
	// System.out.println();
	// }
	// }
}
