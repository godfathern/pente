import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EvalBoard {
    public static int amountOfMovesAnalyzed = 5;
    public int[] DScore = new int[] { 0, 1, 9, 81, 729 };
    public int[] AScore = new int[] { 0, 2, 18, 162, 1458 };

    public static String[] win = { "11111" };
    public static String[] liveFour = { "11011", "10111", "11101", "11110", "01111" }; // 500
    public static String[] liveThree = { "010110", "011010", "011100", "001110" }; // 120
    public static String[] capture = { "1220", "0221" }; // 100
    public static String[] liveTwo = { "0110", "0101", "1100", "1010" }; // 50
    public static String[] liveOne = { "012", "021", "120", "210" }; // 10

    public static ArrayList<Move> getEvalMoves(Mark[][] board, Mark player) {
        ArrayList<Move> moves = new ArrayList<>();

        moves.addAll(evalHorizontal(board, player));
        moves.addAll(evalVertical(board, player));
        moves.addAll(evalDiagonalLeftToRight(board, player));
        moves.addAll(evalDiagonalRightToLeft(board, player));

        return moves;
    }

    private static ArrayList<Move> evalHorizontal(Mark[][] board, Mark player) {
        ArrayList<Move> moves = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            StringBuilder s = new StringBuilder();

            for (int j = 0; j < board[0].length; j++) {
                s.append(board[i][j] == player ? "1" : board[i][j] == Mark.Empty ? "0" : "2");
            }

            moves.addAll(checkPattern(board, player, s.toString(), win, Board.WINNING_SCORE, i, 0, true, false, false,
                    false));
            moves.addAll(checkPattern(board, player, s.toString(), liveFour, 1000, i, 0, true, false, false, false));
            moves.addAll(checkPattern(board, player, s.toString(), liveThree, 100, i, 0, true, false, false, false));
            moves.addAll(checkPattern(board, player, s.toString(), capture, 1500, i, 0, true, false, false, false));
            moves.addAll(checkPattern(board, player, s.toString(), liveTwo, 10, i, 0, true, false, false, false));
        }

        return moves;
    }

    private static ArrayList<Move> evalVertical(Mark[][] board, Mark player) {
        ArrayList<Move> moves = new ArrayList<>();

        for (int j = 0; j < board[0].length; j++) {
            StringBuilder s = new StringBuilder();

            for (int i = 0; i < board.length; i++) {
                s.append(board[i][j] == player ? "1" : board[i][j] == Mark.Empty ? "0" : "2");
            }

            moves.addAll(checkPattern(board, player, s.toString(), win, Board.WINNING_SCORE, 0, j, false, true, false,
                    false));
            moves.addAll(checkPattern(board, player, s.toString(), liveFour, 1000, 0, j, false, true, false, false));
            moves.addAll(checkPattern(board, player, s.toString(), liveThree, 100, 0, j, false, true, false, false));
            moves.addAll(checkPattern(board, player, s.toString(), capture, 1500, 0, j, false, true, false, false));
            moves.addAll(checkPattern(board, player, s.toString(), liveTwo, 10, 0, j, false, true, false, false));
        }
        return moves;
    }

    private static ArrayList<Move> evalDiagonalLeftToRight(Mark[][] board, Mark player) {
        int rows = board[0].length;
        int cols = board.length;
        ArrayList<Move> moves = new ArrayList<>();

        for (int startCol = 0; startCol < cols; startCol++) {
            StringBuilder diagonal = new StringBuilder();
            int r = 0, c = startCol;

            while (r < rows && c < cols) {
                diagonal.append(board[c][r] == player ? "1" : board[c][r] == Mark.Empty ? "0" : "2");
                r++;
                c++;
            }

            if (diagonal.length() >= 3) {
                moves.addAll(checkPattern(board, player, diagonal.toString(), win, Board.WINNING_SCORE, r - 1, c - 1,
                        false, false, true, false));
                moves.addAll(checkPattern(board, player, diagonal.toString(), liveFour, 1000, r - 1, c - 1, false,
                        false, true, false));
                moves.addAll(checkPattern(board, player, diagonal.toString(), liveThree, 100, r - 1, c - 1, false,
                        false, true, false));
                moves.addAll(checkPattern(board, player, diagonal.toString(), capture, 1500, r - 1, c - 1, false, false,
                        true, false));
                moves.addAll(checkPattern(board, player, diagonal.toString(), liveTwo, 10, r - 1, c - 1, false, false,
                        true, false));
            }
        }

        for (int startRow = 1; startRow < rows; startRow++) {
            StringBuilder diagonal = new StringBuilder();
            int r = startRow, c = 0;

            while (r < rows && c < cols) {
                diagonal.append(board[c][r] == player ? "1" : board[c][r] == Mark.Empty ? "0" : "2");
                r++;
                c++;
            }

            if (diagonal.length() >= 3) {
                moves.addAll(checkPattern(board, player, diagonal.toString(), win, Board.WINNING_SCORE, r - 1, c - 1,
                        false, false, true, false));
                moves.addAll(checkPattern(board, player, diagonal.toString(), liveFour, 1000, r - 1, c - 1, false,
                        false, true, false));
                moves.addAll(checkPattern(board, player, diagonal.toString(), liveThree, 100, r - 1, c - 1, false,
                        false, true, false));
                moves.addAll(checkPattern(board, player, diagonal.toString(), capture, 1500, r - 1, c - 1, false, false,
                        true, false));
                moves.addAll(checkPattern(board, player, diagonal.toString(), liveTwo, 10, r - 1, c - 1, false, false,
                        true, false));                
            }
        }

        return moves;
    }

    private static ArrayList<Move> evalDiagonalRightToLeft(Mark[][] board, Mark player) {
        int rows = board.length;
        int cols = board[0].length;
        ArrayList<Move> moves = new ArrayList<>();

        // Diagonals starting from the top row (decreasing column)
        for (int startCol = cols - 1; startCol >= 0; startCol--) {
            StringBuilder diagonal = new StringBuilder();
            int r = 0, c = startCol;

            while (r < rows && c >= 0) {
                diagonal.append(board[c][r] == player ? "1" : board[c][r] == Mark.Empty ? "0" : "2");
                r++;
                c--;
            }

            if (diagonal.length() >= 3) {
                moves.addAll(checkPattern(board, player, diagonal.toString(), win, Board.WINNING_SCORE, r - 1, c + 1,
                        false, false, false, true));
                moves.addAll(checkPattern(board, player, diagonal.toString(), liveFour, 1000, r - 1, c + 1, false,
                        false, false, true));
                moves.addAll(checkPattern(board, player, diagonal.toString(), liveThree, 100, r - 1, c + 1, false,
                        false, false, true));
                moves.addAll(checkPattern(board, player, diagonal.toString(), capture, 1500, r - 1, c + 1, false, false,
                        false, true));
                moves.addAll(checkPattern(board, player, diagonal.toString(), liveTwo, 10, r - 1, c + 1, false, false,
                        false, true));                
            }
        }

        // Diagonals starting from the leftmost column (excluding top row)
        for (int startRow = 1; startRow < rows; startRow++) {
            StringBuilder diagonal = new StringBuilder();
            int r = startRow, c = cols - 1;

            while (r < rows && c >= 0) {
                diagonal.append(board[c][r] == player ? "1" : board[c][r] == Mark.Empty ? "0" : "2");
                r++;
                c--;
            }

            if (diagonal.length() >= 3) {
                moves.addAll(checkPattern(board, player, diagonal.toString(), win, Board.WINNING_SCORE, r - 1, c + 1,
                        false, false, false, true));
                moves.addAll(checkPattern(board, player, diagonal.toString(), liveFour, 1000, r - 1, c + 1, false,
                        false, false, true));
                moves.addAll(checkPattern(board, player, diagonal.toString(), liveThree, 100, r - 1, c + 1, false,
                        false, false, true));
                moves.addAll(checkPattern(board, player, diagonal.toString(), capture, 1500, r - 1, c + 1, false, false,
                        false, true));
                moves.addAll(checkPattern(board, player, diagonal.toString(), liveTwo, 10, r - 1, c + 1, false, false,
                        false, true));
            }
        }

        return moves;
    }

    private static ArrayList<Move> checkPattern(Mark[][] board, Mark player, String lineString, String[] patterns,
            int score,
            int baseRow, int baseCol, boolean isHorizontal, boolean isVertical, boolean isDiagonalLeftToRight,
            boolean isDiagonalRightToLeft) {
        ArrayList<Move> moves = new ArrayList<>();
        for (String pattern : patterns) {
            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(lineString);

            while (matcher.find()) {
                for (int k = matcher.start(); k < matcher.end(); k++) {
                    if (isHorizontal && board[k][baseRow] != player && board[k][baseRow] != player.getOpponent()) {
                        Move move = new Move(k, baseRow, player);
                        move.setScore(score);

                        moves.add(move);
                    } else if (isVertical && board[baseCol][k] != player && board[baseCol][k] != player.getOpponent()) {                        
                        Move move = new Move(baseCol, k, player);
                        move.setScore(score);

                        moves.add(move);
                    } else if (isDiagonalLeftToRight) {
                        if (baseRow >= baseCol) {
                            int row = baseRow - baseCol + k;
                            int col = k;

                            if (board[col][row] != player && board[col][row] != player.getOpponent()) {
                                Move move = new Move(col, row, player);
                                move.setScore(score);
                                moves.add(move);
                            }
                        } else {
                            int row = k;
                            int col = baseCol - baseRow + k;

                            if (board[col][row] != player && board[col][row] != player.getOpponent()) {
                                Move move = new Move(col, row, player);
                                move.setScore(score);

                                moves.add(move);
                            }
                        }
                    } else if (isDiagonalRightToLeft) {
                        if (baseCol < 15) {
                            int row = baseCol + k;
                            int col = baseRow - k;

                            if (board[col][row] != player && board[col][row] != player.getOpponent()) {
                                Move move = new Move(col, row, player);
                                move.setScore(score);

                                moves.add(move);
                            }
                        }
                    }
                }
            }
        }

        return moves;
    }
}
