	public static boolean isBlocking(Mark[][] board, Move move, int[] direction) {
public class Solvers {        
    public static final int[][] DIRECTIONS = {       
        {1, 0}, // Right  
        {0, 1}, // Up
        {1, 1}, // Diagonal up-right
        {1, -1}, // Diagonal down-right
        
        {-1, 0}, // Left
        {0, -1}, // Down
        {-1, 1}, // Diagonal up-left
        {-1, -1} // Diagonal down-left
    };

    /**
     * Determines if the move has {@code connectionCount} connected pieces in the given direction
     * @param board The game board
     * @param col The column of the move
     * @param row The row of the move
     * @param player The player to check
     * @param connectionCount The number of connected pieces to check
     * @param direction The direction to check
     * @return  true if the move has {@code connectionCount} connected, false otherwise
     */
    public static boolean verifyConnectionCount(Mark[][] board, int col, int row, Mark player, int connectionCount, int[] direction) {        
        int count = 1; // Start with the current position
        int dc = direction[0], dr = direction[1]; // Directional change
        
        // Check in the positive direction
        for (int step = 1; step < connectionCount; step++) {
            int newCol = col + step * dc;
            int newRow = row + step * dr;
            if (Board.isInbound(newCol, newRow) && board[newCol][newRow] == player) {
                count++;
            } else {
                break;
            }
        }        

        return count >= connectionCount;
    }
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
