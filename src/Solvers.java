import java.util.ArrayList;

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
    public static int verifyConnectionCount(Mark[][] board, int col, int row, Mark player, int[] direction) {        
        int count = 1; // Start with the current position
        int dc = direction[0], dr = direction[1]; // Directional change
        
        // Check in the positive direction
        for (int step = 1; step < 6; step++) {
            int newCol = col + step * dc;
            int newRow = row + step * dr;
            if (Board.isInbound(newCol, newRow) && board[newCol][newRow] == player) {
                count++;
            } else {
                break;
            }
        }
        
        for (int step = 1; step < 6; step++) {
            int newCol = col - step * dc;
            int newRow = row - step * dr;
            if (Board.isInbound(newCol, newRow) && board[newCol][newRow] == player) {
                count++;
            } else {
                break;
            }
        }    

        return count;
    }

    /**
     * Determines if the move is blocking one of the opponent's lines
     * @param board The game board
     * @param move The move to check
     * @param direction The direction to check
     * @return true if the move is blocking, false otherwise
     */
    public static boolean isBlocking(Mark[][] board, Move move, int[] direction) {               
        int c = move.getCol() + direction[0];
        int r = move.getRow() + direction[1];
        int cc = move.getCol() - direction[0]; // Opposite direction
        int rr = move.getRow() - direction[1]; // Opposite direction
        if (Board.isInbound(c,r) ) {
            // Check if the next cell is the opponent's cell
            if(board[c][r] == move.getColor().getOpponent()) {
                int firstDir = Solvers.verifyConnectionCount(board, c, r, move.getColor().getOpponent(), direction) - 1;
                int secondDir = Solvers.verifyConnectionCount(board, cc, rr, move.getColor().getOpponent(), direction);
                return firstDir + secondDir >= 2;
            }
        }

        return false;
    }

    /**
     * Determines if the move is blocking one of the opponent's lines
     * @param board The game board
     * @param move The move to check
     * @param direction The direction to check
     * @return true if the move is blocking, false otherwise
     */
    public static boolean isBlocking2(Mark[][] board, Move move) {               
        ArrayList<Move[]> chains = Board.getChains();

        for (Move[] chain : chains) {
            Move first = chain[0];
            Move last = chain[chain.length - 1];
            int colDir = 0;
            int rowDir = 0;        
            int colDiff = last.getCol() - first.getCol();
            int rowDiff = last.getRow() - first.getRow();
            
            if(move.getCol() == first.getCol() && move.getRow() == first.getRow()) {
                continue;
            }

            if(colDiff != 0) {
                colDir = colDiff / Math.abs(colDiff);
            }

            if(rowDiff != 0) {
                rowDir = rowDiff / Math.abs(rowDiff);
            }

            int[] dir = { colDir, rowDir };
            int c = first.getCol() - dir[0];
            int r = first.getRow() - dir[1];
            int cc = last.getCol() + dir[0]; // Opposite direction
            int rr = last.getRow() + dir[1]; // Opposite direction

            if (Board.isInbound(c,r)) {                                   
                if(board[c][r] == move.getColor()) {
                    return true;
                }
            }

            if (Board.isInbound(cc,rr)) {                                   
                if(board[cc][rr] == move.getColor()) {
                    return true;
                }
            }
        }        

        return false;
    }

    /**
     * Determines if the move is blocking one of the opponent's lines
     * @param board The game board
     * @param move The move to check
     * @param direction The direction to check
     * @return true if the move is blocking, false otherwise
     */
    public static int getChainBlockerCount(Mark[][] board, Move[] chain) {               
        int blockerCount = 0;
        Move first = chain[0];
        Move last = chain[chain.length - 1];
        int colDir = 0;
        int rowDir = 0;        
        int colDiff = last.getCol() - first.getCol();
        int rowDiff = last.getRow() - first.getRow();

        if(colDiff != 0) {
            colDir = colDiff / Math.abs(colDiff);
        }

        if(rowDiff != 0) {
            rowDir = rowDiff / Math.abs(rowDiff);
        }

        int[] direction = { colDir, rowDir };

        int c = first.getCol() - direction[0];
        int r = first.getRow() - direction[1];
        int cc = last.getCol() + direction[0]; // Opposite direction
        int rr = last.getRow() + direction[1]; // Opposite direction

        if (Board.isInbound(c,r)) {                   
            if(board[c][r] == first.getColor().getOpponent()) {
                blockerCount++;
            }

            
            return  blockerCount; // 1 or 2
        }

        if (Board.isInbound(cc,rr)) {                
            if(board[cc][rr] == first.getColor().getOpponent()) {
                blockerCount++;
            }
        }

        return blockerCount; // 0
    }

    /**
     * Determines if the move is a capture move
     * @param board The game board
     * @param move The move to check
     * @param direction The direction to check
     * @return true if the move is a capture move, false otherwise
     */
    public static boolean isCapture(Mark[][] board, Move move, int[] direction) {
        int c = move.getCol() + direction[0];
        int r = move.getRow() + direction[1];
        int c2 = c + direction[0];
        int r2 = r + direction[1];
        int c3 = c2 + direction[0];
        int r3 = r2 + direction[1];
        
        // Check if the move is a pair
        return Board.isInbound(c,r) && 
               board[c][r] == move.getColor().getOpponent() &&
               Board.isInbound(c2, r2) &&
               board[c2][r2] == move.getColor().getOpponent() &&
               Board.isInbound(c3, r3) &&
               board[c3][r3] == move.getColor();
    }
}
