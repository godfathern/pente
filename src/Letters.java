public class Letters {
    public static int iAdvancedShape(Mark[][] board, int row, int col, Mark player) {
    // Check horizontal I shape
        if (col + 2 < board[0].length && 
            board[row][col] == player && 
            board[row][col + 2] == player && 
            board[row][col + 1] == null &&
            (col == 0 || board[row][col - 1] != player) && // unbound left
            (col + 3 >= board[0].length || board[row][col + 3] != player) && // unbound right
            // Check for L-shape potential (up or down)
            ((row > 0 && board[row - 1][col] == null && board[row - 1][col + 1] == null && board[row - 1][col + 2] == null) ||
            (row < board.length - 1 && board[row + 1][col] == null && board[row + 1][col + 1] == null && board[row + 1][col + 2] == null))) {
            return 1;
        }
        
        // Check vertical I shape
        if (row + 2 < board.length && 
            board[row][col] == player && 
            board[row + 2][col] == player && 
            board[row + 1][col] == null &&
            (row == 0 || board[row - 1][col] != player) && // unbound top
            (row + 3 >= board.length || board[row + 3][col] != player) && // unbound bottom
            // Check for L-shape potential (left or right)
            ((col > 0 && board[row][col - 1] == null && board[row + 1][col - 1] == null && board[row + 2][col - 1] == null) ||
            (col < board[0].length - 1 && board[row][col + 1] == null && board[row + 1][col + 1] == null && board[row + 2][col + 1] == null))) {
            return 1;
        }

        // Check diagonal I shape (up-right or down-left)
        if (row + 2 < board.length && col + 2 < board[0].length && 
            board[row][col] == player && 
            board[row + 2][col + 2] == player && 
            board[row + 1][col + 1] == null &&
            (row == 0 || col == 0 || board[row - 1][col - 1] != player) && // unbound top left
            (row + 3 >= board.length || col + 3 >= board[0].length || board[row + 3][col + 3] != player) && // unbound bottom right
            // Check for L-shape potential (up-left or down-right)
            ((row > 0 && col > 0 && board[row - 1][col - 1] == null && board[row + 1][col + 1] == null) ||
            (row < board.length - 1 && col < board[0].length - 1 && board[row + 3][col + 3] == null && board[row + 1][col + 1] == null))) {
            return 1;
        }

        // Check diagonal I shape (up-left or down-right)
        if (row + 2 < board.length && col - 2 >= 0 && 
            board[row][col] == player && 
            board[row + 2][col - 2] == player && 
            board[row + 1][col - 1] == null &&
            (row == 0 || col == board[0].length - 1 || board[row - 1][col + 1] != player) && // unbound top right
            (row + 3 >= board.length || col - 3 < 0 || board[row + 3][col - 3] != player) && // unbound bottom left
            // Check for L-shape potential (up-right or down-left)
            ((row > 0 && col < board[0].length - 1 && board[row - 1][col + 1] == null && board[row + 1][col - 1] == null) ||
            (row < board.length - 1 && col > 0 && board[row + 3][col - 3] == null && board[row + 1][col - 1] == null))) {
            return 1;
        }
        return 0;
    }

    public static int lAdvancedShape(Mark[][] board, int row, int col, Mark player) {
        // Check horizontal L shape
        if (col + 2 < board[0].length && 
            board[row][col] == player && 
            board[row][col + 2] == player && 
            board[row][col + 1] == null &&
            (col == 0 || board[row][col - 1] != player) && // unbound left
            (col + 3 >= board[0].length || board[row][col + 3] != player) && // unbound right
            // Check for I-shape potential (up or down)
            ((row > 0 && board[row - 1][col] == null && board[row - 1][col + 1] == null && board[row - 1][col + 2] == null) ||
            (row < board.length - 1 && board[row + 1][col] == null && board[row + 1][col + 1] == null && board[row + 1][col + 2] == null))) {
            return 1;
        }
        
        // Check vertical L shape
        if (row + 2 < board.length && 
            board[row][col] == player && 
            board[row + 2][col] == player && 
            board[row + 1][col] == null &&
            (row == 0 || board[row - 1][col] != player) && // unbound top
            (row + 3 >= board.length || board[row + 3][col] != player) && // unbound bottom
            // Check for I-shape potential (left or right)
            ((col > 0 && board[row][col - 1] == null && board[row + 1][col - 1] == null && board[row + 2][col - 1] == null) ||
            (col < board[0].length - 1 && board[row][col + 1] == null && board[row + 1][col + 1] == null && board[row + 2][col + 1] == null))) {
            return 1;
        }

        // Check diagonal L shape (up-right or down-left)
        if (row + 2 < board.length && col + 2 < board[0].length && 
            board[row][col] == player && 
            board[row + 2][col + 2] == player && 
            board[row + 1][col + 1] == null &&
            (row == 0 || col == 0 || board[row - 1][col - 1] != player) && // unbound top left
            (row + 3 >= board.length || col + 3 >= board[0].length || board[row + 3][col + 3] != player) && // unbound bottom right
            // Check for I-shape potential (up-left or down-right)
            ((row > 0 && col > 0 && board[row - 1][col - 1] == null && board[row + 1][col + 1] == null) ||
            (row < board.length - 1 && col < board[0].length - 1 && board[row + 3][col + 3] == null && board[row + 1][col + 1] == null))) {
            return 1;
        }

        // Check diagonal L shape (up-left or down-right)
        if (row + 2 < board.length && col - 2 >= 0 && 
            board[row][col] == player && 
            board[row + 2][col - 2] == player && 
            board[row + 1][col - 1] == null &&
            (row == 0 || col == board[0].length - 1 || board[row - 1][col + 1] != player) && // unbound top right
            (row + 3 >= board.length || col - 3 < 0 || board[row + 3][col - 3] != player) && // unbound bottom left
            // Check for I-shape potential (up-right or down-left)
            ((row > 0 && col < board[0].length - 1 && board[row - 1][col + 1] == null && board[row + 1][col - 1] == null) ||
            (row < board.length - 1 && col > 0 && board[row + 3][col - 3] == null && board[row + 1][col - 1] == null))) {
            return 1;
        }
        return 0;
    }
}
