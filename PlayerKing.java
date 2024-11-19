import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerKing implements Player {
	EvalBoard eBoard;
	BoardState boardState;
	int playerFlag = 2; //My AI
	int _x, _y; 

	public static int maxDepth = 11;
	public static int maxMove = 3;

	public int[] DScore = { 0, 1, 9, 81, 729 };
	public int[] AScore = { 0, 2, 18, 162, 1458 };
	public boolean cWin = false;
	Move goMove;

	
	public PlayerKing(BoardState board) {
		this.boardState = board;
		this.eBoard = new EvalBoard(board.width, board.height);
	}

	public void evalChessBoard(int player, EvalBoard eBoard) {
		int rw, cl;
		int eAI, eOrdi;
		eBoard.resetBoard();
		for (rw = 0; rw < eBoard.width; rw++)
			for (cl = 0; cl < eBoard.height - 4; cl++) {
				eAI = 0;
				eOrdi = 0;
				for (int i = 0; i < 5; i++) {
					if (boardState.getPosition(rw, cl + i) == 1)
						eOrdi++;
					if (boardState.getPosition(rw, cl + i) == 2)
						eAI++;
				}
				if (eOrdi * eAI == 0 && eOrdi != eAI)
					for (int i = 0; i < 5; i++) {
						if (boardState.getPosition(rw, cl + i) == 0) {
							if (eOrdi == 0) // eAI #0
								if (player == 1)
									eBoard.EBoard[rw][cl + i] += DScore[eAI]; 
									eBoard.EBoard[rw][cl + i] += AScore[eAI];
							if (eAI == 0) // eOrdi #0
								if (player == 2) {
									eBoard.EBoard[rw][cl + i] += DScore[eOrdi];
								} else {
									eBoard.EBoard[rw][cl + i] += AScore[eOrdi];
								}
							if (eOrdi == 4 || eAI == 4)
								eBoard.EBoard[rw][cl + i] *= 2;
						}
					}
			}

		for (cl = 0; cl < eBoard.height; cl++)
			for (rw = 0; rw < eBoard.width - 4; rw++) {
				eAI = 0;
				eOrdi = 0;
				for (int i = 0; i < 5; i++) {
					if (boardState.getPosition(rw + i, cl) == 1)
						eOrdi++;
					if (boardState.getPosition(rw + i, cl) == 2)
						eAI++;
				}
				if (eOrdi * eAI == 0 && eOrdi != eAI)
					for (int i = 0; i < 5; i++) {
						if (boardState.getPosition(rw + i, cl) == 0) 
						{
							if (eOrdi == 0)
								if (player == 1)
									eBoard.EBoard[rw + i][cl] += DScore[eAI];
								else
									eBoard.EBoard[rw + i][cl] += AScore[eAI];
							if (eAI == 0)
								if (player == 2)
									eBoard.EBoard[rw + i][cl] += DScore[eOrdi];
								else
									eBoard.EBoard[rw + i][cl] += AScore[eOrdi];
							if (eOrdi == 4 || eAI == 4)
								eBoard.EBoard[rw + i][cl] *= 2;
						}

					}
			}

		for (cl = 0; cl < eBoard.height - 4; cl++)
			for (rw = 0; rw < eBoard.width - 4; rw++) {
				eAI = 0;
				eOrdi = 0;
				for (int i = 0; i < 5; i++) {
					if (boardState.getPosition(rw + i, cl + i) == 1)
						eOrdi++;
					if (boardState.getPosition(rw + i, cl + i) == 2)
						eAI++;
				}
				if (eOrdi * eAI == 0 && eOrdi != eAI)
					for (int i = 0; i < 5; i++) {
						if (boardState.getPosition(rw + i, cl + i) == 0) 
						{
							if (eOrdi == 0)
								if (player == 1)
									eBoard.EBoard[rw + i][cl + i] += DScore[eAI];
								else
									eBoard.EBoard[rw + i][cl + i] += AScore[eAI];
							if (eAI == 0)
								if (player == 2)
									eBoard.EBoard[rw + i][cl + i] += DScore[eOrdi];
								else
									eBoard.EBoard[rw + i][cl + i] += AScore[eOrdi];
							if (eOrdi == 4 || eAI == 4)
								eBoard.EBoard[rw + i][cl + i] *= 2;
						}

					}
			}
		for (rw = 4; rw < eBoard.width; rw++)
			for (cl = 0; cl < eBoard.height - 4; cl++) {
				eAI = 0;
				eOrdi = 0;
				for (int i = 0; i < 5; i++) {
					if (boardState.getPosition(rw - i, cl + i) == 1)
						eOrdi++;
					if (boardState.getPosition(rw - i, cl + i) == 2)
						eAI++;
				}
				if (eOrdi * eAI == 0 && eOrdi != eAI)
					for (int i = 0; i < 5; i++) {
						if (boardState.getPosition(rw - i, cl + i) == 0) {
							if (eOrdi == 0)
								if (player == 1)
									eBoard.EBoard[rw - i][cl + i] += DScore[eAI];
								else
									eBoard.EBoard[rw - i][cl + i] += AScore[eAI];
							if (eAI == 0)
								if (player == 2)
									eBoard.EBoard[rw - i][cl + i] += DScore[eOrdi];
								else
									eBoard.EBoard[rw - i][cl + i] += AScore[eOrdi];
							if (eOrdi == 4 || eAI == 4)
								eBoard.EBoard[rw - i][cl + i] *= 2;
						}

					}
			}

	}

	int depth = 0;

	public void alphaBeta(int alpha, int beta, int depth, int player) {
		if(player==2){
			maxVal(boardState, alpha, beta, depth);
			
		}else{
			minVal(boardState, alpha, beta, depth);
		}
	}

	private int maxVal(BoardState state, int alpha, int beta, int depth) {
		evalChessBoard(2, eBoard);
		eBoard.MaxPos();
		int value = eBoard.evaluationBoard;
		if (depth >= maxDepth) {
			return value;
		}
		evalChessBoard(2, eBoard);
		ArrayList<Move> list = new ArrayList<>();
		for (int i = 0; i < maxMove; i++) {
			Move node = eBoard.MaxPos();
			if(node == null)break;
			list.add(node);
			eBoard.EBoard[node.getRow()][node.getCol()] = 0;
		}
		int v = Integer.MIN_VALUE;
		for (int i = 0; i < list.size(); i++) {
			Move com = list.get(i);
			state.setPosition(com.getRow(), com.getCol(), 2);
			v = Math.max(v, minVal(state, alpha, beta, depth+1));
			state.setPosition(com.getRow(), com.getCol(), 0);
			if(v>= beta || state.checkEnd(com.getRow(), com.getCol())==2){
				goMove = com;
				return v;
				
			}
			alpha = Math.max(alpha, v);
		}

		return v;
	}

	private int minVal(BoardState state, int alpha, int beta, int depth) {
		evalChessBoard(1, eBoard);
		eBoard.MaxPos();
		int value = eBoard.evaluationBoard;
		if (depth >= maxDepth) {
			return value;
		}
		evalChessBoard(1, eBoard);
		ArrayList<Move> list = new ArrayList<>();
		for (int i = 0; i < maxMove; i++) {
			Move node = eBoard.MaxPos();
			if(node==null)break;
			list.add(node);
			eBoard.EBoard[node.getRow()][node.getCol()] = 0;
		}
		int v = Integer.MAX_VALUE;
		for (int i = 0; i < list.size(); i++) {
			Move com = list.get(i);
			state.setPosition(com.getRow(), com.getCol(), 1);
			v = Math.min(v, maxVal(state, alpha, beta, depth+1));
			state.setPosition(com.getRow(), com.getCol(), 0);
			if(v<= alpha || state.checkEnd(com.getRow(), com.getCol())==1){
				return v;
			}
			beta = Math.min(beta, v);
		}
		return v;
	}

	
	// private ArrayList<Move> listEmty(){
	// 	ArrayList<Move> list = new ArrayList<>();
	// 	for (int i = 0; i < boardState.width; i++) {
	// 		for (int j = 0; j < boardState.height; j++) {
	// 			if (boardState.getPosition(i, j) == 0) {
	// 				list.add(new Move(i, j));
	// 			}
	// 		}
	// 	}
	// 	return list;
	// }

	public Move cook(int player) {
		depth = 0;
		alphaBeta(0, 1, 2,player);
		Move temp = goMove;
		if (temp != null) {
			_x = temp.getCol();
			_y = temp.getRow();
		}
		return new Move(_x, _y);
	}

	@Override
	public int getPlayerFlag() {
		return playerFlag;
	}

	@Override
	public void setPlayerFlag(int playerFlag) {
		this.playerFlag = playerFlag;
	}

	@Override
	public BoardState getBoardState() {
		return boardState;
	}

	@Override
	public Move move(int player) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'move'");
	}


}
