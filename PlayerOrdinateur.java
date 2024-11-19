

public class PlayerOrdinateur implements Player {
	BoardState boardState;
	int playerFlag = 1; //1 : Ordinateur (bot du prof), 2 : My AI

	public PlayerOrdinateur(BoardState board) {
		this.boardState = board;
	}

	@Override
	public Move move(int player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPlayerFlag() {
		// TODO Auto-generated method stub
		return playerFlag;
	}

	@Override
	public void setPlayerFlag(int playerFlag) {
		this.playerFlag = playerFlag;
	}

	@Override
	public BoardState getBoardState() {
		// TODO Auto-generated method stub
		return boardState;
	}

}
