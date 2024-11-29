import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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

    /**
     * Handles the capturing of pieces when a move is played
     * @param move The move that was played
     */
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

    /**
     * Undoes a move
     * @param move The move to undo
     */
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

    /**
     * Undoes the capturing of pieces when a move is undone
     * @param move The move to undo
     */
    private void handleCaptureUndo(Move move) {
        if(move.isCapture()) {
            move.setCapture(false);
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

    /**
     * Returns the number of captures for a certain color
     * @param mark The color to get the captures for
     * @return The number of captures for the color
     */
    public int getCaptures(Mark mark) {
        if (mark == Mark.Black) {
            return blackCaptures;
        }

        return redCaptures;
    }

    /**
     * Checks if a certain color has won the game
     * @param mark The color to check for
     * @return true if the color has won, false otherwise
     */
    public boolean checkWin(Mark mark) {
        int markCaptures = getCaptures(mark);

        for (Move[] chain : getChains()) {
            if (chain.length >= 5 && chain[0].getColor() == mark) {
                return true;
            }
        }

        return markCaptures >= 5;
    }

    /**
     * Evaluates the board for a certain color
     * @param mark The color to evaluate the board for
     * @return The score of the board for the color
     */
    public int evaluate(Mark mark) {
        // System.out.println("[evaluate()] Evaluating board for: " + mark);  
        int markScore = 0;        
        int oppScore = 0;
        int markThreatCount = 0;
        int oppThreatCount = 0;        
        
        Mark oppMark = mark.getOpponent();
        // System.out.println("[evaluate()] Opponent: " + oppMark);
        if(checkWin(mark)) {
            // System.out.println("[evaluate()] " + mark + " has won");
            return Board.WINNING_SCORE;
        } else if (checkWin(oppMark)) {
            return -Board.WINNING_SCORE;
        }
        
        for (Move move : playedMoves) {
            // System.out.println("[evaluate()] Evaluating move: " + move);            
            int moveScore = 0;
            int threatCount = 0;  

            for (int[] dir : Solvers.DIRECTIONS) {
                // System.out.println("[evaluate()] Checking direction: " + dir[0] + ", " + dir[1]);
                if(Solvers.isBlocking(board, move, dir)) {
                    threatCount++;
                    moveScore += 500;
                                        
                    Move blockedMove = new Move(move.getCol() + dir[0], move.getRow() + dir[1], move.getColor().getOpponent());
                    // System.out.println("[evaluate()]" + move + " is blocking");                    
                    if(Solvers.verifyConnectionCount(board, blockedMove.getCol(), blockedMove.getRow(), blockedMove.getColor(), 4, dir)) {
                        // System.out.println("[evaluate()] " + move + " is a row of 4");
                        threatCount++;
                        moveScore += 1250;
                    }
                    
                    if(Solvers.verifyConnectionCount(board, blockedMove.getCol(), blockedMove.getRow(), blockedMove.getColor(), 3, dir)) {
                        // System.out.println("[evaluate()] " + move + " is a row of 3");
                        threatCount++;
                        moveScore += 1000;
                    }
                    
                    if(Solvers.verifyConnectionCount(board, blockedMove.getCol(), blockedMove.getRow(), blockedMove.getColor(), 2, dir)) {
                        // System.out.println("[evaluate()] " + move + " is a row of 3");
                        threatCount++;
                        moveScore += 900;

                        if(Solvers.isCapture(board, move, dir)) {
                            // System.out.println("[evaluate()] " + move + " is a capture move");
                            threatCount++;
                            moveScore += 5000;
                        }
                    }                                                 
                }

    /**
     * Returns all the chains on the board
     * @return List of all the chains on the board
     */
    public static ArrayList<Move[]> getChains() {
        ArrayList<Move[]> chains = new ArrayList<Move[]>();
        ArrayList<Move> playedMovesCopy = new ArrayList<Move>(playedMoves);

        playedMovesCopy.removeIf(move -> move.isCaptured());
        chains.addAll(getColChains(playedMovesCopy));    
        chains.addAll(getRowChains(playedMovesCopy));    
        chains.addAll(getDiagonalChains(playedMovesCopy));

        return chains;
    }

    /**
     * Returns all the column chains on the board
     * @param moves List of all the moves played
     * @return List of all the column chains on the board
     */
    private static ArrayList<Move[]> getColChains(ArrayList<Move> moves) {
        ArrayList<Move[]> colChains = new ArrayList<Move[]>();

        moves.sort(Comparator
            .comparing(Move::getColor)
            .thenComparingInt(Move::getCol)
            .thenComparingInt(Move::getRow));

        for (int i = 0; i < moves.size(); i++) {
            Move currentMove = moves.get(i);

            if(currentMove.isCaptured()) {
                continue;
            }

            int chainCount = 1;
            List<Move> chain = new ArrayList<Move>();
            chain.add(currentMove);

            for(int j = i + 1; j < moves.size(); j++) {
                Move chainedMove = moves.get(j);
                
                if(Solvers.verifyConnectionCount(board, move.getCol(), move.getRow(), move.getColor(), 4, dir)) {
                    // System.out.println("[evaluate()] " + move + " is a row of 4");
                    threatCount++;
                    moveScore += 1250;
                if(chainedMove.isCaptured()) {
                    continue;
                }

                if(chainedMove.getColor() == currentMove.getColor() && chainedMove.getCol() == currentMove.getCol() && chainedMove.getRow() == currentMove.getRow() + chainCount) {
                    chainCount++;
                    chain.add(chainedMove);
                } else {                    
                    break;
                }
            }
            
            if(chainCount > 1) {
                boolean chainFound = findChain(chain, colChains);
                
                if(Solvers.verifyConnectionCount(board, move.getCol(), move.getRow(), move.getColor(), 3, dir)) {
                    // System.out.println("[evaluate()] " + move + " is a row of 3");
                    threatCount++;
                    moveScore += 1000;
                }                            
                if(!chainFound) {
                    colChains.add(chain.toArray(new Move[chain.size()])); 
                }    
            }
        }

        return colChains;
    }

            // System.out.println("[evaluate()] Move score: " + moveScore);
            if(move.getColor() == mark) {
                markThreatCount += threatCount;
                markScore += moveScore;

        if(markThreatCount > oppThreatCount) {
            markScore += 2000;
        } else if(markThreatCount < oppThreatCount) {
            oppScore += 2000;
        }                
    /**
     * Returns all the row chains on the board
     * @param moves List of all the moves played
     * @return List of all the row chains on the board
     */
    private static ArrayList<Move[]> getRowChains(ArrayList<Move> moves) {
        ArrayList<Move[]> rowChains = new ArrayList<>();

        int score = markScore - oppScore;
        return score;
        moves.sort(Comparator
            .comparing(Move::getColor)
            .thenComparingInt(Move::getRow)
            .thenComparingInt(Move::getCol));

        for (int i = 0; i < moves.size(); i++) {
            Move currentMove = moves.get(i);

            if(currentMove.isCaptured()) {
                continue;
            }

            int chainCount = 1;
            List<Move> chain = new ArrayList<Move>();
            chain.add(currentMove);

            for(int j = i + 1; j < moves.size(); j++) {
                Move chainedMove = moves.get(j);             
                
                if (chainedMove.isCaptured()) {
                    break;
                }

                if (chainedMove.getColor() == currentMove.getColor() && 
                   chainedMove.getRow() == currentMove.getRow() && 
                   chainedMove.getCol() == currentMove.getCol() + chainCount) {
                    chainCount++;
                    chain.add(chainedMove);
                } else {
                    break;
                }
            }
            
            if(chainCount > 1) {
                boolean chainFound = findChain(chain, rowChains);

                if(!chainFound) {
                    rowChains.add(chain.toArray(new Move[chain.size()]));    
                }
            }
        }        

        return rowChains;
    }

    /**
     * Returns all the diagonal chains on the board
     * @param moves List of all the moves played
     * @return List of all the diagonal chains on the board
     */
    private static ArrayList<Move[]> getDiagonalChains(ArrayList<Move> moves) {
        ArrayList<Move[]> diagonalChains = new ArrayList<>();

        // Sort by color, then by row, then by column
        moves.sort(Comparator
            .comparing(Move::getColor)
            .thenComparingInt(Move::getCol)
            .thenComparingInt(Move::getRow));

        for (Move currentMove : moves) {
            ArrayList<Move> mainDiagonalChain = new ArrayList<>();
            mainDiagonalChain.add(currentMove);

            if(currentMove.isCaptured()) {
                continue;
            }

            for (int i = 1; i < 5; i++) {
                Move tempMove = new Move(currentMove.getCol() + i, currentMove.getRow() + i, currentMove.getColor());
                if (!moves.contains(tempMove)) {
                    break;
                }

                Move chainedMove = moves.get(moves.indexOf(tempMove));
                if (chainedMove.isCaptured()) {
                    break;
                }
                
                mainDiagonalChain.add(chainedMove);
            }

            if (mainDiagonalChain.size() > 1) {
                boolean chainFound = findChain(mainDiagonalChain, diagonalChains);
                

                if(!chainFound) {
                    diagonalChains.add(mainDiagonalChain.toArray(new Move[0]));
                }
            }
        }

        // Check anti-diagonal (top-right to bottom-left)
        for (Move currentMove : moves) {
            ArrayList<Move> antiDiagonalChain = new ArrayList<>();
            antiDiagonalChain.add(currentMove);

            if (currentMove.isCaptured()) {
                continue;                
            }
            
            for (int i = 1; i < 5; i++) {
                Move tempMove = new Move(currentMove.getCol() + i, currentMove.getRow() - i, currentMove.getColor());
                if (!moves.contains(tempMove)) {
                    break;
                }

                Move chainedMove = moves.get(moves.indexOf(tempMove));
                if (chainedMove.isCaptured()) {
                    break;
                }

                antiDiagonalChain.add(chainedMove);
            }

            if (antiDiagonalChain.size() > 1) {
                boolean chainFound = findChain(antiDiagonalChain, diagonalChains);
                
                if(!chainFound) {
                    diagonalChains.add(antiDiagonalChain.toArray(new Move[0]));
                }
                
            }
        }

        return diagonalChains;
    }

    /**
     * Checks if a chain is already in the list of chains
     * @param chain The chain to check
     * @param chains List of all the chains
     * @return true if the chain is already in the list of chains, false otherwise
     */
    private static boolean findChain(List<Move> chain, ArrayList<Move[]> chains) {
        for (Move[] c : chains) {
            if(c.length > chain.size()) {
                if(Arrays.asList(c).containsAll(chain)) {
                    return true;                        
                }
            } else {
                if(chain.containsAll(Arrays.asList(c))) {
                    chains.remove(c);
                    chains.add(chain.toArray(new Move[chain.size()]));
                }
            }                                 
        }

        return false;
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

    /**
     * Returns the board
     */
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
