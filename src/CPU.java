import java.util.ArrayList;
import java.util.Random;

public class CPU {    
    private final Mark _mark;
    private final static int MAX_DEPTH = 3;
    private int numberOfExploredNodes = 0;

    public CPU(Mark mark) {
        _mark = mark;
    }

    public Mark get_mark() {
        return _mark;
    }

    public Move getNextMove(Board board) {
        // System.out.println("[getNextMove()] Getting Next Move for " + _mark);
        ArrayList<Move> moves = getNextMoveNegaMax(board);
        Random random = new Random();
        int r = random.nextInt(moves.size());
        return moves.get(r);
    }

    public ArrayList<Move> getNextMoveNegaMax(Board board) {
        // System.out.println("[getNextMoveNegaMax()] Getting Next Move with NegaMax for " + _mark); 
        numberOfExploredNodes = 0;
        ArrayList<Move> moves = new ArrayList<>();

        // First move
        if (_mark == Mark.Red && board.getTurns() == 0) {        
            moves.add(new Move("H8", Mark.Red));
            return moves;
        }

        ArrayList<Move> possibleMoves;
        possibleMoves = board.getPossibleMoves(_mark);        

        int max = Integer.MIN_VALUE;
        for (Move move : possibleMoves) {
            // System.out.println("[getNextMoveNegaMax()] Evaluating Node: " + move + ", Depth: " + MAX_DEPTH);
            board.play(move);
            
            if(Zobrist.hashExists()) {
                move.setScore(Zobrist.getScore());
            } else {
                move.setScore(negaMax(board, _mark, -Board.WINNING_SCORE, Board.WINNING_SCORE, MAX_DEPTH));
                Zobrist.addEntry(move.getScore());
            }
            
            board.undo(move);
            // System.out.println("Score negaMax: " + score + " Move: " + move);

            if(move.getScore() > max) {
                max = move.getScore();
                moves.clear();
                moves.add(move);
            } else if(move.getScore() == max && !moves.contains(move)) {
                moves.add(move);
            }
        }        
        
        System.out.println("[getNextMoveNegaMax()] Number of Explored Nodes: " + numberOfExploredNodes);
        return moves;
    }


    public int negaMax(Board board, Mark mark, int alpha, int beta, int depth) {
        // System.out.println("[negaMax()] Evaluating Board for: " + mark + ", Depth: " + depth);
        // System.out.println("[negaMax()] Alpha: " + alpha + ", Beta: " + beta);
        numberOfExploredNodes++;
        if(depth == 0 || board.checkWin(mark.getOpponent()) || board.checkWin(mark)) {
            return board.evaluate(mark);
        }

        ArrayList<Move> possibleMoves = board.getPossibleMoves(mark.getOpponent());

        int maxScore = Integer.MIN_VALUE;
        for (Move possibleMove : possibleMoves) {
            // System.out.println("[()] Evaluating Node: " + possibleMove + ", Depth: " + depth);
            board.play(possibleMove);            
                        
            possibleMove.setScore(-negaMax(board, mark.getOpponent(), -beta, -alpha, depth - 1));     

            // System.out.println("[getNextMoveNegaMax()] Score for " + possibleMove + ": " + possibleMove.getScore() + ", Depth: " + depth);
            board.undo(possibleMove);
            
            maxScore = Math.max(maxScore, possibleMove.getScore());
            alpha = Math.max(alpha, maxScore);
            // System.out.println("[getNextMoveNegaMax()] Alpha: " + alpha + ", MaxScore: " + maxScore);

            if (alpha >= beta) {
                break;
            }
        }

        // System.out.println("[getNextMoveNegaMax()] Returning: " + maxScore);
        return maxScore;
    }
}
