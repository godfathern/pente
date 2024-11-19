public interface Player {
	public Move move(int player);

	int getPlayerFlag();

	void setPlayerFlag(int playerFlag);

	BoardState getBoardState();
}
