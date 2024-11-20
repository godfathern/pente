public class Shapes {
    /**
     * Determines if there is a pair of the same player in the given row, column, or diagonal.
     * @param board The game board
     * @param col The row of the cell
     * @param row The column of the cell
     * @param player The player to check for
     * @return 1 if there is a pair, 0 otherwise
     */
    public static boolean isPair(Mark[][] board, int col, int row, Mark player) {
        // Check horizontal pair
        if (row + 1 < board[0].length && board[col][row] == player && board[col][row + 1] == player) { 
            // (col + 1 < board[0].length) is to check if the pair is in bound
            // board[row][col + 1] == player is to check if the next cell is the same player
            return true;
        }
        // Check vertical pair
        if (col + 1 < board.length && board[col][row] == player && board[col + 1][row] == player) {
            return true;
        }
        // Check diagonal pair (top-left to bottom-right)
        if (col + 1 < board.length && row + 1 < board[0].length && board[col][row] == player && board[col + 1][row + 1] == player) {
            return true;
        }
        // Check diagonal pair (bottom-left to top-right)
        if (col - 1 >= 0 && row + 1 < board[0].length && board[col][row] == player && board[col - 1][row + 1] == player) {
            return true;
        }

        return false;
    }

    /**
     * Determines if there is a pair of the same player in the given row, column, or diagonal.
     * @param board The game board
     * @param col The row of the cell
     * @param row The column of the cell
     * @param player The player to check for
     * @return 1 if there is a pair, 0 otherwise
     */
    public static boolean isOpenPair(Mark[][] board, int col, int row, Mark player) {
        // Check horizontal pair
        if (row + 1 < board[0].length && board[col][row] == player && board[col][row + 1] == player) {
            // (col + 1 < board[0].length) is to check if the pair is in bound
            // board[row][col + 1] == player is to check if the next cell is the same player
            return true;
        }
        // Check vertical pair
        if (col + 1 < board.length && board[col][row] == player && board[col + 1][row] == player) {
            return true;
        }
        // Check diagonal pair (top-left to bottom-right)
        if (col + 1 < board.length && row + 1 < board[0].length && board[col][row] == player && board[col + 1][row + 1] == player) {
            return true;
        }
        // Check diagonal pair (bottom-left to top-right)
        if (col - 1 >= 0 && row + 1 < board[0].length && board[col][row] == player && board[col - 1][row + 1] == player) {
            return true;
        }

        return false;
    }

    /**
     * Determines if there is a stretch of three of the same player in the given row, column, or diagonal.
     * @param board The game board
     * @param col The row of the cell
     * @param row The column of the cell
     * @param player The player to check for
     * @return 1 if there is a stretch, 0 otherwise
     */
    public static boolean isStretchTwo(Mark[][] board, int col, int row, Mark player) {
        // Check horizontal stretch two
        if (row + 2 < board[0].length && 
            board[col][row] == player && 
            board[col][row + 2] == player && 
            board[col][row + 1] == Mark.Empty
        ) {
            return true;
        }
        
        // Check vertical stretch two
        if (col + 2 < board.length &&
            board[col][row] == player &&
            board[col + 2][row] == player && 
            board[col + 1][row] == Mark.Empty
        ) {
            return true;
        }
        
        // Check diagonal stretch two (top-left to bottom-right)
        if (col + 2 < board.length && row + 2 < board[0].length && 
            board[col][row] == player && 
            board[col + 2][row + 2] == player && 
            board[col + 1][row + 1] == Mark.Empty
        ) {
            return true;
        }
        
        // Check diagonal stretch two (bottom-left to top-right)
        if (col - 2 >= 0 && row + 2 < board[0].length && 
            board[col][row] == player && 
            board[col - 2][row + 2] == player && 
            board[col - 1][row + 1] == Mark.Empty
        ) {
            return true;
        }
        
        return false;
    }

    /**
     * Determines if there is an open tria in the given row, column, or diagonal.
     * @param board The game board
     * @param col The row of the cell
     * @param row The column of the cell
     * @param player The player to check for
     * @return 1 if there is an open tria, 0 otherwise
     */
    public static boolean isOpenTria(Mark[][] board, int col, int row, Mark player) {
        // Check horizontal open tria
        if ((row > 0 && board[col][row - 1] == Mark.Empty) && // check not bound on left
            (row + 3 <= board[0].length || board[col][row + 3] == Mark.Empty) // check not bound on right
        ) {
            return true;
        }
        
        // Check vertical open tria
        if (col + 2 < board.length && 
            board[col][row] == player && 
            board[col + 1][row] == player && 
            board[col + 2][row] == player &&
            (col == 0 || board[col - 1][row] == Mark.Empty) && // check not bound on top
            (col + 3 >= board.length || board[col + 3][row] == Mark.Empty) // check not bound on bottom
        ) {
            return true;
        }

        // Check diagonal open tria (bottom-left to top-right)
        if (col + 2 < board.length && row + 2 < board[0].length &&
            board[col][row] == player && 
            board[col + 1][row + 1] == player && 
            board[col + 2][row + 2] == player &&
            (col == 0 || row == 0 || board[col - 1][row - 1] == Mark.Empty) && // check not bound on top-left
            (col + 3 >= board.length || row + 3 >= board[0].length || board[col + 3][row + 3] == Mark.Empty) // check not bound on bottom-right
        ) {
            return true;
        }
        
        // Check diagonal open tria (top-left to bottom-right)
        if (col - 2 >= 0 && row + 2 < board[0].length &&
            board[col][row] == player && 
            board[col - 1][row + 1] == player && 
            board[col - 2][row + 2] == player &&
            (col + 1 >= board.length || row == 0 || board[col + 1][row - 1] == Mark.Empty) && // check not bound on bottom-left
            (col - 3 < 0 || row + 3 >= board[0].length || board[col - 3][row + 3] == Mark.Empty) // check not bound on top-right
        ) {
            return true;
        }

        return false;
    }

    /**
     * Determines if there is an  tria in the given row, column, or diagonal.
     * @param board The game board
     * @param col The row of the cell
     * @param row The column of the cell
     * @param player The player to check for
     * @return true if there is a tria, false otherwise
     */
    public static boolean isTria(Mark[][] board, int col, int row, Mark player) {
        // Check horizontal tria
        if (row + 2 < board[0].length &&
                board[col][row] == player && // board[row][col] is the first stone
                board[col][row + 1] == player && // board[row][col + 1] is the second stone
                board[col][row + 2] == player && // board[row][col + 2] is the third stone
                (row > 0 && board[col][row - 1] == Mark.Empty) || // check left end is empty
                (row + 3 < board[0].length && board[col][row + 3] == Mark.Empty) // check right end is empty
        ) {
            return true;
        }

        // Check vertical tria
        if (col + 2 < board.length &&
                board[col][row] == player &&
                board[col + 1][row] == player &&
                board[col + 2][row] == player &&
                (col > 0 && 
                    board[col - 1][row] == Mark.Empty) || // check top end is empty
                (col + 3 < board.length && 
                    board[col + 3][row] == Mark.Empty) // check bottom end is empty
        ) {
            return true;
        }

        // Check diagonal tria (bottom-left to top-right)
        if (col + 2 < board.length && row + 2 < board[0].length &&
                board[col][row] == player &&
                board[col + 1][row + 1] == player &&
                board[col + 2][row + 2] == player &&
                (col > 0 && row > 0 && board[col - 1][row - 1] == Mark.Empty) || // check top-left end is empty
                (col + 3 < board.length && 
                    row + 3 < board[0].length && 
                    board[col + 3][row + 3] == Mark.Empty) //Check bottom-right end is empty
        ) {
            return true;
        }

        // Check diagonal tria (top-left to bottom-right)
        if (col - 2 >= 0 && row + 2 < board[0].length &&
                board[col][row] == player &&
                board[col - 1][row + 1] == player &&
                board[col - 2][row + 2] == player &&
                (col + 1 < board.length && row > 0 && 
                    board[col + 1][row - 1] == Mark.Empty) || // check bottom-left end
                (col - 3 >= 0 && row + 3 < board[0].length && 
                    board[col - 3][row + 3] == Mark.Empty) // check top-right
                                                                                                     // end is empty
        ) {
            return true;
        }

        return false;
    }

    /**
     * Determines if there is a stretch of three of the same player in the given row, column, or diagonal.
     * @param board The game board
     * @param col The row of the cell
     * @param row The column of the cell
     * @param player The player to check for
     * @return 1 if there is a stretch, 0 otherwise
     */
    public static boolean isStretchTria(Mark[][] board, int col, int row, Mark player) {
        // Check horizontal stretch tria (pair on left)
        if (row + 3 < board[0].length && 
            board[col][row] == player && 
            board[col][row + 1] == player && // pair
            board[col][row + 2] == Mark.Empty &&    // gap
            board[col][row + 3] == player
        ) { // single stone
            return true;
        }
        
        // Check horizontal stretch tria (pair on right)
        if (row + 3 < board[0].length && 
            board[col][row] == player &&     // single stone
            board[col][row + 1] == Mark.Empty &&    // gap
            board[col][row + 2] == player && // pair
            board[col][row + 3] == player
        ) {
            return true;
        }
        
        // Check vertical stretch tria (pair on top)
        if (col + 3 < board.length && 
            board[col][row] == player && 
            board[col + 1][row] == player && // pair
            board[col + 2][row] == Mark.Empty &&    // gap
            board[col + 3][row] == player
        ) { // single stone
            return true;
        }
        
        // Check vertical stretch tria (pair on bottom)
        if (col + 3 < board.length && 
            board[col][row] == player &&     // single stone
            board[col + 1][row] == Mark.Empty &&    // gap
            board[col + 2][row] == player && // pair
            board[col + 3][row] == player
        ) {
            return true;
        }
        
        // Check diagonal stretch tria (top-left to bottom-right, pair on top)
        if (col + 3 < board.length && row + 3 < board[0].length && 
            board[col][row] == player && 
            board[col + 1][row + 1] == player && // pair
            board[col + 2][row + 2] == Mark.Empty &&    // gap
            board[col + 3][row + 3] == player
        ) { // single stone
            return true;
        }
        
        // Check diagonal stretch tria (top-left to bottom-right, pair on bottom)
        if (col + 3 < board.length && row + 3 < board[0].length && 
            board[col][row] == player &&         // single stone
            board[col + 1][row + 1] == Mark.Empty &&    // gap
            board[col + 2][row + 2] == player && // pair
            board[col + 3][row + 3] == player
        ) {
            return true;
        }
        
        // Check diagonal stretch tria (bottom-left to top-right, pair on bottom)
        if (col >= 3 && row + 3 < board[0].length && 
            board[col][row] == player && 
            board[col - 1][row + 1] == player && // pair
            board[col - 2][row + 2] == Mark.Empty &&    // gap
            board[col - 3][row + 3] == player) { // single stone
            return true;
        }
        
        // Check diagonal stretch tria (bottom-left to top-right, pair on top)
        if (col >= 3 && row + 3 < board[0].length && 
            board[col][row] == player &&         // single stone
            board[col - 1][row + 1] == Mark.Empty &&    // gap
            board[col - 2][row + 2] == player && // pair
            board[col - 3][row + 3] == player
        ) {
            return true;
        }
        
        return false;
    }

    /**
     * Determines if there is an open tessera in the given row, column, or diagonal.
     * @param board The game board
     * @param col The row of the cell
     * @param row The column of the cell
     * @param player The player to check for
     * @return 1 if there is an open tessera, 0 otherwise
     */
    public static boolean isOpenTessera(Mark[][] board, int col, int row, Mark player) {
        // Check horizontal open tessera
        if (row + 3 < board[0].length && 
            board[col][row] == player && 
            board[col][row + 1] == player && 
            board[col][row + 2] == player && 
            board[col][row + 3] == player &&
            (row == 0 || board[col][row - 1] == Mark.Empty) && // check not bound on left
            (row + 4 >= board[0].length || board[col][row + 4] == Mark.Empty) // check not bound on right
        ) { 
            return true; 
        }

        // Check vertical open tessera
        if (col + 3 < board.length && 
            board[col][row] == player && 
            board[col + 1][row] == player && 
            board[col + 2][row] == player && 
            board[col + 3][row] == player &&
            (col == 0 || board[col - 1][row] == Mark.Empty) && // check not bound on top
            (col + 4 >= board.length || board[col + 4][row] == Mark.Empty)
        ) { // check not bound on bottom
            return true;
        }

        // Check diagonal open tessera (top-left to bottom-right)
        if (col + 3 < board.length && row + 3 < board[0].length &&
            board[col][row] == player && 
            board[col + 1][row + 1] == player && 
            board[col + 2][row + 2] == player && 
            board[col + 3][row + 3] == player &&
            (col == 0 || row == 0 || board[col - 1][row - 1] == Mark.Empty) && // check not bound on top-left
            (col + 4 >= board.length || row + 4 >= board[0].length || board[col + 4][row + 4] == Mark.Empty) // check not bound on bottom-right
        ) { 
            return true;
        }

        // Check diagonal open tessera (bottom-left to top-right)
        if (col - 3 >= 0 && row + 3 < board[0].length &&
            board[col][row] == player &&
            board[col - 1][row + 1] == player &&
            board[col - 2][row + 2] == player &&
            board[col - 3][row + 3] == player &&
            (col + 1 >= board.length || row == 0 || board[col + 1][row - 1] == Mark.Empty) && // check not bound on bottom-left
            (col - 4 < 0 || row + 4 >= board[0].length || board[col - 4][row + 4] == Mark.Empty) // check not bound on top-right
        ) { 
            return true;
        }

        return false;
    }

    /**
     * Checks if a move forms a tessera (four stones in a row) with at least one end
     * being empty.
     *
     * @param board  The game board represented as a 2D array.
     * @param col    The column index of the move.
     * @param row    The row index of the move.
     * @param player The player making the move.
     * @return true if the move forms a tessera with at least one end being empty,
     *         false otherwise.
     */
    public static boolean isTesseraWithEmptyEnd(Mark[][] board, int col, int row, Mark player) {
        // Check horizontal tessera
        if (row + 3 < board[0].length &&
                board[col][row] == player &&
                board[col][row + 1] == player &&
                board[col][row + 2] == player &&
                board[col][row + 3] == player &&
                ((row > 0 && board[col][row - 1] == Mark.Empty) || // check left end is empty
                        (row + 4 < board[0].length && board[col][row + 4] == Mark.Empty)) // check right end is empty
        ) {
            return true;
        }

        // Check vertical tessera
        if (col + 3 < board.length &&
                board[col][row] == player &&
                board[col + 1][row] == player &&
                board[col + 2][row] == player &&
                board[col + 3][row] == player &&
                ((col > 0 && board[col - 1][row] == Mark.Empty) || // check top end is empty
                        (col + 4 < board.length && board[col + 4][row] == Mark.Empty)) // check bottom end is empty
        ) {
            return true;
        }

        // Check diagonal tessera (bottom-left to top-right)
        if (col + 3 < board.length && row + 3 < board[0].length &&
                board[col][row] == player &&
                board[col + 1][row + 1] == player &&
                board[col + 2][row + 2] == player &&
                board[col + 3][row + 3] == player &&
                ((col > 0 && row > 0 && board[col - 1][row - 1] == Mark.Empty) || // check top-left end is empty
                        (col + 4 < board.length && row + 4 < board[0].length && board[col + 4][row + 4] == Mark.Empty)) // check bottom-right end is
                                                                                                                        // empty
        ) {
            return true;
        }

        // Check diagonal tessera (top-left to bottom-right)
        if (col - 3 >= 0 && row + 3 < board[0].length &&
                board[col][row] == player &&
                board[col - 1][row + 1] == player &&
                board[col - 2][row + 2] == player &&
                board[col - 3][row + 3] == player &&
                ((col + 1 < board.length && row > 0 && board[col + 1][row - 1] == Mark.Empty) || // check bottom-left end is empty
                        (col - 4 >= 0 && row + 4 < board[0].length && board[col - 4][row + 4] == Mark.Empty)) // check top-right end is empty
        ) {
            return true;
        }

        return false;
    }
}
