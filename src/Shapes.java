`public class Shapes {
    /**
     * Determines if there is a pair of the same player in the given row, column, or diagonal.
     * @param board The game board
     * @param row The row of the cell
     * @param col The column of the cell
     * @param player The player to check for
     * @return 1 if there is a pair, 0 otherwise
     */
    public static int isPair(Mark[][] board, int row, int col, Mark player) {
        // Check horizontal pair
        if (col + 1 < board[0].length && board[row][col] == player && board[row][col + 1] == player) { 
            // (col + 1 < board[0].length) is to check if the pair is in bound
            // board[row][col + 1] == player is to check if the next cell is the same player
            return 1;
        }
        // Check vertical pair
        if (row + 1 < board.length && board[row][col] == player && board[row + 1][col] == player) {
            return 1;
        }
        // Check diagonal pair (top-left to bottom-right)
        if (row + 1 < board.length && col + 1 < board[0].length && board[row][col] == player && board[row + 1][col + 1] == player) {
            return 1;
        }
        // Check diagonal pair (bottom-left to top-right)
        if (row - 1 >= 0 && col + 1 < board[0].length && board[row][col] == player && board[row - 1][col + 1] == player) {
            return 1;
        }
        return 0;
    }

    /**
     * Determines if there is a stretch of three of the same player in the given row, column, or diagonal.
     * @param board The game board
     * @param row The row of the cell
     * @param col The column of the cell
     * @param player The player to check for
     * @return 1 if there is a stretch, 0 otherwise
     */
    public static int isStretchTwo(Mark[][] board, int row, int col, Mark player) {
        // Check horizontal stretch two
        if (col + 2 < board[0].length && 
            board[row][col] == player && 
            board[row][col + 2] == player && 
            board[row][col + 1] == null) {
            return 1;
        }
        
        // Check vertical stretch two
        if (row + 2 < board.length && 
            board[row][col] == player && 
            board[row + 2][col] == player && 
            board[row + 1][col] == null) {
            return 1;
        }
        
        // Check diagonal stretch two (top-left to bottom-right)
        if (row + 2 < board.length && col + 2 < board[0].length && 
            board[row][col] == player && 
            board[row + 2][col + 2] == player && 
            board[row + 1][col + 1] == null) {
            return 1;
        }
        
        // Check diagonal stretch two (bottom-left to top-right)
        if (row - 2 >= 0 && col + 2 < board[0].length && 
            board[row][col] == player && 
            board[row - 2][col + 2] == player && 
            board[row - 1][col + 1] == null) {
            return 1;
        }
        
        return 0;
    }

    /**
     * Determines if there is an open tria in the given row, column, or diagonal.
     * @param board The game board
     * @param row The row of the cell
     * @param col The column of the cell
     * @param player The player to check for
     * @return 1 if there is an open tria, 0 otherwise
     */
    public static int isOpenTria(Mark[][] board, int row, int col, Mark player) {
        // Check horizontal open tria
        if (col + 2 < board[0].length && 
            board[row][col] == player && // board[row][col] is the first stone
            board[row][col + 1] == player && // board[row][col + 1] is the second stone
            board[row][col + 2] == player && // board[row][col + 2] is the third stone
            (col == 0 || board[row][col - 1] != player) && // check not bound on left
            (col + 3 >= board[0].length || board[row][col + 3] != player))
        { // check not bound on right
            return 1;
        }
        // Check vertical open tria
        if (row + 2 < board.length && 
            board[row][col] == player && 
            board[row + 1][col] == player && 
            board[row + 2][col] == player &&
            (row == 0 || board[row - 1][col] != player) && // check not bound on top
            (row + 3 >= board.length || board[row + 3][col] != player))
        { // check not bound on bottom
            return 1;
        }
        // Check diagonal open tria (top-left to bottom-right)
        if (row + 2 < board.length && col + 2 < board[0].length &&
            board[row][col] == player && 
            board[row + 1][col + 1] == player && 
            board[row + 2][col + 2] == player &&
            (row == 0 || col == 0 || board[row - 1][col - 1] != player) && // check not bound on top-left
            (row + 3 >= board.length || col + 3 >= board[0].length || board[row + 3][col + 3] != player))
        { // check not bound on bottom-right
            return 1;
        }
        // Check diagonal open tria (bottom-left to top-right)
        if (row - 2 >= 0 && col + 2 < board[0].length &&
            board[row][col] == player && 
            board[row - 1][col + 1] == player && 
            board[row - 2][col + 2] == player &&
            (row + 1 >= board.length || col == 0 || board[row + 1][col - 1] != player) && // check not bound on bottom-left
            (row - 3 < 0 || col + 3 >= board[0].length || board[row - 3][col + 3] != player))
        { // check not bound on top-right
            return 1;
        }
        return 0;
    }

    /**
     * Determines if there is a stretch of three of the same player in the given row, column, or diagonal.
     * @param board The game board
     * @param row The row of the cell
     * @param col The column of the cell
     * @param player The player to check for
     * @return 1 if there is a stretch, 0 otherwise
     */
    public static int isStretchTria(Mark[][] board, int row, int col, Mark player) {
        // Check horizontal stretch tria (pair on left)
        if (col + 3 < board[0].length && 
            board[row][col] == player && 
            board[row][col + 1] == player && // pair
            board[row][col + 2] == null &&    // gap
            board[row][col + 3] == player) { // single stone
            return 1;
        }
        
        // Check horizontal stretch tria (pair on right)
        if (col + 3 < board[0].length && 
            board[row][col] == player &&     // single stone
            board[row][col + 1] == null &&    // gap
            board[row][col + 2] == player && // pair
            board[row][col + 3] == player) {
            return 1;
        }
        
        // Check vertical stretch tria (pair on top)
        if (row + 3 < board.length && 
            board[row][col] == player && 
            board[row + 1][col] == player && // pair
            board[row + 2][col] == null &&    // gap
            board[row + 3][col] == player) { // single stone
            return 1;
        }
        
        // Check vertical stretch tria (pair on bottom)
        if (row + 3 < board.length && 
            board[row][col] == player &&     // single stone
            board[row + 1][col] == null &&    // gap
            board[row + 2][col] == player && // pair
            board[row + 3][col] == player) {
            return 1;
        }
        
        // Check diagonal stretch tria (top-left to bottom-right, pair on top)
        if (row + 3 < board.length && col + 3 < board[0].length && 
            board[row][col] == player && 
            board[row + 1][col + 1] == player && // pair
            board[row + 2][col + 2] == null &&    // gap
            board[row + 3][col + 3] == player) { // single stone
            return 1;
        }
        
        // Check diagonal stretch tria (top-left to bottom-right, pair on bottom)
        if (row + 3 < board.length && col + 3 < board[0].length && 
            board[row][col] == player &&         // single stone
            board[row + 1][col + 1] == null &&    // gap
            board[row + 2][col + 2] == player && // pair
            board[row + 3][col + 3] == player) {
            return 1;
        }
        
        // Check diagonal stretch tria (bottom-left to top-right, pair on bottom)
        if (row >= 3 && col + 3 < board[0].length && 
            board[row][col] == player && 
            board[row - 1][col + 1] == player && // pair
            board[row - 2][col + 2] == null &&    // gap
            board[row - 3][col + 3] == player) { // single stone
            return 1;
        }
        
        // Check diagonal stretch tria (bottom-left to top-right, pair on top)
        if (row >= 3 && col + 3 < board[0].length && 
            board[row][col] == player &&         // single stone
            board[row - 1][col + 1] == null &&    // gap
            board[row - 2][col + 2] == player && // pair
            board[row - 3][col + 3] == player) {
            return 1;
        }
        
        return 0;
    }

    /**
     * Determines if there is an open tessera in the given row, column, or diagonal.
     * @param board The game board
     * @param row The row of the cell
     * @param col The column of the cell
     * @param player The player to check for
     * @return 1 if there is an open tessera, 0 otherwise
     */
    public static int isOpenTessera(Mark[][] board, int row, int col, Mark player) {
        // Check horizontal open tessera
        if (col + 3 < board[0].length && 
            board[row][col] == player && 
            board[row][col + 1] == player && 
            board[row][col + 2] == player && 
            board[row][col + 3] == player &&
            (col == 0 || board[row][col - 1] != player) && // check not bound on left
            (col + 4 >= board[0].length || board[row][col + 4] != player))
        { // check not bound on right
            return 1;
        }
        // Check vertical open tessera
        if (row + 3 < board.length && 
            board[row][col] == player && 
            board[row + 1][col] == player && 
            board[row + 2][col] == player && 
            board[row + 3][col] == player &&
            (row == 0 || board[row - 1][col] != player) && // check not bound on top
            (row + 4 >= board.length || board[row + 4][col] != player))
        { // check not bound on bottom
            return 1;
        }
        // Check diagonal open tessera (top-left to bottom-right)
        if (row + 3 < board.length && col + 3 < board[0].length &&
            board[row][col] == player && 
            board[row + 1][col + 1] == player && 
            board[row + 2][col + 2] == player && 
            board[row + 3][col + 3] == player &&
            (row == 0 || col == 0 || board[row - 1][col - 1] != player) && // check not bound on top-left
            (row + 4 >= board.length || col + 4 >= board[0].length || board[row + 4][col + 4] != player))
        { // check not bound on bottom-right
            return 1;
        }
        // Check diagonal open tessera (bottom-left to top-right)
        if (row - 3 >= 0 && col + 3 < board[0].length &&
            board[row][col] == player &&
            board[row - 1][col + 1] == player &&
            board[row - 2][col + 2] == player &&
            board[row - 3][col + 3] == player &&
            (row + 1 >= board.length || col == 0 || board[row + 1][col - 1] != player) && // check not bound on bottom-left
            (row - 4 < 0 || col + 4 >= board[0].length || board[row - 4][col + 4] != player))
        { // check not bound on top-right
            return 1;
        }
        return 0;
    }
}
