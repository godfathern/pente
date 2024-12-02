import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.LineEvent;

public class EvalBoard {

    private int totalScoreBlack = 0;
    private int totalScoreRed = 0;

    private int threatsCountBlack = 0;
    
    private int threatCountRed = 0;
    
    
    
    // Im Pep Guardiola, I have the ball, the opponent has the ball, the ball is the
    // most important thing in the world
    private static HashMap<Integer, String[]> bestMovePatterns;
    private static HashMap<Integer, String[]> evaluatePatterns;
    
    public String[] win = { "11111" };
    public String[] liveFourWedge = { "11011", "10111", "11101"};
    public String[] liveFour = { "01111", "11110"  };
    public String[] liveThreeWedgeFree = { "010110", "011010", "101010" };
    public String[] liveThreeWedge = { "10101", "1011", "1101", };
    public String[] liveThreeFree = { "001110", "011100" };
    public String[] liveThree = { "01110", "01110" };
    public String[] deadThreeWedge = { "21101", "21011", "10112" };
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
    public String[] defThreeWedgeFree = { "022020", "022020", "020220", "20220", "202020", "020202" };
    public String[] defThreeWedge = { "20202", "20022", "2202", "2022" };
    public String[] defThree = { "02220", "02202", "02022", "20220", "20202", "20022" };
    public String[] defFourWedge = { "122022", "220221", "202221", "120222", "222021",  "122202" };
    public String[] defFour = {"022221", "122220", "2222122220", "222122220", "0222212222", "022221222"};

    //Blocking Moves
    public String[] blockDeadThreeWedge = { "122120", "121220", "021220", "122021", "120221", "120221" };
    public String[] blockDeadThree = { "122201", "122210", "012221", "102221" };
    public String[] blockThreeWedgeFree = { "22120", "02212", "12022", "20221", "202021", "021202", "120202" };
    public String[] blockThreeWedge = { "20212", "20122", "2212", "2122" };
    public String[] blockThree = { "12220", "02212", "12022", "20221", "20202", "20022" };
    public String[] blockFourWedge = { "122122", "221221", "21222", "121222", "22212",  "22212" };
    public String[] blockFour = { "122221", "122221" };
    public String[] twoWedge = { "101", "101" };
    
    // Capture
    public String[] potentialCapture = { "0220" };
    public String[] defFourDoubleDoubleCap = { "1220221" };
    public String[] defThreeCap = { "20221", "12202"  };
    public String[] capture = { "1220", "0221", "00221", "12200", "0012200", "2212200", "1220221",  };
    
    // Get captured
    
    //Evaluate patterns 
    
    // Score for each pattern :
    int winScore = Board.WINNING_SCORE + 7;
    int liveFourWedgeScore = Board.WINNING_SCORE + 5;
    int liveFourScore = Board.WINNING_SCORE + 3;
    int defFourDoubleDoubleCapScore = 70013;
    int defFourScore = 55013;
    int defFourWedgeScore = 55011;
    int captureScore = 40223;
    int defThreeCapScore = 25757;
    int defThreeScore = 20507;
    int potentialCaptureScore = 1023;
    int liveThreeWedgeFreeScore = 1505;
    int defThreeWedgeFreeScore = 1503;
    int defThreeWedgeScore = 1021;
    int liveThreeFreeScore = 1013;
    int liveThreeWedgeScore = 1001;
    int defDeadThreeWedgeScore = 1005;
    int defDeadThreeScore = 1003;
    int liveThreeScore = 10235;
    int liveTwoWedgeFreeScore = 5050;
    int getCapturedScore = 523;
    int liveTwoFreeScore = 503;
    int liveTwoWedgeScore = 305;
    int liveTwoScore = 5;
    int liveOneScore = 3;
    int deadThreeWedgeScore = -253;
    int deadThreeScore = -255;    
    int wasteScore = -1003;
    int selfCaptureScore = -10003;        
    
    int[][] board;
    
    public EvalBoard(int size) {
        this.board = new int[size][size];

        evaluatePatterns = new HashMap<Integer, String[]>();
        evaluatePatterns.put(winScore, win);
        evaluatePatterns.put(defFourWedgeScore, blockFourWedge);
        evaluatePatterns.put(defFourScore, blockFour);
        evaluatePatterns.put(defThreeWedgeFreeScore, blockThreeWedgeFree);
        evaluatePatterns.put(defThreeWedgeScore, blockThreeWedge);
        evaluatePatterns.put(defThreeScore, blockThree);
        evaluatePatterns.put(defDeadThreeWedgeScore, blockDeadThreeWedge);
        evaluatePatterns.put(defDeadThreeScore, blockDeadThree);        
        
        bestMovePatterns = new HashMap<Integer, String[]>();
        bestMovePatterns.put(winScore, win);
        bestMovePatterns.put(liveFourWedgeScore, liveFourWedge);
        bestMovePatterns.put(liveFourScore, liveFour);
        bestMovePatterns.put(liveThreeWedgeFreeScore, liveThreeWedgeFree);
        bestMovePatterns.put(liveThreeWedgeScore, liveThreeWedge);
        bestMovePatterns.put(liveThreeFreeScore, liveThreeFree);
        bestMovePatterns.put(liveThreeScore, liveThree);
        bestMovePatterns.put(deadThreeWedgeScore, deadThreeWedge);
        bestMovePatterns.put(deadThreeScore, deadThree);
        bestMovePatterns.put(liveTwoWedgeFreeScore, liveTwoWedgeFree);
        bestMovePatterns.put(liveTwoWedgeScore, liveTwoWedge);
        bestMovePatterns.put(liveTwoFreeScore, liveTwoFree);
        bestMovePatterns.put(liveTwoScore, liveTwo);
        bestMovePatterns.put(liveOneScore,liveOne);
        
        bestMovePatterns.put(wasteScore, waste);
        
        bestMovePatterns.put(selfCaptureScore, selfCapture);
        bestMovePatterns.put(getCapturedScore, getCaptured);
        
        bestMovePatterns.put(defDeadThreeWedgeScore, defDeadThreeWedge);
        bestMovePatterns.put(defDeadThreeScore, defDeadThree);
        bestMovePatterns.put(defThreeWedgeFreeScore, defThreeWedgeFree);
        bestMovePatterns.put(defThreeWedgeScore, defThreeWedge);
        bestMovePatterns.put(defThreeScore, defThree);
        bestMovePatterns.put(defFourWedgeScore, defFourWedge);
        bestMovePatterns.put(defFourScore, defFour);

        bestMovePatterns.put(defFourDoubleDoubleCapScore, defFourDoubleDoubleCap);
        bestMovePatterns.put(potentialCaptureScore, potentialCapture);
        bestMovePatterns.put(defThreeCapScore, defThreeCap);
        bestMovePatterns.put(captureScore, capture);
    }
    
    public int getTotalScoreBlack() {
        return totalScoreBlack;
    }

    public int getTotalScoreRed() {
        return totalScoreRed;
    }    
    
    public int getThreatsCountBlack() {
        return threatsCountBlack;
    }

    public int getThreatCountRed() {
        return threatCountRed;
    }

    public void play(int row, int col, int player) {
        board[row][col] = player;
    }

    public void capture(int x, int y) {
        board[x][y] = 0;
    }
    
    public void updateCaptures(int captures) {
        // bestMovePatterns.remove(captureScore, capture);
        // captureScore = captureScore * captures;
        // bestMovePatterns.put(captureScore, capture);

        // bestMovePatterns.remove(potentialCaptureScore, potentialCapture);
        // potentialCaptureScore = potentialCaptureScore * captures;
        // bestMovePatterns.put(potentialCaptureScore, potentialCapture);

        // bestMovePatterns.remove(defFourDoubleDoubleCapScore * captures, defFourDoubleDoubleCap);
        // defFourDoubleDoubleCapScore = defFourDoubleDoubleCapScore * captures;
        // bestMovePatterns.put(defFourDoubleDoubleCapScore, defFourDoubleDoubleCap);

        // bestMovePatterns.remove(defThreeCapScore, defThreeCap);
        // defThreeCapScore = defThreeCapScore * captures;
        // bestMovePatterns.put(defThreeCapScore, defThreeCap);
    }

    public void addPoint(int x, int y, int score, Mark player) {
        board[x][y] += score;
        
        if (player == Mark.Black) {
            totalScoreBlack += score;
            threatsCountBlack++;
        } else {
            totalScoreRed += score;
            threatCountRed++;
        }
    }

    public void reset() {
        threatCountRed = 0;
        threatsCountBlack = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 1 && board[i][j] != 2) {
                    board[i][j] = 0;
                }
            }
        }
    }

    public ArrayList<Move> bestMove(Mark player) {
        reset();
        this.evaluateMoves(player);
        int maxScore = Integer.MIN_VALUE;
        ArrayList<Move> candidates = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {                
                Move newMove = new Move(j, i, player);
                newMove.setScore(board[i][j]);

                // if(newMove.getScore() != 0 && newMove.getScore() != 1 && newMove.getScore() != 2) {
                //     candidates.add(newMove);
                // }

                if (newMove.getScore() > maxScore) {
                    maxScore = newMove.getScore();

                    candidates.clear();
                    candidates.add(newMove);
                } else if (newMove.getScore() == maxScore) {
                    candidates.add(newMove);
                }
            }
        }
        
        // System.out.("maxScore: " + maxScore);
        this.display();

        return candidates;
    }

    public void evaluateMoves(Mark player) {
        evalHorizontal(player);
        evalVertical(player);
        evalDiagonalLeftToRight(player);
        evalDiagonalRightToLeft(player);
        evaluateBoard(player);
    }

    public void evaluateBoard(Mark player) {
        evaluateHorizontal(player);
        evaluateVertical(player);
        evaluateDiagonalLeftToRight(player);
        evaluateDiagonalRightToLeft(player);
    }

    private int evaluateHorizontal(Mark player) {
        int totalScore = 0;

        for (int i = 0; i < board.length; i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < board[0].length; j++) {
                appendToString(i, j, player, s);
            }

            for (int score : evaluatePatterns.keySet()) {
                evaluateCheckPattern(s.toString(), evaluatePatterns.get(score), score, i, 0, true, false, false, false, player);                
            }
        }

        return totalScore;
    }

    private int evaluateVertical(Mark player) {
        int totalScore = 0;
        
        for (int j = 0; j < board[0].length; j++) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < board.length; i++) {
                appendToString(i, j, player, s);
            }

            for (int score : evaluatePatterns.keySet()) {
                evaluateCheckPattern(s.toString(), evaluatePatterns.get(score), score, 0,j,false, true, false, false, player);
            }
        }

        return totalScore;
    }

    private void evaluateDiagonalLeftToRight(Mark player) {        
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
            
            if (diagonal.length() >= 3) {        
                for (int score : evaluatePatterns.keySet()) {
                    evaluateCheckPattern(diagonal.toString(), evaluatePatterns.get(score), score, r - 1, c - 1, false, false, true, false, player);
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

            if (diagonal.length() >= 3) {                
                for (int score : evaluatePatterns.keySet()) {
                    evaluateCheckPattern(diagonal.toString(), evaluatePatterns.get(score), score, r - 1, c - 1, false, false, true, false, player);
                }
            }
        }
    }

    private void evaluateDiagonalRightToLeft(Mark player) {
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
            if (diagonal.length() >= 3) {
                for (int score : evaluatePatterns.keySet()) {
                    evaluateCheckPattern(diagonal.toString(), evaluatePatterns.get(score), score, r - 1, c + 1, false, false, false, true, player);
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

            if (diagonal.length() >= 3) {
                for (int score : evaluatePatterns.keySet()) {
                    evaluateCheckPattern(diagonal.toString(), evaluatePatterns.get(score), score, r - 1, c + 1, false, false, false, true, player);
                }
            }
        }
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

            for (int score : bestMovePatterns.keySet()) {
                checkPattern(s.toString(), bestMovePatterns.get(score), score, i, 0, true, false, false, false, player);                
            }
        }
    }

    private void evalVertical(Mark player) {
        for (int j = 0; j < board[0].length; j++) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < board.length; i++) {
                appendToString(i, j, player, s);
            }

            for (int score : bestMovePatterns.keySet()) {
                checkPattern(s.toString(), bestMovePatterns.get(score), score, 0,j,false, true, false, false, player);
            }
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
            
            if (diagonal.length() >= 3) {        
                for (int score : bestMovePatterns.keySet()) {
                    checkPattern(diagonal.toString(), bestMovePatterns.get(score), score, r - 1, c - 1, false, false, true, false, player);
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

            if (diagonal.length() >= 3) {                
                for (int score : bestMovePatterns.keySet()) {
                    checkPattern(diagonal.toString(), bestMovePatterns.get(score), score, r - 1, c - 1, false, false, true, false, player);
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
            if (diagonal.length() >= 3) {
                for (int score : bestMovePatterns.keySet()) {
                    checkPattern(diagonal.toString(), bestMovePatterns.get(score), score, r - 1, c + 1, false, false, false, true, player);
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

            if (diagonal.length() >= 3) {
                for (int score : bestMovePatterns.keySet()) {
                    checkPattern(diagonal.toString(), bestMovePatterns.get(score), score, r - 1, c + 1, false, false, false, true, player);
                }
            }
        }
    }

    private void evaluateCheckPattern(String lineString, String[] patterns, int score, int baseRow, int baseCol,
            boolean isHorizontal, boolean isVertical, boolean isDiagonalLeftToRight, boolean isDiagonalRightToLeft, Mark player) {
        for (String pattern : patterns) {
            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(lineString);
            while (matcher.find()) {
                if(player == Mark.Black) {
                    totalScoreBlack += score;
                } else {
                    totalScoreRed += score;
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
