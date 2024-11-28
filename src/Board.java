import java.util.ArrayList;

public class Board {
    public static final int WINNING_SCORE = 100000;
    public static final int BOARD_SIZE = 15;

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

        Zobrist.init();
    }

    public int getTurns() {
        return turns;
    }

    public void play(Move move) {
        if (board[move.getCol()][move.getRow()] == Mark.Empty) {
            board[move.getCol()][move.getRow()] = move.getColor();
            // System.out.println("[play()] Playing move: " + move);
            handleCaptureMoves(move);            
            Zobrist.updateHash(move, false);
            playedMoves.add(turns, move);
            turns++;
        }
    }

    private void handleCaptureMoves(Move move) {
        for(int[] dir : Solvers.DIRECTIONS) {
            if (Solvers.isCapture(board, move, dir)) {
                move.setCapture(true);
                if (move.getColor() == Mark.Black) {
                    blackCaptures++;
                } else {
                    redCaptures++;
                }

                Move firstcaptive = new Move(move.getCol() + dir[0], move.getRow() + dir[1], move.getColor().getOpponent());
                Move secondCaptive = new Move(move.getCol() + dir[0] * 2, move.getRow() + dir[1] * 2, move.getColor().getOpponent());

                if(playedMoves.contains(firstcaptive) && playedMoves.contains(secondCaptive)) {                                            
                    board[firstcaptive.getCol()][firstcaptive.getRow()] = Mark.Empty;
                    board[secondCaptive.getCol()][secondCaptive.getRow()] = Mark.Empty;
                                        
                        for (Move playedMove : playedMoves) {
                            if(playedMove.equals(firstcaptive) || playedMove.equals(secondCaptive)) {
                               playedMove.setCaptured(true);
                               Zobrist.updateHash(playedMove, true);
                               move.addCapture(playedMove);
                            }
                        }
                    }                    
                }
            }
    }

    public void undo(Move move) {
        if (board[move.getCol()][move.getRow()] == move.getColor()) {
            // System.out.println("[undo()] Undoing move: " + move);
            handleCaptureUndo(move);
            
            board[move.getCol()][move.getRow()] = Mark.Empty;
            Zobrist.updateHash(move, false);
            playedMoves.remove(move);
            turns--;
        }
    }

    private void handleCaptureUndo(Move move) {
        if(move.isCapture()) {
            if (move.getColor() == Mark.Black) {
                blackCaptures--;
            } else {
                redCaptures--;
            }

            for (Move capture : move.getCaptureList()) {
                for (Move playedMove : playedMoves) {
                    if(playedMove.equals(capture) && playedMove.isCaptured()) {
                        playedMove.setCaptured(false);
                        Zobrist.updateHash(playedMove, true);                       
                        board[playedMove.getCol()][playedMove.getRow()] = playedMove.getColor();
                    }
                }
            }
        }
    }

    public int getCaptures(Mark mark) {
        if (mark == Mark.Black) {
            return blackCaptures;
        }

        return redCaptures;
    }

    public boolean checkWin(Mark mark) {
        int markCaptures = getCaptures(mark);
        
        for (Move move : playedMoves) {
            if(move.getColor() == mark) { 
                for (int[] dir : Solvers.DIRECTIONS) {
                    return Solvers.verifyConnectionCount(board, move.getCol(), move.getRow(), move.getColor(), 5, dir);
                }
            }
        }

        return markCaptures >= 5;
    }

    public int evaluate(Mark mark) {      
        // System.out.println("[evaluate()] Evaluating board for: " + mark);  
        int markScore = 0;        
        int oppScore = 0;
        int markThreatCount = 0;
        int oppThreatCount = 0;        
        
        Mark oppMark = mark.getOpponent();
        // System.out.println("[evaluate()] Opponent: " + oppMark);

        if(checkWin(mark) || checkWin(oppMark)) {
            // System.out.println("[evaluate()] " + mark + " has won");
            return Board.WINNING_SCORE;
        }
        
        for (Move move : playedMoves) {
            // System.out.println("[evaluate()] Evaluating move: " + move);            
            int moveScore = 0;
            int threatCount = 0;            

            for (int[] dir : Solvers.DIRECTIONS) {
                // System.out.println("[evaluate()] Checking direction: " + dir[0] + ", " + dir[1]);
                if(Solvers.isBlocking(board, move, dir)) {
                    threatCount++;
                    moveScore += 60;
                    
                    Move blockedMove = new Move(move.getCol() + dir[0], move.getRow() + dir[1], move.getColor().getOpponent());
                    // System.out.println("[evaluate()]" + move + " is blocking");                    
                    if(Solvers.verifyConnectionCount(board, blockedMove.getCol(), blockedMove.getRow(), blockedMove.getColor(), 4, dir)) {
                        // System.out.println("[evaluate()] " + move + " is a row of 4");                        
                        moveScore += 30;
                    }
                    
                    if(Solvers.verifyConnectionCount(board, blockedMove.getCol(), blockedMove.getRow(), blockedMove.getColor(), 3, dir)) {
                        // System.out.println("[evaluate()] " + move + " is a row of 3");
                        moveScore += 20;
                    }
                }
                
                if(Solvers.verifyConnectionCount(board, move.getCol(), move.getRow(), move.getColor(), 4, dir)) {
                    // System.out.println("[evaluate()] " + move + " is a row of 4");
                    threatCount++;
                    moveScore += 50;
                }

                if(Solvers.verifyConnectionCount(board, move.getCol(), move.getRow(), move.getColor(), 3, dir)) {
                    // System.out.println("[evaluate()] " + move + " is a row of 3");
                    threatCount++;
                    moveScore += 30;
                }
                
                if(Solvers.isCapture(board, move, dir)) {
                    // System.out.println("[evaluate()] " + move + " is a capture move");
                    threatCount++;
                    moveScore += 1000;
                }
            }

            // System.out.println("[evaluate()] Move score: " + moveScore);
            if(move.getColor() == mark) {
                markThreatCount += threatCount;
                markScore += moveScore;
            } else {
                oppThreatCount += threatCount;
                oppScore += moveScore;
            }
        }

        markScore += markThreatCount * 5;
        oppScore += oppThreatCount * 5;
        // System.out.println("[evaluate()] Final score: " + (markScore - oppScore));
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
        // Have to make a copy because playing a move modifies the original list and that causes errors
        ArrayList<Move> moves = new ArrayList<Move>();        
        ArrayList<Move> playedMovesCopy = new ArrayList<Move>(playedMoves);

        for (Move move : playedMovesCopy) {
            if(move.isCaptured()) {
                continue;
            }

            // System.out.println("[getPossibleMoves()] Getting empty squares around move: " + move);
            for (int i = move.getCol() - squareDist; i <= move.getCol() + squareDist; i++) {
                for (int j = move.getRow() - squareDist; j <= move.getRow() + squareDist; j++) {
                    if(i == move.getCol() && j == move.getRow()) {
                        continue;
                    }

                    int col = i;
                    int row = j;
                    
                    if(turns == 2 && mark == Mark.Red) {
                        col = col + (col - move.getCol()) * 2;
                        row = row + (row - move.getRow()) * 2;
                        
                        if(move.getColor() != Mark.Red) {
                            break;
                        }
                    }

                    if (isInbound(col, row) && board[col][row] == Mark.Empty) {
                        Move newMove = new Move(col, row, mark);
                        // System.out.println("[getPossibleMoves()] Evaluating move: " + newMove);
                        
                        play(newMove);
                        newMove.setScore(evaluate(mark));
                        undo(newMove);

                        if(!moves.contains(newMove)) {
                            moves.add(newMove);
                        }                        
                    }
                }
            }
        }

        moves.sort((Move m1, Move m2) -> m1.compareTo(m2));
        if(moves.size() > 10) {
            moves = new ArrayList<Move>(moves.subList(0, 10));
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

    /**
     * Checks if a certain column and row is within the bounds of the board
     * @param col Column to check
     * @param row Row to check
     * @return true if the column and row are within the bounds of the board, false otherwise
     */
    public static boolean isInbound(int col, int row) {
        if (col < 0 || col >= BOARD_SIZE || row < 0 || row >= BOARD_SIZE) {
            return false;
        }

        return true;
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
