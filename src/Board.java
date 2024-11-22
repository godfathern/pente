import java.util.ArrayList;

public class Board {
    public static final int WINNING_SCORE = 100000;
    private static final int BOARD_SIZE = 15;

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
            // System.out.println("[play()] Playing move: " + move);            
            playedMoves.add(turns, move);
            turns++;
        }
    }

    public void undo(Move move) {
        if (board[move.getCol()][move.getRow()] == move.getColor()) {
            board[move.getCol()][move.getRow()] = Mark.Empty;
            // System.out.println("[undo()] Undoing move: " + move);
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
        Mark player = move.getColor();
        Mark[][] currentBoard = board;
        int highScore = 0, score = 0;
        
        // start with most basic shapes
        Shapes.isPair(currentBoard, row, col, player);
        return false;
    }

    public boolean checkWin(Mark mark) {
        int markCaptures = getCaptures(mark);
        
        for (Move move : playedMoves) {
            if(move.getColor() == mark) { 
                if(Shapes.isConnected(board, move.getCol(), move.getRow(), mark, 5, new int[]{0, 1})) {
                    return true;
                } else if(Shapes.isConnected(board, move.getCol(), move.getRow(), mark, 5, new int[]{1, 0})) {
                    return true;
                } else if(Shapes.isConnected(board, move.getCol(), move.getRow(), mark, 5, new int[]{1, 1})) {
                    return true;
                } else if(Shapes.isConnected(board, move.getCol(), move.getRow(), mark, 5, new int[]{1, -1})) {
                    return true;
                }                
            }
        }
        
        return markCaptures >= 5;
    }

    public int evaluate(Mark mark) {      
        // System.out.println("[evaluate()] Evaluating board for: " + mark);  
        int markScore = 0;        
        int oppScore = 0;
        
        Mark oppMark = mark.getOpponent();
        // System.out.println("[evaluate()] Opponent: " + oppMark);

        if(checkWin(mark) || checkWin(oppMark)) {
            // System.out.println("[evaluate()] " + mark + " has won");
            return Board.WINNING_SCORE;
        }
        
        for (Move move : playedMoves) {
            // System.out.println("[evaluate()] Evaluating move: " + move);            
            int moveScore = 0;

            int[][] directions = {
                {0, 1},  // Horizontal (right)
                {1, 0},  // Vertical (down)
                {1, 1},  // Diagonal (down-right)
                {1, -1}  // Diagonal (down-left)
            };

            for (int[] dir : directions) {
                // System.out.println("[evaluate()] Checking direction: " + dir[0] + ", " + dir[1]);
                if(Solvers.isBlocking(board,  move, dir)) {
                    moveScore += 600;
                    
                    // System.out.println("[evaluate()]" + move + " is blocking");                    
                    if(Shapes.isConnected(board, move.getCol() + dir[1], move.getRow() + dir[0], mark.getOpponent(), 4, dir)) {
                        // System.out.println("[evaluate()] " + move + " is a row of 4");
                        moveScore += 300;
                    } else if(Shapes.isConnected(board, move.getCol() + dir[1], move.getRow() + dir[0], mark.getOpponent(), 3, dir)) {
                        // System.out.println("[evaluate()] " + move + " is a row of 3");
                        moveScore += 200;
                    }
                } else {
                    if(Shapes.isConnected(board, move.getCol(), move.getRow(), mark, 4, dir)) {
                        // System.out.println("[evaluate()] " + move + " is a row of 4");
                        moveScore += 300;
                    } else if(Shapes.isConnected(board, move.getCol(), move.getRow(), mark, 3, dir)) {
                        // System.out.println("[evaluate()] " + move + " is a row of 3");
                        moveScore += 200;
                    }
                }
                
            }

            // System.out.println("[evaluate()] Move score: " + moveScore);
            if(move.getColor() == mark) {
                markScore += moveScore;
            } else {
                oppScore += moveScore;
            }
        }

        // System.out.println("[evaluate()] Final score: " + (markScore - oppScore));
        return (markScore - oppScore);
    }

    /**
     * Returns all the moves of a certain color (Most likely deprecated by the
     * movesPlayed array)
     * 
     * @param mark The color of the moves to return
     * @return List of all the moves of a certain color
     */
    public ArrayList<Move> getAllMarks(Mark mark) {
        ArrayList<Move> moves = new ArrayList<Move>();

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

        // System.out.println("[getPossibleMoves()] Getting possible Moves for: " + mark);
        ArrayList<Move> moves = new ArrayList<Move>();
        ArrayList<Move> playedMovesCopy = new ArrayList<Move>(playedMoves);
            
        int maxScore = Integer.MIN_VALUE;  
        for (Move move : playedMovesCopy) {
            // System.out.println("[getPossibleMoves()] Getting empty squares around move: " + move);
            for (int i = move.getCol() - squareDist; i <= move.getCol() + squareDist; i++) {
                for (int j = move.getRow() - squareDist; j <= move.getRow() + squareDist; j++) {
                    if(i == move.getCol() && j == move.getRow()) {
                        continue;
                    }

                    if (isInbound(i, j) && board[i][j] == Mark.Empty) {
                        Move newMove = new Move(i, j, mark);
                        // System.out.println("[getPossibleMoves()] Evaluating move: " + newMove);
                        play(newMove);
                        newMove.setScore(evaluate(mark));
                        undo(newMove);
        
                        // System.out.println("[getPossibleMoves()] Evaluation result | Move: " + newMove + " - Score: " + newMove.getScore());                        
                        if(newMove.getScore() > maxScore) {
                            // System.out.println("[getPossibleMoves()] " + newMove + " is better than max score" );         
                            
                            maxScore = newMove.getScore();
                            // System.out.println("[getPossibleMoves()] New max score: " + maxScore );                                     
                            moves.clear();
                            moves.add(newMove);
                        } else if(newMove.getScore() == maxScore && !moves.contains(newMove)) {
                            // System.out.println("[getPossibleMoves()] " + newMove + " is equal to max score" );
                            moves.add(newMove);
                        }
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
        if (col < 0 || col >= BOARD_SIZE || row < 0 || row >= BOARD_SIZE) {
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
                if (move.getCol() == m.getCol() && move.getRow() == m.getRow()) {
                    continue;
                }

                if (move.isWithinNSquares(m, 1)) {
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
