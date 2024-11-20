import java.util.ArrayList;

public class Board {
    // 0: Empty, 1: Red, 2: Black
    private final Mark[][] board;
    private ArrayList<Move> playedMoves;
    private int turns;
    private int blackCaptures;
    private int redCaptures;

    public Board() {
        turns = 0;
        board = new Mark[15][15];
        playedMoves = new ArrayList<Move>();

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                board[i][j] = Mark.Empty;
            }
        }
    }

    public int getTurns() {
        return turns;
    }

    public void play(Move move) {
        if (board[move.getCol()][move.getRow()] == Mark.Empty) {
            board[move.getCol()][move.getRow()] = move.getColor();            
            playedMoves.add(turns, move);
            turns++;
        }
    }

    public void undo(Move move) {
        if (board[move.getCol()][move.getRow()] == move.getColor()) {
            board[move.getCol()][move.getRow()] = Mark.Empty;
            playedMoves.remove(move);
            turns--;
        }
    }

    public int getCaptures(Mark mark) {
        if (mark == Mark.Black) {
            return blackCaptures;
        }

        return redCaptures;
    }

    public boolean isCapture(Move move) {
        // TODO: Implement this
        // Check if the move captures any pieces using the Shapes & Letters class
        int row = move.getRow();
        int col = move.getCol();
        Mark player = move.getColor() == Mark.Red ? Mark.Red : Mark.Black;
        Mark[][] currentBoard = board;
        int highScore = 0, score = 0;
        // start with most basic shapes
        Shapes.isPair(currentBoard, row, col, player);
        return false;
    }

    public int evaluate(Mark mark) {
        Mark oppMark = mark.getOpponent();

        int markCaptures = getCaptures(mark);
        int oppCaptures = getCaptures(oppMark);
        int maxConnectedMark = getMaxConnected(mark);
        int maxConnectedOpp = getMaxConnected(oppMark);

        if (markCaptures == 5 || maxConnectedMark == 5) {
            return 100000;
        }

        if (oppCaptures == 5 || maxConnectedOpp == 5) {
            return -100000;
        }

        int markScore = maxConnectedMark * (markCaptures + 1);
        int oppScore = maxConnectedOpp * (oppCaptures + 1);

        return markScore - oppScore;
    }

    /**
     * Returns all the moves of a certain color (Most likely deprecated by the
     * movesPlayed array)
     * 
     * @param mark The color of the moves to return
     * @return List of all the moves of a certain color
     */
    public ArrayList<Move> getAllMarks(Mark mark) {
        ArrayList<Move> moves = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (board[i][j] == mark) {
                    moves.add(new Move(i, j, mark));
                }
            }
        }

        return moves;
    }

    public ArrayList<Move> getPossibleMoves(Mark mark) {        
        int squareDist = 1;

        ArrayList<Move> moves = new ArrayList<Move>();
        for (Move move : playedMoves) {
            for (int i = move.getCol() - squareDist; i <= move.getCol() + squareDist; i++) {
                for (int j = move.getRow() - squareDist; j <= move.getRow() + squareDist; j++) {
                    if(i == move.getCol() && j == move.getRow()) {
                        continue;
                    }

                    Move newMove = new Move(i, j, mark);
                    if (isInbound(i, j) && board[i][j] == Mark.Empty && !moves.contains(newMove)) {
                        moves.add(newMove);
                    }
                }
            }
        }

        return moves;
    }

    public ArrayList<Move> getPossibleMovesRedSecondTurn() {
        ArrayList<Move> moves = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j].equals(Mark.Empty)) {
                    moves.add(new Move(i, j, Mark.Red));
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 5; j < 15; j++) {
                if (board[i][j].equals(Mark.Empty)) {
                    moves.add(new Move(i, j, Mark.Red));
                }
            }
        }

        for (int i = 5; i < 10; i++) {
            for (int j = 10; j < 15; j++) {
                if (board[i][j].equals(Mark.Empty)) {
                    moves.add(new Move(i, j, Mark.Red));
                }
            }
        }

        for (int i = 10; i < 15; i++) {
            for (int j = 5; j < 15; j++) {
                if (board[i][j].equals(Mark.Empty)) {
                    moves.add(new Move(i, j, Mark.Red));
                }
            }
        }

        return moves;
    }

    public static boolean isInbound(int col, int row) {
        if (col < 0 || col >= 15 || row < 0 || row >= 15) {
            return false;
        }

        return true;
    }

    /**
     * Returns the maximum number of connected marks of a certain color
     * 
     * @param mark
     * @return Highest number of connected marks
     */
    public int getMaxConnected(Mark mark) {
        int maxConnected = 0;

        for (Move move : playedMoves) {
            int current = 0;
            for (Move m : playedMoves) {
                if (move == m) {
                    continue;
                }

                if (move.isWithinOneSquare(m)) {
                    current++;
                }
            }

            if (current > maxConnected) {
                maxConnected = current;
            }
        }

        return maxConnected;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 15; i++) {
            sb.append("[");
            for (int j = 0; j < 15; j++) {
                if (board[i][j].equals(Mark.Empty)) {
                    sb.append("E");
                } else if (board[i][j].equals(Mark.Red)) {
                    sb.append("R");
                } else {
                    sb.append("B");
                }
            }

            sb.append("]");
            sb.append("\n");
        }

        return sb.toString();
    }

}
