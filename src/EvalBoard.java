import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EvalBoard {

    //Im Pep Guardiola, I have the ball, the opponent has the ball, the ball is the most important thing in the world
    public String[] liveFour = {"11011", "10111", "11101", "11110", "01111"}; //410
    public String[] liveThree = { "010110", "011010", "011100", "001110" }; // 310
    public String[] deadThree = {"211100", "211010", "210110", "010112"}; // 290
    public String[] capture = {"1220", "0221"}; //250
    public String[] liveTwo = {"0110", "0101","1100","1010"}; // 200
    public String[] liveOne = {"012", "021", "120","210","02"}; // 10

    //Im Jose Mourinho
    public String[] defThree = {"02220", "02202", "02022", "20220", "20202", "20022"}; // 300

    //Score for each pattern : 
    int liveFourScore = 410;
    int liveThreeScore = 310;
    int deadThreeScore = 290;
    int captureScore = 250;
    int liveTwoScore = 200;
    int liveOneScore = 10;
    int defThreeScore = 300;
    

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
    public void addPoint(int x, int y, int score){
        board[x][y] += score;
    }
    public void reset(){
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[i].length; j++){
                if (board[i][j] != 1 && board[i][j] != 2){
                    board[i][j] = 0;
                }
            }
        }
    }

    //Ditmemay :

    public String bestMove() {
        this.evaluateBoard();
        int maxScore = Integer.MIN_VALUE;
        ArrayList<int[]> candidates = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] > maxScore) {
                    maxScore = board[i][j];
                    candidates.clear();
                    candidates.add(new int[]{i, j});
                } else if (board[i][j] == maxScore) {
                    candidates.add(new int[]{i, j});
                }
            }
        }
        int[] bestMove = candidates.get(random.nextInt(candidates.size()));
        System.out.println("maxScore: " + maxScore);
        this.display();
        this.play(bestMove[0], bestMove[1], 1);
        this.display();
        this.reset();
        this.display();
        return CoordinateConverter.coordinateToString(bestMove[0], bestMove[1]);
    }


    public void evaluateBoard() {
        evalHorizontal(1);
        evalVertical(1);
        evalDiagonalLeftToRight(1);
        evalDiagonalRightToLeft(1);
    }
    
    private void evalHorizontal(int player) {
        for (int i = 0; i < board.length; i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < board[0].length; j++) {
                s.append(board[i][j] == player ? "1" : board[i][j] == 0 ? "0" : "2");
            }
            checkPattern(s.toString(), liveFour, liveFourScore, i, 0, true, false,false,false);
            checkPattern(s.toString(), liveThree, liveThreeScore, i, 0, true, false,false,false);
            checkPattern(s.toString(), deadThree, deadThreeScore, i,0, false, true,false,false);
            checkPattern(s.toString(), capture, captureScore, i, 0, true, false,false,false);
            checkPattern(s.toString(), liveTwo, liveTwoScore, i, 0, true, false,false,false);
            checkPattern(s.toString(), liveOne, liveOneScore, i, 0, true, false,false,false);
            checkPattern(s.toString(), defThree, defThreeScore, i, 0, true, false,false,false);
        }
    }
    
    private void evalVertical(int player) {
        for (int j = 0; j < board[0].length; j++) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < board.length; i++) {
                s.append(board[i][j] == player ? "1" : board[i][j] == 0 ? "0" : "2");
            }
            checkPattern(s.toString(), liveFour, liveFourScore, 0, j, false, true,false,false);
            checkPattern(s.toString(), liveThree, liveThreeScore, 0, j, false, true,false,false);
            checkPattern(s.toString(), deadThree, deadThreeScore, 0, j, false, true,false,false);
            checkPattern(s.toString(), capture, captureScore, 0, j, false, true,false,false);
            checkPattern(s.toString(), liveTwo, liveTwoScore, 0, j, false, true,false,false);
            checkPattern(s.toString(), liveOne, liveOneScore, 0, j, false, true,false,false);
            checkPattern(s.toString(), defThree, defThreeScore, 0, j, false, true,false,false);
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
                    checkPattern(diagonal.toString(), liveFour, liveFourScore, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), liveThree, liveThreeScore, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), deadThree, deadThreeScore, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), capture, captureScore, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), liveTwo, liveTwoScore, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), liveOne, liveOneScore, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), defThree, defThreeScore, r-1, c-1, false, false,true,false);
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
                    checkPattern(diagonal.toString(), liveFour, liveFourScore, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), liveThree, liveThreeScore, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), deadThree, deadThreeScore, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), capture, captureScore, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), liveTwo, liveTwoScore, r-1, c-1, false, false,true,false);
                    checkPattern(diagonal.toString(), liveOne, liveOneScore, r-1, c-1, false, false,true,false);
                }   checkPattern(diagonal.toString(), defThree, defThreeScore, r-1, c-1, false, false,true,false);
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
                checkPattern(diagonal.toString(), liveFour, liveFourScore, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), liveThree, liveThreeScore, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), deadThree, deadThreeScore, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), capture, captureScore, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), liveTwo, liveTwoScore, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), liveOne, liveOneScore, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), defThree, defThreeScore, r - 1, c + 1, false, false,false,true);
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
                checkPattern(diagonal.toString(), liveFour, liveFourScore, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), liveThree, liveThreeScore, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), deadThree, deadThreeScore, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), capture, captureScore, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), liveTwo, liveTwoScore, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), liveOne, liveOneScore, r - 1, c + 1, false, false,false,true);
                checkPattern(diagonal.toString(), defThree, defThreeScore, r - 1, c + 1, false, false,false,true);
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
