public class Solvers {
	public static boolean isBlocking(Mark[][] board, Move move, int[] direction) {
        // Look in all 8 directions for wedge pattern        
            int c = move.getCol() + direction[0];
            int r = move.getRow() + direction[1];        
            if (Board.isInbound(c,r) ) {
                // Check if the next cell is the opponent's cell
                if(board[c][r] == move.getColor().getOpponent()) {
                    return Shapes.isConnected(board, c, r, move.getColor().getOpponent(), 4, direction) ||
                        Shapes.isConnected(board, c, r, move.getColor().getOpponent(), 3, direction);
                }
            }        

        return false;
    }
}
