public class Letters {
    public static int iAdvancedShape(Mark[][] board, int row, int col, Mark player) {
    // Check horizontal I shape
        if (col + 2 < board[0].length && 
            board[row][col] == player && 
            board[row][col + 2] == player && 
            board[row][col + 1] == Mark.Empty &&
            (col == 0 || board[row][col - 1] != player) && // unbound left
            (col + 3 >= board[0].length || board[row][col + 3] != player) && // unbound right
            // Check for L-shape potential (up or down)
            ((row > 0 && board[row - 1][col] == Mark.Empty && board[row - 1][col + 1] == Mark.Empty && board[row - 1][col + 2] == Mark.Empty) ||
            (row < board.length - 1 && board[row + 1][col] == Mark.Empty && board[row + 1][col + 1] == Mark.Empty && board[row + 1][col + 2] == Mark.Empty))) {
            return 1;
        }
        
        // Check vertical I shape
        if (row + 2 < board.length && 
            board[row][col] == player && 
            board[row + 2][col] == player && 
            board[row + 1][col] == Mark.Empty &&
            (row == 0 || board[row - 1][col] != player) && // unbound top
            (row + 3 >= board.length || board[row + 3][col] != player) && // unbound bottom
            // Check for L-shape potential (left or right)
            ((col > 0 && board[row][col - 1] == Mark.Empty && board[row + 1][col - 1] == Mark.Empty && board[row + 2][col - 1] == Mark.Empty) ||
            (col < board[0].length - 1 && board[row][col + 1] == Mark.Empty && board[row + 1][col + 1] == Mark.Empty && board[row + 2][col + 1] == Mark.Empty))) {
            return 1;
        }

        // Check diagonal I shape (up-right or down-left)
        if (row + 2 < board.length && col + 2 < board[0].length && 
            board[row][col] == player && 
            board[row + 2][col + 2] == player && 
            board[row + 1][col + 1] == Mark.Empty &&
            (row == 0 || col == 0 || board[row - 1][col - 1] != player) && // unbound top left
            (row + 3 >= board.length || col + 3 >= board[0].length || board[row + 3][col + 3] != player) && // unbound bottom right
            // Check for L-shape potential (up-left or down-right)
            ((row > 0 && col > 0 && board[row - 1][col - 1] == Mark.Empty && board[row + 1][col + 1] == Mark.Empty) ||
            (row < board.length - 1 && col < board[0].length - 1 && board[row + 3][col + 3] == Mark.Empty && board[row + 1][col + 1] == Mark.Empty))) {
            return 1;
        }

        // Check diagonal I shape (up-left or down-right)
        if (row + 2 < board.length && col - 2 >= 0 && 
            board[row][col] == player && 
            board[row + 2][col - 2] == player && 
            board[row + 1][col - 1] == Mark.Empty &&
            (row == 0 || col == board[0].length - 1 || board[row - 1][col + 1] != player) && // unbound top right
            (row + 3 >= board.length || col - 3 < 0 || board[row + 3][col - 3] != player) && // unbound bottom left
            // Check for L-shape potential (up-right or down-left)
            ((row > 0 && col < board[0].length - 1 && board[row - 1][col + 1] == Mark.Empty && board[row + 1][col - 1] == Mark.Empty) ||
            (row < board.length - 1 && col > 0 && board[row + 3][col - 3] == Mark.Empty && board[row + 1][col - 1] == Mark.Empty))) {
            return 1;
        }
        return 0;
    }

    public static int lAdvancedShape(Mark[][] board, int row, int col, Mark player) {
        // Check horizontal L shape
        if (col + 2 < board[0].length && 
            board[row][col] == player && 
            board[row][col + 2] == player && 
            board[row][col + 1] == Mark.Empty &&
            (col == 0 || board[row][col - 1] != player) && // unbound left
            (col + 3 >= board[0].length || board[row][col + 3] != player) && // unbound right
            // Check for I-shape potential (up or down)
            ((row > 0 && board[row - 1][col] == Mark.Empty && board[row - 1][col + 1] == Mark.Empty && board[row - 1][col + 2] == Mark.Empty) ||
            (row < board.length - 1 && board[row + 1][col] == Mark.Empty && board[row + 1][col + 1] == Mark.Empty && board[row + 1][col + 2] == Mark.Empty))) {
            return 1;
        }
        
        // Check vertical L shape
        if (row + 2 < board.length && 
            board[row][col] == player && 
            board[row + 2][col] == player && 
            board[row + 1][col] == Mark.Empty &&
            (row == 0 || board[row - 1][col] != player) && // unbound top
            (row + 3 >= board.length || board[row + 3][col] != player) && // unbound bottom
            // Check for I-shape potential (left or right)
            ((col > 0 && board[row][col - 1] == Mark.Empty && board[row + 1][col - 1] == Mark.Empty && board[row + 2][col - 1] == Mark.Empty) ||
            (col < board[0].length - 1 && board[row][col + 1] == Mark.Empty && board[row + 1][col + 1] == Mark.Empty && board[row + 2][col + 1] == Mark.Empty))) {
            return 1;
        }

        // Check diagonal L shape (up-right or down-left)
        if (row + 2 < board.length && col + 2 < board[0].length && 
            board[row][col] == player && 
            board[row + 2][col + 2] == player && 
            board[row + 1][col + 1] == Mark.Empty &&
            (row == 0 || col == 0 || board[row - 1][col - 1] != player) && // unbound top left
            (row + 3 >= board.length || col + 3 >= board[0].length || board[row + 3][col + 3] != player) && // unbound bottom right
            // Check for I-shape potential (up-left or down-right)
            ((row > 0 && col > 0 && board[row - 1][col - 1] == Mark.Empty && board[row + 1][col + 1] == Mark.Empty) ||
            (row < board.length - 1 && col < board[0].length - 1 && board[row + 3][col + 3] == Mark.Empty && board[row + 1][col + 1] == Mark.Empty))) {
            return 1;
        }

        // Check diagonal L shape (up-left or down-right)
        if (row + 2 < board.length && col - 2 >= 0 && 
            board[row][col] == player && 
            board[row + 2][col - 2] == player && 
            board[row + 1][col - 1] == Mark.Empty &&
            (row == 0 || col == board[0].length - 1 || board[row - 1][col + 1] != player) && // unbound top right
            (row + 3 >= board.length || col - 3 < 0 || board[row + 3][col - 3] != player) && // unbound bottom left
            // Check for I-shape potential (up-right or down-left)
            ((row > 0 && col < board[0].length - 1 && board[row - 1][col + 1] == Mark.Empty && board[row + 1][col - 1] == Mark.Empty) ||
            (row < board.length - 1 && col > 0 && board[row + 3][col - 3] == Mark.Empty && board[row + 1][col - 1] == Mark.Empty))) {
            return 1;
        }
        return 0;
    }
    public static int hLowerAdvancedShape(Mark[][] board, int row, int col, Mark player) {
        // Check horizontal H shape
        if (row + 2 < board.length && col + 2 < board[0].length &&
            // check 2 vertical lines
            board[row][col] == player &&
            board[row + 1][col] == player &&
            board[row + 2][col] == player && 
            board[row][col + 2] == player &&
            board[row + 1][col + 2] == player &&
            board[row + 2][col + 2] == player &&
            // check middle connector line
            board[row + 1][col + 1] == Mark.Empty &&
            // check empty spaces for threat reaction
            board[row][col + 1] == Mark.Empty &&
            board[row + 2][col + 1] == Mark.Empty &&
            // check edges which should be unbound
            (row == 0 || board[row - 1][col + 1] != player) && // unbound top
            (row == 0 || board[row - 1][col + 2] != player) && // unbound top right
            (row + 3 >= board.length || board[row + 3][col] != player) && // unbound bottom
            (row + 3 >= board.length || board[row + 3][col + 2] != player) && // unbound bottom right
            (col == 0 || board[row + 1][col - 1] != player) && // unbound left
            (col + 3 >= board[0].length || board[row + 1][col + 3] != player)) // unbound right
        {
            return 2; // higher score for H shape because of threat reaction
        }
        return 0;
    }
    
    public static int xAdvancedShape(Mark[][] board, int row, int col, Mark player) {
        // Check space for X shape
        if (row + 2 < board.length && col + 2 < board[0].length) {
            // Core check for X pattern
            if (board[row][col] == player &&
                board[row + 2][col] == player &&
                board[row][col + 2] == player &&
                board[row + 2][col + 2] == player &&
                board[row + 1][col + 1] == player &&
                // Check for empty spaces
                board[row + 1][col] == Mark.Empty &&
                board[row + 1][col + 2] == Mark.Empty &&
                board[row][col + 1] == Mark.Empty &&
                board[row + 2][col + 1] == Mark.Empty &&
                // Check for unbound edges
                (row == 0 || board[row - 1][col + 1] != player) && // unbound top
                (row + 3 >= board.length || board[row + 3][col + 1] != player) && // unbound bottom
                (col == 0 || board[row - 1][col - 1] != player) && // unbound top left
                (col + 3 >= board[0].length || board[row + 1][col + 3] != player))
                {
                    return 3; // highest score for X shape because of threat reaction + H potential, which can potentially lead to a win
                }
        }
        return 0;
    }
    public static int hHigherAdvancedShape(Mark[][] board, int row, int col, Mark player) {
        // Check if we have enough space for H pattern
        if (row + 4 < board.length && col + 4 < board[0].length) {
            // Check base H pattern
            if (board[row][col] == player &&
                board[row + 4][col] == player &&
                board[row][col + 4] == player &&
                board[row + 4][col + 4] == player &&
                board[row + 2][col + 2] == player &&
                
                // Vertical connectors must be present
                board[row + 1][col] == player &&
                board[row + 3][col] == player &&
                board[row + 1][col + 4] == player &&
                board[row + 3][col + 4] == player &&
                
                // Horizontal connector positions must be empty for tria threats
                board[row + 2][col + 1] == Mark.Empty &&
                board[row + 2][col + 3] == Mark.Empty &&
                
                // Middle spaces must be empty for double tria potential
                board[row + 1][col + 2] == Mark.Empty &&
                board[row + 3][col + 2] == Mark.Empty &&
                
                // Verify unbound edges for tessera formation
                (row == 0 || board[row - 1][col] != player) &&
                (row + 5 >= board.length || board[row + 5][col] != player) &&
                (col == 0 || board[row + 2][col - 1] != player) &&
                (col + 5 >= board[0].length || board[row + 2][col + 5] != player)) {
                return 4; // Highest threat score due to double tria and tessera potential
            }
        }
        return 0;
    }

    public static int fourByThreeTriangle(Mark[][] board, int row, int col, Mark player) {
        // Check horizontal base case (4 wide x 3 high)
        if (row + 2 < board.length && col + 3 < board[0].length) {
            // Check base triangle pattern
            if (board[row][col] == player &&
                board[row][col + 3] == player &&
                board[row + 2][col] == player &&
                
                // Verify required empty spaces
                board[row][col + 1] == Mark.Empty &&
                board[row][col + 2] == Mark.Empty &&
                board[row + 1][col] == Mark.Empty &&
                
                // Check spaces for tria and stretch tria potential
                board[row + 1][col + 1] == Mark.Empty &&
                board[row + 1][col + 2] == Mark.Empty &&
                board[row + 2][col + 1] == Mark.Empty &&
                board[row + 2][col + 2] == Mark.Empty &&
                
                // Verify unbound edges for H shape potential
                (row == 0 || board[row - 1][col] != player) &&
                (row == 0 || board[row - 1][col + 3] != player) &&
                (col == 0 || board[row][col - 1] != player) &&
                (col + 4 >= board[0].length || board[row][col + 4] != player))
            {
                return 2; // High threat score due to forced response and H potential
            }
        }
        return 0;
    }
    public static int fiveByThreeTriangle(Mark[][] board, int row, int col, Mark player){
        // Check dimensions for 5x3 triangle
        if (row + 2 < board.length && col + 4 < board[0].length) {
            // Check base triangle pattern
            if (board[row][col] == player &&
                board[row][col + 4] == player &&
                board[row + 2][col] == player &&
                
                // Verify empty spaces for stretch twos
                board[row][col + 1] == Mark.Empty &&
                board[row][col + 2] == Mark.Empty &&
                board[row][col + 3] == Mark.Empty &&
                board[row + 1][col] == Mark.Empty &&
                
                // Check spaces for tria threat potential
                board[row + 1][col + 1] == Mark.Empty &&
                board[row + 1][col + 2] == Mark.Empty &&
                board[row + 1][col + 3] == Mark.Empty &&
                board[row + 2][col + 1] == Mark.Empty &&
                board[row + 2][col + 2] == Mark.Empty &&
                
                // Verify unbound edges
                (row == 0 || board[row - 1][col] != player) &&
                (row == 0 || board[row - 1][col + 4] != player) &&
                (col == 0 || board[row][col - 1] != player) &&
                (col + 5 >= board[0].length || board[row][col + 5] != player)) {
                
                return 2; // Threat score for stretch two potential
            }
        }
        return 0;
    }

    public static int hatLetter(Mark[][] board, int row, int col, Mark player) {
        // Check dimensions for hat shape
        if (row + 3 < board.length && col + 3 < board[0].length) {
            // Check base scalene triangle pattern
            if (board[row][col] == player &&
                board[row][col + 3] == player &&
                board[row + 2][col + 1] == player &&
                
                // Verify empty spaces for stretch tria
                board[row][col + 1] == Mark.Empty &&
                board[row][col + 2] == Mark.Empty &&
                board[row + 1][col + 1] == Mark.Empty &&
                
                // Check spaces for X shape potential
                board[row + 1][col + 2] == Mark.Empty &&
                board[row + 2][col + 2] == Mark.Empty &&
                board[row + 2][col] == Mark.Empty &&
                
                // Verify unbound edges
                (row == 0 || board[row - 1][col] != player) &&
                (row == 0 || board[row - 1][col + 3] != player) &&
                (col == 0 || board[row][col - 1] != player) &&
                (col + 4 >= board[0].length || board[row][col + 4] != player)) {
                
                return 2; // Threat score for stretch tria and X formation potential
            }
        }
        return 0;
    }
}
