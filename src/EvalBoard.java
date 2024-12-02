import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EvalBoard {

    private int totalScoreBlack = 0;
    private int totalScoreRed = 0;
    
    // Im Pep Guardiola, I have the ball, the opponent has the ball, the ball is the
    // most important thing in the world
    private static HashMap<Integer, String[]> patterns;

    public String[] win = { "11111" };
    public String[] liveFourWedge = { "11011", "10111", "11101"};
    public String[] liveFour = { "11110", "01111" };
    public String[] liveThreeWedgeFree = { "010110", "011010" };
    public String[] liveThreeWedge = { "1011", "1101" };
    public String[] liveThreeFree = { "001110", "011100" };
    public String[] liveThree = { "01110", "01110" };
    public String[] deadThreeWedge = { "211010", "210110", "010112" };
    public String[] deadThree = { "211100", "001112" };
    public String[] liveTwoWedgeFree = { "0101", "1010" };
    public String[] liveTwoWedge = { "101", "101" };
    public String[] liveTwoFree = { "0110", "1100"};
    public String[] liveTwo = { "011", "110"};
    public String[] liveOne = { "02" };
    
    // Waste patterns
    public String[] waste = { "021", "120" };
    
    // Self burn patterns
    public String[] selfCapture = { "012", "210" };
    public String[] getCaptured = { "0112", "2110" };
    
    // Defensive patterns    
    public String[] defDeadThreeWedge = { "122020", "120220", "020221" };
    public String[] defDeadThree = { "122200", "002221" };
    public String[] defThreeWedgeFree = { "22020", "02202", "02022", "20220", "202020", "020202" };
    public String[] defThreeWedge = { "20202", "20022", "2202", "2022" };
    public String[] defThree = { "02220", "02202", "02022", "20220", "20202", "20022" };
    public String[] defFourWedge = { "122022", "220221", "202221", "120222", "222021",  "122202" };
    public String[] defFour = { "122220", "022221" };
    
    // Capture
    public String[] potentialCapture = { "0220" };
    public String[] defFourDoubleDoubleCap = { "1220221" };
    public String[] defThreeCap = { "20221", "12202"  };
    public String[] capture = { "1220", "0221" };

    // Get captured

    // Score for each pattern :
    int winScore = Board.WINNING_SCORE;
    int liveFourWedgeScore = 45000;
    int liveFourScore = 40000;
    int liveThreeWedgeFreeScore = 2500;
    int liveThreeWedgeScore = 2000;
    int liveThreeFreeScore = 1500;
    int liveThreeScore = 1000;
    int deadThreeWedgeScore = -250;
    int deadThreeScore = -250;
    int liveTwoWedgeFreeScore = 300;
    int liveTwoWedgeScore = 250;
    int liveTwoFreeScore = 225;
    int liveTwoScore = 5;
    int liveOneScore = 3;
    
    int wasteScore = -1000;
    
    int selfCaptureScore = -5000;    
    
    int defDeadThreeWedgeScore = 2000;
    int defDeadThreeScore = 1000;
    int defThreeWedgeFreeScore = 3000;
    int defThreeWedgeScore = 2500;
    int defThreeScore = 2000;
    int defFourWedgeScore = 25000;
    int defFourScore = 15000;
    
    int defFourDoubleDoubleCapScore = Board.WINNING_SCORE;
    int potentialCaptureScore = 500;
    int defThreeCapScore = 5000;
    int captureScore = 25000;
    
    int getCapturedScore = -2500;
    
    int[][] board;

    public EvalBoard(int size) {
        this.board = new int[size][size];

        patterns = new HashMap<Integer, String[]>();                
        patterns.put(winScore, win);
        patterns.put(liveFourWedgeScore, liveFourWedge);
        patterns.put(liveFourScore, liveFour);
        patterns.put(liveThreeWedgeFreeScore, liveThreeWedgeFree);
        patterns.put(liveThreeWedgeScore, liveThreeWedge);
        patterns.put(liveThreeFreeScore, liveThreeFree);
        patterns.put(liveThreeScore, liveThree);
        patterns.put(deadThreeWedgeScore, deadThreeWedge);
        patterns.put(deadThreeScore, deadThree);
        patterns.put(liveTwoWedgeFreeScore, liveTwoWedgeFree);
        patterns.put(liveTwoWedgeScore, liveTwoWedge);
        patterns.put(liveTwoFreeScore, liveTwoFree);
        patterns.put(liveTwoScore, liveTwo);
        patterns.put(liveOneScore,liveOne);

        patterns.put(wasteScore, waste);

        patterns.put(selfCaptureScore, selfCapture);
        patterns.put(getCapturedScore, getCaptured);

        patterns.put(defDeadThreeWedgeScore, defDeadThreeWedge);
        patterns.put(defDeadThreeScore, defDeadThree);
        patterns.put(defThreeWedgeFreeScore, defThreeWedgeFree);
        patterns.put(defThreeWedgeScore, defThreeWedge);
        patterns.put(defThreeScore, defThree);
        patterns.put(defFourWedgeScore, defFourWedge);
        patterns.put(defFourScore, defFour);

        patterns.put(defFourDoubleDoubleCapScore, defFourDoubleDoubleCap);
        patterns.put(potentialCaptureScore, potentialCapture);
        patterns.put(defThreeCapScore, defThreeCap);
        patterns.put(captureScore, capture);
    }

    public int getTotalScoreBlack() {
        return totalScoreBlack;
    }

    public int getTotalScoreRed() {
        return totalScoreRed;
    }    

    public void play(int row, int col, int player) {
        board[row][col] = player;
    }

    public void capture(int x, int y) {
        board[x][y] = 0;
    }

    public void addPoint(int x, int y, int score, Mark player) {
        board[x][y] += score;
        
        if (player == Mark.Black) {
            totalScoreBlack += score;
        } else { 
            totalScoreRed += score;
        }
    }

    public void reset() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 1 && board[i][j] != 2) {
                    board[i][j] = 0;
                }
            }
        }
    }

    public ArrayList<Move> bestMove(Mark player) {
        this.evaluateBoard(player);
        int maxScore = Integer.MIN_VALUE;
        ArrayList<Move> candidates = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {                
                
                if(board[i][j] != 0 && board[i][j] != 1 && board[i][j] != 2) {
                    Move newMove = new Move(i, j, player);
                    newMove.setScore(board[i][j]);
                    candidates.add(newMove);
                    // if (newMove.getScore() > maxScore) {
                    //     maxScore = newMove.getScore();
    
                    //     candidates.clear();
                    //     candidates.add(newMove);
                    // } else if (newMove.getScore() == maxScore) {
                        
                    // }
                }

            }
        }
        
        // System.out.("maxScore: " + maxScore);
        this.display();

        return candidates;
    }

    public void evaluateBoard(Mark player) {
        evalHorizontal(player);
        evalVertical(player);
        evalDiagonalLeftToRight(player);
        evalDiagonalRightToLeft(player);
    }

    private void appendToString(int i, int j, Mark player, StringBuilder s) {
        if (board[i][j] == player.ordinal()) {
            s.append("1");
        } else if (board[i][j] == 0) {
            s.append("0");
        } else if (board[i][j] == player.getOpponent().ordinal()) {
            s.append("2");
        }
    }

    private void evalHorizontal(Mark player) {
        for (int i = 0; i < board.length; i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < board[0].length; j++) {
                appendToString(i, j, player, s);
            }

            for (int score : patterns.keySet()) {
                checkPattern(s.toString(), patterns.get(score), score, i, 0, true, false, false, false, player);                
            }

            // checkPattern(s.toString(), win, winScore, i, 0, true, false, false, false, player);            
            // checkPattern(s.toString(), liveFour, liveFourScore, i, 0, true, false, false, false, player);
            // checkPattern(s.toString(), liveThree, liveThreeScore, i, 0, true, false, false, false, player);
            // checkPattern(s.toString(), deadThree, deadThreeScore, i, 0, true, false, false, false, player);
            // checkPattern(s.toString(), capture, captureScore, i, 0, true, false, false, false, player);
            // checkPattern(s.toString(), liveTwo, liveTwoScore, i, 0, true, false, false, false, player);
            // checkPattern(s.toString(), liveOne, liveOneScore, i, 0, true, false, false, false, player);
            // checkPattern(s.toString(), defThree, defThreeScore, i, 0, true, false, false, false, player);
            // checkPattern(s.toString(), defFour, defFourScore, i, 0, true, false, false, false, player);
        }
    }

    private void evalVertical(Mark player) {
        for (int j = 0; j < board[0].length; j++) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < board.length; i++) {
                appendToString(i, j, player, s);
            }

            for (int score : patterns.keySet()) {
                checkPattern(s.toString(), patterns.get(score), score, 0,j,false, true, false, false, player);
            }

            // checkPattern(s.toString(), win, winScore, 0,j,false, true, false, false, player);
            // checkPattern(s.toString(), liveFour, liveFourScore, 0, j, false, true, false, false, player);
            // checkPattern(s.toString(), liveThree, liveThreeScore, 0, j, false, true, false, false, player);
            // checkPattern(s.toString(), deadThree, deadThreeScore, 0, j, false, true, false, false, player);
            // checkPattern(s.toString(), capture, captureScore, 0, j, false, true, false, false, player);
            // checkPattern(s.toString(), liveTwo, liveTwoScore, 0, j, false, true, false, false, player);
            // checkPattern(s.toString(), liveOne, liveOneScore, 0, j, false, true, false, false, player);
            // checkPattern(s.toString(), defThree, defThreeScore, 0, j, false, true, false, false, player);
            // checkPattern(s.toString(), defFour, defFourScore, 0, j, false, true, false, false, player);
        }
    }

    private void evalDiagonalLeftToRight(Mark player) {
        int rows = board.length;
        int cols = board[0].length;

        for (int startCol = 0; startCol < cols; startCol++) {
            StringBuilder diagonal = new StringBuilder();
            int r = 0, c = startCol;
            while (r < rows && c < cols) {
                appendToString(r, c, player, diagonal);
                r++;
                c++;

            }
            // System.out.(diagonal.toString());
            if (diagonal.length() >= 3) {
            //     checkPattern(diagonal.toString(), win, winScore, r-1, c-1, false,
            //     false,true,false, player);
            //     checkPattern(diagonal.toString(), liveFour, liveFourScore, r-1, c-1, false,
            //     false,true,false, player);
            //     checkPattern(diagonal.toString(), liveThree, liveThreeScore, r-1, c-1, false,
            //     false,true,false, player);
            //     checkPattern(diagonal.toString(), deadThree, deadThreeScore, r-1, c-1, false,
            //     false,true,false, player);
            //     checkPattern(diagonal.toString(), capture, captureScore, r-1, c-1, false,
            //     false,true,false, player);
            //     checkPattern(diagonal.toString(), liveTwo, liveTwoScore, r-1, c-1, false,
            //     false,true,false, player);
            //     checkPattern(diagonal.toString(), liveOne, liveOneScore, r-1, c-1, false, false,true,false, player);
            //     checkPattern(diagonal.toString(), defThree, defThreeScore, r-1, c-1, false,
            //     false,true,false, player);
            //     checkPattern(diagonal.toString(), defFour, defFourScore, r-1, c-1, false,
            //     false,true,false, player);

                for (int score : patterns.keySet()) {
                    checkPattern(diagonal.toString(), patterns.get(score), score, r - 1, c - 1, false, false, true, false, player);
                }
            }
        }

        for (int startRow = 1; startRow < rows; startRow++) {
            StringBuilder diagonal = new StringBuilder();
            int r = startRow, c = 0;
            while (r < rows && c < cols) {
                appendToString(r, c, player, diagonal);
                r++;
                c++;

            }
            // // System.out.(diagonal.toString());
            if (diagonal.length() >= 3) {
                // checkPattern(diagonal.toString(), win, winScore, r-1, c-1, false,
                // false,true,false, player);
                // checkPattern(diagonal.toString(), liveFour, liveFourScore, r-1, c-1, false,
                // false,true,false, player);
                // checkPattern(diagonal.toString(), liveThree, liveThreeScore, r-1, c-1, false,
                // false,true,false, player);
                // checkPattern(diagonal.toString(), deadThree, deadThreeScore, r-1, c-1, false,
                // false,true,false, player);
                // checkPattern(diagonal.toString(), capture, captureScore, r-1, c-1, false,
                // false,true,false, player);
                // checkPattern(diagonal.toString(), liveTwo, liveTwoScore, r-1, c-1, false,
                // false,true,false, player);
                // checkPattern(diagonal.toString(), liveOne, liveOneScore, r-1, c-1, false, false,true,false, player);
                // checkPattern(diagonal.toString(), defThree, defThreeScore, r-1, c-1, false,
                // false,true,false, player);
                // checkPattern(diagonal.toString(), defFour, defFourScore, r-1, c-1, false,
                // false,true,false, player);
                for (int score : patterns.keySet()) {
                    checkPattern(diagonal.toString(), patterns.get(score), score, r - 1, c - 1, false, false, true, false, player);
                }
            }
        }
    }

    private void evalDiagonalRightToLeft(Mark player) {
        int rows = board.length;
        int cols = board[0].length;

        // Diagonals starting from the top row (decreasing column)
        for (int startCol = cols - 1; startCol >= 0; startCol--) {
            StringBuilder diagonal = new StringBuilder();
            int r = 0, c = startCol;
            while (r < rows && c >= 0) {
                appendToString(r, c, player, diagonal);
                r++;
                c--;
            }
            // System.out.(diagonal.toString());
            if (diagonal.length() >= 3) {
                // checkPattern(diagonal.toString(), win, winScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), liveFour, liveFourScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), liveThree, liveThreeScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), deadThree, deadThreeScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), capture, captureScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), liveTwo, liveTwoScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), liveOne, liveOneScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), defThree, defThreeScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), defFour, defFourScore, r - 1, c + 1, false, false, false, true, player);       
                for (int score : patterns.keySet()) {
                    checkPattern(diagonal.toString(), patterns.get(score), score, r - 1, c + 1, false, false, false, true, player);
                }
            }
        }

        // Diagonals starting from the leftmost column (excluding top row)
        for (int startRow = 1; startRow < rows; startRow++) {
            StringBuilder diagonal = new StringBuilder();
            int r = startRow, c = cols - 1;
            while (r < rows && c >= 0) {
                appendToString(r, c, player, diagonal);
                r++;
                c--;
            }
            // System.out.(diagonal.toString());
            if (diagonal.length() >= 3) {
                // checkPattern(diagonal.toString(), win, winScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), liveFour, liveFourScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), liveThree, liveThreeScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), deadThree, deadThreeScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), capture, captureScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), liveTwo, liveTwoScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), liveOne, liveOneScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), defThree, defThreeScore, r - 1, c + 1, false, false, false, true, player);
                // checkPattern(diagonal.toString(), defFour, defFourScore, r - 1, c + 1, false, false, false, true, player);
                for (int score : patterns.keySet()) {
                    checkPattern(diagonal.toString(), patterns.get(score), score, r - 1, c + 1, false, false, false, true, player);
                }
            }
        }
    }

    private void checkPattern(String lineString, String[] patterns, int score, int baseRow, int baseCol,
            boolean isHorizontal, boolean isVertical, boolean isDiagonalLeftToRight, boolean isDiagonalRightToLeft, Mark player) {
        boolean scoreAssigned = false;
        for (String pattern : patterns) {
            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(lineString);
            while (matcher.find()) {
                // System.out.("Pattern found: " + pattern);
                // System.out.("Adding score: " + score);
                // System.out.("Base row: " + baseRow + " Base col: " + baseCol);
                // System.out.("Start: " + matcher.start() + " End: " + matcher.end());
                for (int k = matcher.start(); k < matcher.end(); k++) {
                    if (isHorizontal && board[baseRow][k] != 1 && board[baseRow][k] != 2) {                        
                        this.addPoint(baseRow, k, score, player);
                        scoreAssigned = true;
                    } else if (isVertical && board[k][baseCol] != 1 && board[k][baseCol] != 2) {                        
                        this.addPoint(k, baseCol, score, player);           
                        scoreAssigned = true;             
                    } else if (isDiagonalLeftToRight) {
                        if (baseRow >= baseCol) {
                            int row = baseRow - baseCol + k;
                            int col = k;
                            // System.out.("row: " + row + " col: " + col);
                            if (board[row][col] != 1 && board[row][col] != 2) {
                                this.addPoint(row, col, score, player);         
                                scoreAssigned = true;                           
                            }
                        } else {
                            int row = k;
                            int col = baseCol - baseRow + k;
                            // System.out.("row: " + row + " col: " + col);
                            if (board[row][col] != 1 && board[row][col] != 2) {
                                this.addPoint(row, col, score, player);    
                                scoreAssigned = true;                                
                            }                                
                        }
                    } else if (isDiagonalRightToLeft) {
                        if (baseCol < 15) {
                            int row = baseCol + k;
                            int col = baseRow - k;
                            if (board[row][col] != 1 && board[row][col] != 2) {
                                this.addPoint(row, col, score, player);       
                                scoreAssigned = true;                             
                            }
                        }
                    }
                }
            }
            if (scoreAssigned) {
                break;                    
            }
        }
    }

    public void display() {
        // System.out.("Board:");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                // System.out.printf("%8d", board[i][j]);
            }
            // System.out.();
            // System.out.print("\n");
        }
    }

}
