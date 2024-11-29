import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EvalBoard {
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

    public static int amountOfMovesAnalyzed = 5;
    public int[] DScore = new int[] { 0, 1, 9, 81, 729 };
    public int[] AScore = new int[] { 0, 2, 18, 162, 1458 };


    public String[] liveFour = {"11011", "10111", "11101", "11110", "01111"}; //500
    public String[] liveThree = { "010110", "011010", "011100", "001110" }; // 120
    public String[] capture = {"1220", "0221"}; //100
    public String[] liveTwo = {"0110", "0101","1100","1010"}; // 50
    public String[] liveOne = {"012", "021", "120","210"}; // 10
    



    int [][] board;


    public EvalBoard(int size){
        this.board = new int[size][size];
    }
    public void play(int x, int y, int player){
        board[x][y] = player;
    }
    public void undo(int x, int y){
        board[x][y] = 0;
    }
    public void addPoint(int x, int y, int point){
        board[x][y] += point;
    }

    //Ditmemay :
    public void evalBoard(int player) {
        evalHorizontal(player);
        evalVertical(player);
        evalDiagonalLeftToRight(player);
        evalDiagonalRightToLeft(player);
    }
    
    private void evalHorizontal(int player) {
        for (int i = 0; i < board.length; i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < board[0].length; j++) {
                s.append(board[i][j] == player ? "1" : board[i][j] == 0 ? "0" : "2");
            }
            checkPattern(s.toString(), liveFour, 500, i, 0, true, false,false,false);
            checkPattern(s.toString(), liveThree, 120, i, 0, true, false,false,false);
            checkPattern(s.toString(), capture, 100, i, 0, true, false,false,false);
            checkPattern(s.toString(), liveTwo, 50, i, 0, true, false,false,false);
            checkPattern(s.toString(), liveOne, 10, i, 0, true, false,false,false);
        }
    }
    
    private void evalVertical(int player) {
        for (int j = 0; j < board[0].length; j++) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < board.length; i++) {
                s.append(board[i][j] == player ? "1" : board[i][j] == 0 ? "0" : "2");
            }
            checkPattern(s.toString(), liveFour, 500, 0, j, false, true,false,false);
            checkPattern(s.toString(), liveThree, 120, 0, j, false, true,false,false);
            checkPattern(s.toString(), capture, 100, 0, j, false, true,false,false);
            checkPattern(s.toString(), liveTwo, 50, 0, j, false, true,false,false);
            checkPattern(s.toString(), liveOne, 10, 0, j, false, true,false,false);
        }
    }
    
    private void evalDiagonalLeftToRight(int player) {
        int rows = board.length;
        int cols = board[0].length;
    
            for (int startCol = 0; startCol < cols; startCol++) {
                StringBuilder diagonal = new StringBuilder();
                int r = 0, c = startCol;
                while (r< rows && c< cols) {
                    diagonal.append(board[r][c] == player ? "1" : board[r][c] == 0 ? "0" : "2");
                    r++;
                    c++;

                }
                System.out.println(diagonal.toString());
                if (diagonal.length() >= 3) {
                    checkPattern(diagonal.toString(), liveFour, 500, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), liveThree, 120, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), capture, 100, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), liveTwo, 50, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), liveOne, 10, r-1, c-1, false, false,true,false);
                }
            }
        
            for (int startRow = 1; startRow < rows; startRow++) {
                StringBuilder diagonal = new StringBuilder();
                int r = startRow, c = 0;
                while (r< rows && c< cols) {
                    diagonal.append(board[r][c] == player ? "1" : board[r][c] == 0 ? "0" : "2");
                    r++;
                    c++;

                }
                //System.out.println(diagonal.toString());
                if (diagonal.length() >= 3) {
                    checkPattern(diagonal.toString(), liveFour, 500, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), liveThree, 120, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), capture, 100, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), liveTwo, 50, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), liveOne, 10, r-1, c-1, false, false,true,false);
                }
            }
    }
    
    private void evalDiagonalRightToLeft(int player) {
        int rows = board.length;
        int cols = board[0].length;
    
        // Diagonals starting from the top row (decreasing column)
        for (int startCol = cols - 1; startCol >= 0; startCol--) {
            StringBuilder diagonal = new StringBuilder();
            int r = 0, c = startCol;
            while (r < rows && c >= 0) {
                diagonal.append(board[r][c] == player ? "1" : board[r][c] == 0 ? "0" : "2");
                r++;
                c--;
            }
            System.out.println(diagonal.toString());
            if (diagonal.length() >= 3) {
                checkPattern(diagonal.toString(), liveFour, 500, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), liveThree, 120, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), capture, 100, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), liveTwo, 50, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), liveOne, 10, r - 1, c + 1, false, false,false,true);
            }
        }
    
        // Diagonals starting from the leftmost column (excluding top row)
        for (int startRow = 1; startRow < rows; startRow++) {
            StringBuilder diagonal = new StringBuilder();
            int r = startRow, c = cols - 1;
            while (r < rows && c >= 0) {
                diagonal.append(board[r][c] == player ? "1" : board[r][c] == 0 ? "0" : "2");
                r++;
                c--;
            }
            System.out.println(diagonal.toString());
            if (diagonal.length() >= 3) {
                checkPattern(diagonal.toString(), liveFour, 500, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), liveThree, 120, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), capture, 100, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), liveTwo, 50, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), liveOne, 50, r - 1, c + 1, false, false,false,true);
            }
        }
    }
    

    
    private void checkPattern(String lineString, String[] patterns, int score, int baseRow, int baseCol, boolean isHorizontal, boolean isVertical
    , boolean isDiagonalLeftToRight, boolean isDiagonalRightToLeft) {
        for (String pattern : patterns) {
            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(lineString);
            while (matcher.find()) {
                System.out.println("Pattern found: " + pattern);
                System.out.println("Base row: " + baseRow + " Base col: " + baseCol);
                System.out.println("Start: " + matcher.start() + " End: " + matcher.end());
                for (int k = matcher.start(); k < matcher.end(); k++) {
                    if (isHorizontal && board[baseRow][k] != 1 && board[baseRow][k] != 2) {
                        this.addPoint(baseRow, k, score);
                    } else if (isVertical && board[k][baseCol] != 1 && board[k][baseCol] != 2) {
                        this.addPoint(k, baseCol, score);
                    } else if (isDiagonalLeftToRight) {
                        if (baseRow >= baseCol){
                            int row = baseRow - baseCol + k;
                            int col = k;
                            System.out.println("row: " + row + " col: " + col);
                            if (board[row][col] != 1 && board[row][col] != 2)
                            this.addPoint(row, col, score);
                        }
                        else{
                            int row = k;
                            int col = baseCol - baseRow + k;
                            System.out.println("row: " + row + " col: " + col);
                            if (board[row][col] != 1 && board[row][col] != 2)
                            this.addPoint(row, col, score);
                        }
                    }
                    else if (isDiagonalRightToLeft) {
                        if (baseCol <15){
                            int row = baseCol + k;
                            int col = baseRow - k;
                            if (board[row][col] != 1 && board[row][col] != 2)
                            this.addPoint(row, col, score);
                        }
                    }
                }
            }
        }
    }
    
    
    public void display() {
        System.out.println("Board:");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.printf("%8d", board[i][j]);
            }
            System.out.println();
            System.out.print("\n");
        }
    }

}
