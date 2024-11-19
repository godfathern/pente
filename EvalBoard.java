
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EvalBoard {
	public int height, width;
	public int[][] EBoard;

	public EvalBoard(int height, int width) {
		this.height = height;
		this.width = width;
		EBoard = new int[height][width];
		//ResetBoard();
	}

	public void resetBoard() {
		for (int r = 0; r < height; r++)
			for (int c = 0; c < width; c++)
				EBoard[r][c] = 0;
	}

	public void setPosition(int x, int y, int diem) {
		EBoard[x][y] = diem;
	}

	public Move MaxPos() {
		int Max = 0;
		Move p = new Move(0,0);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (EBoard[i][j] > Max) {
					p.setRow(i);
					p.setCol(j);
					Max = EBoard[i][j];
				}
			}
		}
		if (Max == 0) {
			return null;
		}
		evaluationBoard = Max;
		return p;
	}
	public int evaluationBoard = 0;
	
	public void pr(int[][] a) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				System.out.print(a[i][j] + "\t");
			}
			System.out.println();
		}
	}
}
