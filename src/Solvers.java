public class Solvers {
	public static boolean isBlocking(Mark[][] board, Move move) {
        // Look in all 8 directions for wedge pattern
        int[][] directions = {{0,1}, {1,0}, {1,1}, {1,-1}, {0,-1}, {-1,0}, {-1,-1}, {-1,1}};
        
        for (int[] dir : directions) {
            int c = move.getCol() + dir[0];
            int r = move.getRow() + dir[1];
            
            if (Board.isInbound(c,r) ) {
                // Check for stretch two pattern
                if (board[c][r] == move.getColor().getOpponent() &&
					(Shapes.isTria(board, c, r, move.getColor().getOpponent()) ||
					Shapes.isTesseraWithEmptyEnd(board, c, r, move.getColor().getOpponent()))
				) {
                    return true;
                }
            }
        }

        return false;
    }
}
