import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Board {
    public static final int WINNING_SCORE = 1000000;
    public static final int BOARD_SIZE = 15;

    // 0: Empty, 1: Red, 2: Black
    private final Mark[][] board;
    private static ArrayList<Move> playedMoves;
    private EvalBoard evalBoard = new EvalBoard(BOARD_SIZE);
    private int turns;
    private int blackCaptures;
    private int redCaptures;

    public Board() {
        turns = 0;
        board = new Mark[BOARD_SIZE][BOARD_SIZE];
        playedMoves = new ArrayList<Move>();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
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
            evalBoard.play(move.getRow(), move.getCol(), move.getColor().ordinal());
            evalBoard.reset();
            //evalBoard.removeCapture(move.getColor());
            handleCaptureMoves(move);
            Zobrist.updateHash(move, false);

            playedMoves.add(turns, move);
            turns++;
        }
    }

    /**
     * Handles the capturing of pieces when a move is played
     * 
     * @param move The move that was played
     */
    private void handleCaptureMoves(Move move) {
        for (int[] dir : Solvers.DIRECTIONS) {
            if (Solvers.isCapture(board, move, dir)) {
                move.setCapture(true);
                if (move.getColor() == Mark.Black) {
                    blackCaptures++;
                    evalBoard.updateCaptures(blackCaptures);
                } else {
                    redCaptures++;
                    evalBoard.updateCaptures(redCaptures);
                }

                Move firstcaptive = new Move(move.getCol() + dir[0], move.getRow() + dir[1],
                        move.getColor().getOpponent());
                Move secondCaptive = new Move(move.getCol() + dir[0] * 2, move.getRow() + dir[1] * 2,
                        move.getColor().getOpponent());

                if (playedMoves.contains(firstcaptive) && playedMoves.contains(secondCaptive)) {
                    board[firstcaptive.getCol()][firstcaptive.getRow()] = Mark.Empty;
                    board[secondCaptive.getCol()][secondCaptive.getRow()] = Mark.Empty;

                    for (Move playedMove : playedMoves) {
                        if (playedMove.equals(firstcaptive) || playedMove.equals(secondCaptive)) {
                            playedMove.setCaptured(true);                            
                            move.addCapture(playedMove);
                            evalBoard.capture(playedMove.getRow(), playedMove.getCol());                            

                            Zobrist.updateHash(playedMove, true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Undoes a move
     * 
     * @param move The move to undo
     */
    public void undo(Move move) {
        if (board[move.getCol()][move.getRow()] == move.getColor()) {
            handleCaptureUndo(move);
            Zobrist.updateHash(move, false);
            
            board[move.getCol()][move.getRow()] = Mark.Empty;      
            evalBoard.play(move.getRow(), move.getCol(), Mark.Empty.ordinal());
            playedMoves.remove(move);
            turns--;
        }
    }

    /**
     * Undoes the capturing of pieces when a move is undone
     * 
     * @param move The move to undo
     */
    private void handleCaptureUndo(Move move) {
        if (move.isCapture()) {
            //evalBoard.removeCapture(move.getColor());
            move.setCapture(false);
            if (move.getColor() == Mark.Black) {
                blackCaptures--;
                evalBoard.updateCaptures(blackCaptures);
            } else {
                redCaptures--;
                evalBoard.updateCaptures(redCaptures);
            }

            for (Move capture : move.getCaptureList()) {
                for (Move playedMove : playedMoves) {
                    if (playedMove.equals(capture) && playedMove.isCaptured()) {
                        playedMove.setCaptured(false);                        
                        board[playedMove.getCol()][playedMove.getRow()] = playedMove.getColor();
                        evalBoard.play(playedMove.getRow(), playedMove.getCol(), playedMove.getColor().ordinal());
                        Zobrist.updateHash(playedMove, true);
                    }
                }
            }
        }
    }

    /**
     * Returns the number of captures for a certain color
     * 
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
     * 
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
     * 
     * @param mark The color to evaluate the board for
     * @return The score of the board for the color
     */
    public int evaluate(Mark mark) {
        int markScore = 0;
        int oppScore = 0;
        Mark oppMark = mark.getOpponent();
        int threatCountMark = 0;
        int threatCountOpp = 0;        

        if (checkWin(mark)) {
            return WINNING_SCORE;
        } else if(checkWin(oppMark)) {
            return -WINNING_SCORE;
        }

        int threatModifier = 2000;
        int captureModifier = 10000;
        if(mark == Mark.Black) {
            markScore += evalBoard.getTotalScoreBlack() + captureModifier * blackCaptures;
            oppScore += evalBoard.getTotalScoreRed() + captureModifier * redCaptures;

            if (evalBoard.getThreatsCountBlack() > evalBoard.getThreatCountRed()) {
                markScore += threatModifier * threatCountMark;
            } else if (evalBoard.getThreatsCountBlack() < evalBoard.getThreatCountRed()) {
                oppScore += threatModifier * threatCountOpp;
            }
        } else {
            markScore += evalBoard.getTotalScoreRed() + captureModifier * blackCaptures;
            oppScore += evalBoard.getTotalScoreBlack() + captureModifier * redCaptures;

            if (evalBoard.getThreatCountRed() > evalBoard.getThreatsCountBlack()) {
                markScore += threatModifier * threatCountMark;
            } else if (evalBoard.getThreatCountRed() < evalBoard.getThreatsCountBlack()) {
                oppScore += threatModifier * threatCountOpp;
            }
        }

        int score = markScore - oppScore;
        return score;
    }

    /**
     * Returns all the chains on the board
     * 
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
     * 
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

            if (currentMove.isCaptured()) {
                continue;
            }

            int chainCount = 1;
            List<Move> chain = new ArrayList<Move>();
            chain.add(currentMove);

            for (int j = i + 1; j < moves.size(); j++) {
                Move chainedMove = moves.get(j);

                if (chainedMove.isCaptured()) {
                    continue;
                }

                if (chainedMove.getColor() == currentMove.getColor() && chainedMove.getCol() == currentMove.getCol()
                        && chainedMove.getRow() == currentMove.getRow() + chainCount) {
                    chainCount++;
                    chain.add(chainedMove);
                } else {
                    break;
                }
            }

            if (chainCount > 1) {
                boolean chainFound = findChain(chain, colChains);

                if (!chainFound) {
                    colChains.add(chain.toArray(new Move[chain.size()]));
                }
            }
        }

        return colChains;
    }

    /**
     * Returns all the row chains on the board
     * 
     * @param moves List of all the moves played
     * @return List of all the row chains on the board
     */
    private static ArrayList<Move[]> getRowChains(ArrayList<Move> moves) {
        ArrayList<Move[]> rowChains = new ArrayList<>();

        moves.sort(Comparator
                .comparing(Move::getColor)
                .thenComparingInt(Move::getRow)
                .thenComparingInt(Move::getCol));

        for (int i = 0; i < moves.size(); i++) {
            Move currentMove = moves.get(i);

            if (currentMove.isCaptured()) {
                continue;
            }

            int chainCount = 1;
            List<Move> chain = new ArrayList<Move>();
            chain.add(currentMove);

            for (int j = i + 1; j < moves.size(); j++) {
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

            if (chainCount > 1) {
                boolean chainFound = findChain(chain, rowChains);

                if (!chainFound) {
                    rowChains.add(chain.toArray(new Move[chain.size()]));
                }
            }
        }

        return rowChains;
    }

    /**
     * Returns all the diagonal chains on the board
     * 
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

            if (currentMove.isCaptured()) {
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

                if (!chainFound) {
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

                if (!chainFound) {
                    diagonalChains.add(antiDiagonalChain.toArray(new Move[0]));
                }

            }
        }

        return diagonalChains;
    }

    /**
     * Checks if a chain is already in the list of chains
     * 
     * @param chain  The chain to check
     * @param chains List of all the chains
     * @return true if the chain is already in the list of chains, false otherwise
     */
    private static boolean findChain(List<Move> chain, ArrayList<Move[]> chains) {
        ArrayList<Move[]> chainArrayCopy = new ArrayList<Move[]>(chains);
        for (Move[] c : chainArrayCopy) {
            if (c.length > chain.size()) {
                if (Arrays.asList(c).containsAll(chain)) {
                    return true;
                }
            } else {
                if (chain.containsAll(Arrays.asList(c))) {
                    chains.remove(c);
                    chains.add(chain.toArray(new Move[chain.size()]));
                }
            }
        }

        return false;
    }

    public ArrayList<Move> getPossibleMoves(Mark mark) {
        ArrayList<Move> moves = evalBoard.bestMove(mark);

        for (Move move : moves) {
            play(move);
            move.setScore(evaluate(mark));
            undo(move);
        }

        moves.sort((Move m1, Move m2) -> m1.compareTo(m2));

        // if (moves.size() > 10) {
        //     moves = new ArrayList<Move>(moves.subList(0, 10));
        // }

        return moves;
    }

    public ArrayList<Move> getPossibleMovesWithinNsquares(int N, Mark mark) {
        ArrayList<Move> moves = new ArrayList<Move>();
        ArrayList<Move> playedMovesCopy = new ArrayList<Move>(playedMoves);

        for (Move move : playedMovesCopy) {
            if (move.isCaptured()) {
                continue;
            }

            // Get all the empty squares within N squares of the played move
            for (int i = move.getCol() - N; i <= move.getCol() + N; i++) {
                for (int j = move.getRow() - N; j <= move.getRow() + N; j++) {
                    if (i == move.getCol() && j == move.getRow()) {
                        continue;
                    }

                    int col = i;
                    int row = j;

                    if (turns == 2 && mark == Mark.Red) {
                        col = col + (col - move.getCol()) * 2;
                        row = row + (row - move.getRow()) * 2;

                        if (move.getColor() != Mark.Red) {
                            break;
                        }
                    }

                    if (isInbound(col, row) && board[col][row] == Mark.Empty) {
                        Move newMove = new Move(col, row, mark);

                        if (moves.contains(newMove)) {
                            continue;
                        }

                        play(newMove);
                        newMove.setScore(evaluate(mark));
                        undo(newMove);

                        moves.add(newMove);
                    }
                }
            }
        }

        return moves;
    }

    /**
     * Checks if a certain column and row is within the bounds of the board
     * 
     * @param col Column to check
     * @param row Row to check
     * @return true if the column and row are within the bounds of the board, false
     *         otherwise
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

    public void display() {
        System.out.println("Board:");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.printf("%8d", board[i][j].ordinal());
            }
            System.out.println();
            System.out.print("\n");
        }
    }
}
