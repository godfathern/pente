import java.util.ArrayList;

public class Board {
    public static final int WINNING_SCORE = 100000;
    public static final int MAX_ADJACENCY_COUNT = 5;
    private static final int BOARD_SIZE = 15;

    // 0: Empty, 1: Red, 2: Black
    private final Mark[][] board;
    private ArrayList<Move> playedMoves;
    private int turns;
    private int blackCaptures;
    private int redCaptures;

    /**
     * Constructor for the Board class
     */
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

    /**
     * getTurns() returns the number of turns that have been played
     * @return The number of turns that have been played
     */
    public int getTurns() {
        return turns;
    }

    /**
     * play(Move move) plays a move on the board
     * @param move The move to play
     */
    public void play(Move move) {
        if (board[move.getCol()][move.getRow()] == Mark.Empty) {
            board[move.getCol()][move.getRow()] = move.getColor();
            // System.out.println("[play()] Playing move: " + move); // Debug
            for(int[] dir : Solvers.DIRECTIONS) {
                if (Solvers.isCapture(board, move, dir)) {
                    move.setCapture(true);
                    if (move.getColor() == Mark.Black) {
                        blackCaptures++;
                    } else {
                        redCaptures++;
                    }

                    // Gets the two moves that are being captured
                    Move firstCaptive = new Move(move.getCol() + dir[0], move.getRow() + dir[1], move.getColor().getOpponent());
                    Move secondCaptive = new Move(move.getCol() + dir[0] * 2, move.getRow() + dir[1] * 2, move.getColor().getOpponent());

                    // Check if playedMoves contains the two moves that are being captured
                    if(playedMoves.contains(firstCaptive) && playedMoves.contains(secondCaptive)) {
                        // Set the two moves as empty                                          
                        board[firstCaptive.getCol()][firstCaptive.getRow()] = Mark.Empty;
                        board[secondCaptive.getCol()][secondCaptive.getRow()] = Mark.Empty;
                            // Go through playedMoves and check for captured moves
                            for (Move playedMove : playedMoves) {
                                if(playedMove.equals(firstCaptive) || playedMove.equals(secondCaptive)) {
                                   playedMove.setCaptured(true);
                                   move.addCapture(playedMove);
                                }
                            }
                        }                        
                    }
                }            
            playedMoves.add(turns, move);
            turns++;
        }
    }

    /**
     * undo(Move move) undoes a move on the board
     * @param move The move to undo
     */
    public void undo(Move move) {
        if (board[move.getCol()][move.getRow()] == move.getColor()) {
            // System.out.println("[undo()] Undoing move: " + move);
            // Update the captures if the move was a capture move
            if(move.isCapture()) {
                if (move.getColor() == Mark.Black) {
                    blackCaptures--;
                } else {
                    redCaptures--;
                }
                // Iterate through the capture list and set the captured moves back to their original state
                for (Move capture : move.getCaptureList()) {
                    for (Move playedMove : playedMoves) {
                        if(playedMove.equals(capture) && playedMove.isCaptured()) {
                            playedMove.setCaptured(false);                            
                            board[playedMove.getCol()][playedMove.getRow()] = playedMove.getColor();
                        }
                    }
                }
            }
            // Cleanup undo logic
            board[move.getCol()][move.getRow()] = Mark.Empty;
            playedMoves.remove(move);
            turns--;
        }
    }

    /**
     * getCaptures(Mark mark) returns the number of captures for a certain color
     * @param mark The color to get the captures for
     * @return The number of captures for a certain color
     */
    public int getCaptures(Mark mark) {
        if (mark == Mark.Black) {
            return blackCaptures;
        }
        return redCaptures;
    }

    /**
     * checkWin(Mark mark) checks if a certain color has won based on specific parameters
     * @param mark The color to check for a win
     * @return true if the color has won, false otherwise
     */
    public boolean checkWin(Mark mark) {
        int markCaptures = getCaptures(mark);
        
        // Check if player has 5 connections in any direction
        for (Move move : playedMoves) {
            if(move.getColor() == mark) { 
                for (int[] dir : Solvers.DIRECTIONS) {
                    return Solvers.verifyConnectionCount(board, move.getCol(), move.getRow(), move.getColor(), MAX_ADJACENCY_COUNT, dir);
                }
            }
        }
        return markCaptures >= MAX_ADJACENCY_COUNT;
    }

    /**
     * evaluate(Mark mark) evaluates the board based on a certain color to determine if there is a winning move
     * @param mark The color to evaluate the board for
     * @return The score of the board
     */
    public int evaluate(Mark mark) {      
        // System.out.println("[evaluate()] Evaluating board for: " + mark);  
        int markScore = 0;        
        int oppScore = 0;
        int markThreatCount = 0;
        int oppThreatCount = 0;        
        
        Mark oppMark = mark.getOpponent();
        // System.out.println("[evaluate()] Opponent: " + oppMark);

        // Check if either player has won
        if(checkWin(mark) || checkWin(oppMark)) {
            // System.out.println("[evaluate()] " + mark + " has won");
            return Board.WINNING_SCORE;
        }
        
        // Iterate through all the played moves and evaluate them
        for (Move move : playedMoves) {
            // System.out.println("[evaluate()] Evaluating move: " + move);            
            int moveScore = 0;
            int threatCount = 0;            

            // Iterate through all the directions to check for threats
            for (int[] dir : Solvers.DIRECTIONS) {
                // System.out.println("[evaluate()] Checking direction: " + dir[0] + ", " + dir[1]);
                // Check if the move is blocking
                if(Solvers.isBlocking(board, move, dir)) {
                    threatCount++;
                    moveScore += 60;
                    
                    Move blockedMove = new Move(move.getCol() + dir[0], move.getRow() + dir[1], move.getColor().getOpponent());
                    // System.out.println("[evaluate()]" + move + " is blocking");                    
                    if(Solvers.verifyConnectionCount(board, blockedMove.getCol(), blockedMove.getRow(), blockedMove.getColor(), MAX_ADJACENCY_COUNT - 1, dir)) {
                        // System.out.println("[evaluate()] " + move + " is a row of 4");                        
                        moveScore += 30;
                    } else if(Solvers.verifyConnectionCount(board, blockedMove.getCol(), blockedMove.getRow(), blockedMove.getColor(), MAX_ADJACENCY_COUNT - 2, dir)) {
                        // System.out.println("[evaluate()] " + move + " is a row of 3");
                        moveScore += 20;
                    }
                }
                
                if(Solvers.verifyConnectionCount(board, move.getCol(), move.getRow(), move.getColor(), 4, dir)) {
                    // System.out.println("[evaluate()] " + move + " is a row of 4");
                    threatCount++;
                    moveScore += 50;
                } else if(Solvers.verifyConnectionCount(board, move.getCol(), move.getRow(), move.getColor(), 3, dir)) {
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

        markScore += markThreatCount * MAX_ADJACENCY_COUNT;
        oppScore += oppThreatCount * MAX_ADJACENCY_COUNT;
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
    @Deprecated
    public ArrayList<Move> getAllMarks(Mark mark) {
        ArrayList<Move> moves = new ArrayList<Move>();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == mark) {
                    moves.add(new Move(i, j, mark));
                }
            }
        }

        return moves;
    }

    /**
     * Returns all the moves that are possible for a certain color
     * @param mark The color to get the possible moves for
     * @return List of all the possible moves for a certain color
     */
    public ArrayList<Move> getPossibleMoves(Mark mark) {        
        int squareDist = 1;
        // System.out.println("[getPossibleMoves()] Getting possible Moves for: " + mark);
        // Have to make a copy because playing a move modifies the original list and that causes errors
        ArrayList<Move> moves = new ArrayList<Move>();        
        ArrayList<Move> playedMovesCopy = new ArrayList<Move>(playedMoves);  
        int maxScore = Integer.MIN_VALUE;

        // Iterate through all the played moves and get the empty squares around them
        for (Move move : playedMovesCopy) {
            if(move.isCaptured()) {
                continue;
            }

            // System.out.println("[getPossibleMoves()] Getting empty squares around move: " + move);
            // Iterate through all the squares around the move
            for (int i = move.getCol() - squareDist; i <= move.getCol() + squareDist; i++) {
                for (int j = move.getRow() - squareDist; j <= move.getRow() + squareDist; j++) { // Check if the square is the move itself
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

    /**
     * getPossibleMovesRedSecondTurn() returns all the possible moves for the red player on the second turn
     * @return
     */
    public ArrayList<Move> getPossibleMovesRedSecondTurn() {
        ArrayList<Move> moves = new ArrayList<>();

        // Check if the center is empty
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < MAX_ADJACENCY_COUNT; j++) {
                if (board[i][j].equals(Mark.Empty)) {
                    moves.add(new Move(i, j, Mark.Red));
                }
            }
        }

        for (int i = 0; i < MAX_ADJACENCY_COUNT; i++) {
            for (int j = 5; j < BOARD_SIZE; j++) {
                if (board[i][j].equals(Mark.Empty)) {
                    moves.add(new Move(i, j, Mark.Red));
                }
            }
        }

        for (int i = MAX_ADJACENCY_COUNT; i < 10; i++) {
            for (int j = 10; j < BOARD_SIZE; j++) {
                if (board[i][j].equals(Mark.Empty)) {
                    moves.add(new Move(i, j, Mark.Red));
                }
            }
        }

        for (int i = 10; i < BOARD_SIZE; i++) {
            for (int j = 5; j < BOARD_SIZE; j++) {
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
        return col >= 0 && col < BOARD_SIZE && row >= 0 && row < BOARD_SIZE;
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
