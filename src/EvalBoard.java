import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EvalBoard {

    // Im Pep Guardiola, I have the ball, the opponent has the ball, the ball is the
    // most important thing in the world
    public String[] liveFour = { "11011", "10111", "11101", "11110", "01111" };
    public String[] liveThree = { "010110", "011010", "011100", "001110" };
    public String[] deadThree = { "211100", "211010", "210110", "010112" };
    public String[] capture = { "1220", "0221" };
    public String[] liveTwo = { "0110", "0101", "1100", "1010" };
    public String[] liveOne = { "012", "021", "120", "210", "02" };

    // Im Jose Mourinho
    public String[] defThree = { "02220", "02202", "02022", "20220", "20202", "20022" };
    public String[] defFour = { "122220", "022221", "202221", "220221", "222021" };
    public String[] getCaptured = { "01120", "02110" };// -250 (havent implemented yet)

    // Score for each pattern :
    int liveFourScore = 41000;
    int liveThreeScore = 3100;
    int deadThreeScore = 290;
    int captureScore = 250;
    int liveTwoScore = 3;
    int liveOneScore = 3;
    int defThreeScore = 3000;
    int defFourScore = 3150;
    int getCapturedScore = -250;

    // Capture :
    public String[] captureForReal = { "1221", "1221","2112", "2112" };

 

    int[][] board;

    public EvalBoard(int size) {
        this.board = new int[size][size];
    }

    public void play(int x, int y, int player) {
        board[x][y] = player;
    }

    public void capture(int x, int y) {
        board[x][y] = 0;
    }

    public void addPoint(int x, int y, int score) {
        board[x][y] += score;
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

    // Ditmemay :

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
                    candidates.add(new int[] { i, j });
                } else if (board[i][j] == maxScore) {
                    candidates.add(new int[] { i, j });
                }
            }
        }
        int[] bestMove = candidates.get(random.nextInt(candidates.size()));
        System.out.println("maxScore: " + maxScore);
        this.display();
        this.play(bestMove[0], bestMove[1], 1);
        return CoordinateConverter.coordinateToString(bestMove[0], bestMove[1]);
    }

    public void evaluateBoard() {
        evalHorizontal();
        evalVertical();
        evalDiagonalLeftToRight();
        evalDiagonalRightToLeft();
    }

    private void evalHorizontal() {
        for (int i = 0; i < board.length; i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 1) {
                    s.append("1");
                } else if (board[i][j] == 0) {
                    s.append("0");
                } else if (board[i][j] == 2) {
                    s.append("2");
                }
            }
            checkPattern(s.toString(), liveFour, liveFourScore, i, 0, true, false, false, false);
            checkPattern(s.toString(), liveThree, liveThreeScore, i, 0, true, false, false, false);
            checkPattern(s.toString(), deadThree, deadThreeScore, i, 0, true, false, false, false);
            checkPattern(s.toString(), capture, captureScore, i, 0, true, false, false, false);
            checkPattern(s.toString(), liveTwo, liveTwoScore, i, 0, true, false, false, false);
            checkPattern(s.toString(), liveOne, liveOneScore, i, 0, true, false, false, false);
            checkPattern(s.toString(), defThree, defThreeScore, i, 0, true, false, false, false);
            checkPattern(s.toString(), defFour, defFourScore, i, 0, true, false, false, false);

        }
    }

    private void evalVertical() {
        for (int j = 0; j < board[0].length; j++) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < board.length; i++) {
                if (board[i][j] == 1) {
                    s.append("1");
                } else if (board[i][j] == 0) {
                    s.append("0");
                } else if (board[i][j] == 2) {
                    s.append("2");
                }
            }
            checkPattern(s.toString(), liveFour, liveFourScore, 0, j, false, true, false, false);
            checkPattern(s.toString(), liveThree, liveThreeScore, 0, j, false, true, false, false);
            checkPattern(s.toString(), deadThree, deadThreeScore, 0, j, false, true, false, false);
            checkPattern(s.toString(), capture, captureScore, 0, j, false, true, false, false);
            checkPattern(s.toString(), liveTwo, liveTwoScore, 0, j, false, true, false, false);
            checkPattern(s.toString(), liveOne, liveOneScore, 0, j, false, true, false, false);
            checkPattern(s.toString(), defThree, defThreeScore, 0, j, false, true, false, false);
            checkPattern(s.toString(), defFour, defFourScore, 0, j, false, true, false, false);

        }
    }

    private void evalDiagonalLeftToRight() {
        int rows = board.length;
        int cols = board[0].length;

        for (int startCol = 0; startCol < cols; startCol++) {
            StringBuilder diagonal = new StringBuilder();
            int r = 0, c = startCol;
            while (r < rows && c < cols) {
                if (board[r][c] == 1) {
                    diagonal.append("1");
                } else if (board[r][c] == 0) {
                    diagonal.append("0");
                } else if (board[r][c] == 2) {
                    diagonal.append("2");
                }
                r++;
                c++;

            }
            System.out.println(diagonal.toString());
            if (diagonal.length() >= 3) {
                checkPattern(diagonal.toString(), liveFour, liveFourScore, r-1, c-1, false,
                false,true,false);
                checkPattern(diagonal.toString(), liveThree, liveThreeScore, r-1, c-1, false,
                false,true,false);
                checkPattern(diagonal.toString(), deadThree, deadThreeScore, r-1, c-1, false,
                false,true,false);
                checkPattern(diagonal.toString(), capture, captureScore, r-1, c-1, false,
                false,true,false);
                checkPattern(diagonal.toString(), liveTwo, liveTwoScore, r-1, c-1, false,
                false,true,false);
                checkPattern(diagonal.toString(), liveOne, liveOneScore, r-1, c-1, false,
                false,true,false);
                checkPattern(diagonal.toString(), defThree, defThreeScore, r-1, c-1, false,
                false,true,false);
                checkPattern(diagonal.toString(), defFour, defFourScore, r-1, c-1, false,
                false,true,false);

            }
        }

        for (int startRow = 1; startRow < rows; startRow++) {
            StringBuilder diagonal = new StringBuilder();
            int r = startRow, c = 0;
            while (r < rows && c < cols) {
                if (board[r][c] == 1) {
                    diagonal.append("1");
                } else if (board[r][c] == 0) {
                    diagonal.append("0");
                } else if (board[r][c] == 2) {
                    diagonal.append("2");
                }
                r++;
                c++;

            }
            // System.out.println(diagonal.toString());
            if (diagonal.length() >= 3) {
                checkPattern(diagonal.toString(), liveFour, liveFourScore, r-1, c-1, false,
                false,true,false);
                checkPattern(diagonal.toString(), liveThree, liveThreeScore, r-1, c-1, false,
                false,true,false);
                checkPattern(diagonal.toString(), deadThree, deadThreeScore, r-1, c-1, false,
                false,true,false);
                checkPattern(diagonal.toString(), capture, captureScore, r-1, c-1, false,
                false,true,false);
                checkPattern(diagonal.toString(), liveTwo, liveTwoScore, r-1, c-1, false,
                false,true,false);
                checkPattern(diagonal.toString(), liveOne, liveOneScore, r-1, c-1, false,
                false,true,false);
                checkPattern(diagonal.toString(), defThree, defThreeScore, r-1, c-1, false,
                false,true,false);
                checkPattern(diagonal.toString(), defFour, defFourScore, r-1, c-1, false,
                false,true,false);

            }
        }
    }

    private void evalDiagonalRightToLeft() {
        int rows = board.length;
        int cols = board[0].length;

        // Diagonals starting from the top row (decreasing column)
        for (int startCol = cols - 1; startCol >= 0; startCol--) {
            StringBuilder diagonal = new StringBuilder();
            int r = 0, c = startCol;
            while (r < rows && c >= 0) {
                if (board[r][c] == 1) {
                    diagonal.append("1");
                } else if (board[r][c] == 0) {
                    diagonal.append("0");
                } else if (board[r][c] == 2) {
                    diagonal.append("2");
                }
                r++;
                c--;
            }
            System.out.println(diagonal.toString());
            if (diagonal.length() >= 3) {
                checkPattern(diagonal.toString(), liveFour, liveFourScore, r - 1, c + 1, false, false, false, true);
                checkPattern(diagonal.toString(), liveThree, liveThreeScore, r - 1, c + 1, false, false, false, true);
                checkPattern(diagonal.toString(), deadThree, deadThreeScore, r - 1, c + 1, false, false, false, true);
                checkPattern(diagonal.toString(), capture, captureScore, r - 1, c + 1, false, false, false, true);
                checkPattern(diagonal.toString(), liveTwo, liveTwoScore, r - 1, c + 1, false, false, false, true);
                checkPattern(diagonal.toString(), liveOne, liveOneScore, r - 1, c + 1, false, false, false, true);
                checkPattern(diagonal.toString(), defThree, defThreeScore, r - 1, c + 1, false, false, false, true);
                checkPattern(diagonal.toString(), defFour, defFourScore, r - 1, c + 1, false, false, false, true);

                
            }
        }

        // Diagonals starting from the leftmost column (excluding top row)
        for (int startRow = 1; startRow < rows; startRow++) {
            StringBuilder diagonal = new StringBuilder();
            int r = startRow, c = cols - 1;
            while (r < rows && c >= 0) {
                if (board[r][c] == 1) {
                    diagonal.append("1");
                } else if (board[r][c] == 0) {
                    diagonal.append("0");
                } else if (board[r][c] == 2) {
                    diagonal.append("2");
                }
                r++;
                c--;
            }
            System.out.println(diagonal.toString());
            if (diagonal.length() >= 3) {
                checkPattern(diagonal.toString(), liveFour, liveFourScore, r - 1, c + 1, false, false, false, true);
                checkPattern(diagonal.toString(), liveThree, liveThreeScore, r - 1, c + 1, false, false, false, true);
                checkPattern(diagonal.toString(), deadThree, deadThreeScore, r - 1, c + 1, false, false, false, true);
                checkPattern(diagonal.toString(), capture, captureScore, r - 1, c + 1, false, false, false, true);
                checkPattern(diagonal.toString(), liveTwo, liveTwoScore, r - 1, c + 1, false, false, false, true);
                checkPattern(diagonal.toString(), liveOne, liveOneScore, r - 1, c + 1, false, false, false, true);
                checkPattern(diagonal.toString(), defThree, defThreeScore, r - 1, c + 1, false, false, false, true);
                checkPattern(diagonal.toString(), defFour, defFourScore, r - 1, c + 1, false, false, false, true);
            }
        }
    }

    private void checkPattern(String lineString, String[] patterns, int score, int baseRow, int baseCol,
            boolean isHorizontal, boolean isVertical, boolean isDiagonalLeftToRight, boolean isDiagonalRightToLeft) {
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
                        if (baseRow >= baseCol) {
                            int row = baseRow - baseCol + k;
                            int col = k;
                            System.out.println("row: " + row + " col: " + col);
                            if (board[row][col] != 1 && board[row][col] != 2)
                                this.addPoint(row, col, score);
                        } else {
                            int row = k;
                            int col = baseCol - baseRow + k;
                            System.out.println("row: " + row + " col: " + col);
                            if (board[row][col] != 1 && board[row][col] != 2)
                                this.addPoint(row, col, score);
                        }
                    } else if (isDiagonalRightToLeft) {
                        if (baseCol < 15) {
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

    public void removeCapture() {
        int rows = board.length;
        int cols = board[0].length;
        // Horizontal
        for (int i = 0; i < board.length; i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 1) {
                    s.append("1");
                } else if (board[i][j] == 0) {
                    s.append("0");
                } else if (board[i][j] == 2) {
                    s.append("2");
                }
            }
            checkCapture(s.toString(), captureForReal, i, 0, true, false, false, false);
        }
        // Vertical
        for (int j = 0; j < board[0].length; j++) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < board.length; i++) {
                if (board[i][j] == 1) {
                    s.append("1");
                } else if (board[i][j] == 0) {
                    s.append("0");
                } else if (board[i][j] == 2) {
                    s.append("2");
                }
            }

            checkCapture(s.toString(), captureForReal, 0, j, false, true, false, false);
        }
        // Diagonal left to right
        for (int startCol = 0; startCol < cols; startCol++) {
            StringBuilder diagonal = new StringBuilder();
            int r = 0, c = startCol;
            while (r < rows && c < cols) {
                if (board[r][c] == 1) {
                    diagonal.append("1");
                } else if (board[r][c] == 0) {
                    diagonal.append("0");
                } else if (board[r][c] == 2) {
                    diagonal.append("2");
                }
                r++;
                c++;

            }
            checkCapture(diagonal.toString(), captureForReal, r - 1, c - 1, false, false, true, false);
        }
        for (int startRow = 1; startRow < rows; startRow++) {
            StringBuilder diagonal = new StringBuilder();
            int r = startRow, c = 0;
            while (r < rows && c < cols) {
                if (board[r][c] == 1) {
                    diagonal.append("1");
                } else if (board[r][c] == 0) {
                    diagonal.append("0");
                } else if (board[r][c] == 2) {
                    diagonal.append("2");
                }
                r++;
                c++;

            }
            checkCapture(diagonal.toString(), captureForReal, r - 1, c - 1, false, false, true, false);
        }
        // Diagnol right to left
        for (int startCol = cols - 1; startCol >= 0; startCol--) {
            StringBuilder diagonal = new StringBuilder();
            int r = 0, c = startCol;
            while (r < rows && c >= 0) {
                if (board[r][c] == 1) {
                    diagonal.append("1");
                } else if (board[r][c] == 0) {
                    diagonal.append("0");
                } else if (board[r][c] == 2) {
                    diagonal.append("2");
                }
                r++;
                c--;
            }
            checkCapture(diagonal.toString(), captureForReal, r - 1, c + 1, false, false, false, true);
        }
        for (int startRow = 1; startRow < rows; startRow++) {
            StringBuilder diagonal = new StringBuilder();
            int r = startRow, c = cols - 1;
            while (r < rows && c >= 0) {
                if (board[r][c] == 1) {
                    diagonal.append("1");
                } else if (board[r][c] == 0) {
                    diagonal.append("0");
                } else if (board[r][c] == 2) {
                    diagonal.append("2");
                }
                r++;
                c--;
            }
            checkCapture(diagonal.toString(), captureForReal, r - 1, c + 1, false, false, false, true);
        }
    }

    private void checkCapture(String lineString, String[] patterns, int baseRow, int baseCol, boolean isHorizontal,
            boolean isVertical, boolean isDiagonalLeftToRight, boolean isDiagonalRightToLeft) {
        for (String pattern : patterns) {
            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(lineString);
            while (matcher.find()) {
                System.out.println("Pattern found: " + pattern);
                System.out.println("Base row: " + baseRow + " Base col: " + baseCol);
                System.out.println("Start cap: " + matcher.start() + " End: " + matcher.end());
                int k = matcher.start();
                if (isHorizontal) {
                    board[baseRow][k + 1] = 0;
                    board[baseRow][k + 2] = 0;
                } else if (isVertical) {
                    board[k + 1][baseCol] = 0;
                    board[k + 2][baseCol] = 0;
                } else if (isDiagonalLeftToRight) {
                    if (baseRow >= baseCol) {
                        int row = baseRow - baseCol + k;
                        int col = k;
                        System.out.println("row: " + row + " col: " + col);
                        if (board[row][col]==1){
                            board[row + 1][col + 1] = 0;
                            board[row + 2][col + 2] = 0;
                        }
                        if (board[row][col]==2){
                            board[row + 1][col + 1] = 0;
                            board[row + 2][col + 2] = 0;
                        }

                    } else {
                        int row = k;
                        int col = baseCol - baseRow + k;
                        System.out.println("row: " + row + " col: " + col);
                        if (board[row][col]==1){
                            board[row + 1][col + 1] = 0;
                            board[row + 2][col + 2] = 0;
                        }
                        if (board[row][col]==2){
                        board[row + 1][col + 1] = 0;
                        board[row + 2][col + 2] = 0;
                        }
                    }
                } else if (isDiagonalRightToLeft) {
                    if (baseCol < 15) {
                        int row = baseCol + k;
                        int col = baseRow - k;
                        if (board[row][col]==1){
                            board[row + 1][col - 1] = 0;
                            board[row + 2][col - 2] = 0;
                        }
                        if (board[row][col]==2){
                            board[row + 1][col - 1] = 0;
                            board[row + 2][col - 2] = 0;
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
