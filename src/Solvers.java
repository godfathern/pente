public class Solvers {
    public static int detectWedge(Mark[][] board, int row, int col, Mark player) {
        // Look in all 8 directions for wedge pattern
        int[][] directions = {{0,1}, {1,0}, {1,1}, {1,-1}, {0,-1}, {-1,0}, {-1,-1}, {-1,1}};
        
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
            
            if (isValidPosition(board, r + dir[0], c + dir[1]) && 
                isValidPosition(board, r - dir[0], c - dir[1])) {
                    
                // Check for stretch two pattern
                if (board[r][c] == player &&
                    board[r + dir[0]][c + dir[1]] == null &&
                    board[r + 2*dir[0]][c + 2*dir[1]] == player &&
                    // Check for potential pair formation
                    board[r - dir[0]][c - dir[1]] == null &&
                    // Verify capture opportunity exists
                    isCapturePossible(board, r, c, dir[0], dir[1], player)) {
                    return 2;
                }
            }
        }
        return 0;
    }

    public static int detectExtension(Mark[][] board, int row, int col, Mark player) {
        // Look in all 4 directions for stretch patterns
        int[][] directions = {{0,1}, {1,0}, {1,1}, {1,-1}};
        
        for (int[] dir : directions) {
            // Check for stretch tessera
            if (isValidPosition(board, row + 3*dir[0], col + 3*dir[1])) {
                if (board[row][col] == player &&
                    board[row + dir[0]][col + dir[1]] == player &&
                    board[row + 2*dir[0]][col + 2*dir[1]] == player &&
                    board[row + 3*dir[0]][col + 3*dir[1]] == null &&
                    board[row + 4*dir[0]][col + 4*dir[1]] == player &&
                    // Check for adjacent capture potential
                    hasAdjacentCapturePotential(board, row, col, dir[0], dir[1], player)) {
                    return 3;
                }
            }
        }
        return 0;
    }

    private static boolean isValidPosition(Mark[][] board, int row, int col) {
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }

    private static boolean isCapturePossible(Mark[][] board, int row, int col, int dx, int dy, Mark player) {
        // Check perpendicular directions for capture setup
        int[][] perpDirs = {{-dy,dx}, {dy,-dx}};
        
        for (int[] dir : perpDirs) {
            if (isValidPosition(board, row + 2*dir[0], col + 2*dir[1]) &&
                board[row + dir[0]][col + dir[1]] == player.getOpponent() &&
                board[row + 2*dir[0]][col + 2*dir[1]] == player.getOpponent()) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasAdjacentCapturePotential(Mark[][] board, int row, int col, int dx, int dy, Mark player) {
        // Check adjacent positions for potential pair formation
        int[][] adjacentDirs = {{-dy,dx}, {dy,-dx}};
        
        for (int[] dir : adjacentDirs) {
            if (isValidPosition(board, row + dir[0], col + dir[1]) &&
                board[row + dir[0]][col + dir[1]] == player.getOpponent()) {
                return true;
            }
        }
        return false;
    }
}
